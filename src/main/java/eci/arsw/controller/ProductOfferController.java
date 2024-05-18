package eci.arsw.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eci.arsw.dto.ProductOfferRequestDto;
import eci.arsw.dto.ProductResponseDto;
import eci.arsw.model.Product;
import eci.arsw.model.ProductOffer;
import eci.arsw.model.User;
import eci.arsw.repository.ProductOfferRepository;
import eci.arsw.repository.ProductRepository;
import eci.arsw.repository.UsuarioRepository;
import eci.arsw.utility.Constants.ProductOfferStatus;
import eci.arsw.utility.Constants.ProductStatus;

@RestController
@RequestMapping("api/product/offer")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductOfferController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ProductRepository productDao;
	
	@Autowired
	private ProductOfferRepository productOfferDao;
	
	@Autowired
	private UsuarioRepository userDao;

	@PostMapping("add")
	public ResponseEntity<ProductResponseDto> addOffer(@RequestBody ProductOfferRequestDto request) {

		ProductResponseDto response = new ProductResponseDto();

		Long createdDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		if (request == null) {
			response.setResponseMessage("bad request - missing request body");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getUserId() == 0 || request.getAmount() == null || request.getProductId() == 0) {
			response.setResponseMessage("bad request - missing fields");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		Optional<User> optional = userDao.findById(request.getUserId());

		if (optional.isEmpty()) {
			response.setResponseMessage("User not found - failed to add bid");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User buyer = optional.get();
		
		Optional<Product> optionalProduct = productDao.findById(request.getProductId());

		if (optionalProduct.isEmpty()) {
			response.setResponseMessage("product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = optionalProduct.get();

		if (product == null) {
			response.setResponseMessage("product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getUserId() == product.getSeller().getId()) {
			response.setResponseMessage("You can't Bid on your own Product!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(createdDateTime > Long.valueOf(product.getEndDate())) {
			response.setResponseMessage("Failed to add your Bid, Auction Time for this product is ended!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(!product.getStatus().equals(ProductStatus.AVAILABLE.value())) {
			response.setResponseMessage("Failed to add your Bid, product not available currently for Auction");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getAmount().compareTo(product.getPrice()) <= 0) {
			response.setResponseMessage("Bid Amount should be greater than the base price!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(request.getAmount().compareTo(buyer.getWalletAmount()) > 0) {
			response.setResponseMessage("Can't Add Bid, Insufficient Balance in Wallet!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		List<ProductOffer> offers = this.productOfferDao.findByProductOrderByIdDesc(product);
		
		if(!CollectionUtils.isEmpty(offers)) {
			ProductOffer highestOffer = offers.get(0);
			
			if(highestOffer.getUser().getId() == request.getUserId()) {
				response.setResponseMessage("Failed to add Bid, Highest Bid for this Auction is add by you only!!!");
				response.setSuccess(false);

				return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
			}
			
			if(request.getAmount().compareTo(highestOffer.getAmount()) <= 0) {
				response.setResponseMessage("Failed to add Bid, Bid amount should be higher than current highest Bid!!!");
				response.setSuccess(false);

				return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
			}
		}
		
		
		
		ProductOffer offer = new ProductOffer();
		offer.setAmount(request.getAmount());
		offer.setDateTime(String.valueOf(createdDateTime));
		offer.setProduct(product);
		offer.setStatus(ProductOfferStatus.ACTIVE.value());
		offer.setUser(buyer);

		ProductOffer savedProductOffer = this.productOfferDao.save(offer);

		if (savedProductOffer == null) {
			response.setResponseMessage("failed to add bid");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			
			List<ProductOffer> updatedOffers = this.productOfferDao.findByProduct(product);
			product.setOffers(updatedOffers);
			
			response.setProduct(product);
			response.setResponseMessage("Your Bid Added Successful!!!");
			response.setSuccess(true);

			messagingTemplate.convertAndSend("/topic/offerMade", savedProductOffer);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
		}

	}

}
