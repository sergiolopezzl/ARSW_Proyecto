package eci.arsw.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eci.arsw.dto.ProductResponseDto;
import eci.arsw.model.Product;
import eci.arsw.model.ProductOffer;
import eci.arsw.model.User;
import eci.arsw.repository.ProductOfferRepository;
import eci.arsw.repository.ProductRepository;
import eci.arsw.repository.UsuarioRepository;
import eci.arsw.utility.Constants.ProductStatus;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ProductRepository productDao;
	
	@Autowired
	private ProductOfferRepository productOfferDao;
	
	@Autowired
	private UsuarioRepository userDao;
	
	@PostMapping("add")
	public ResponseEntity<ProductResponseDto> addProduct(@RequestBody Product product) {

		ProductResponseDto response = new ProductResponseDto();

		if (product == null) {
			response.setResponseMessage("bad request - missing request body");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		if (product.getName() == null || product.getDescription() == null || product.getPrice() == null
				|| product.getSellerId() == 0 || product.getEndDate() == null) {
			response.setResponseMessage("bad request - missing fields");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		Optional<User> optional = userDao.findById(product.getSellerId());

		if (optional.isEmpty()) {
			response.setResponseMessage("seller not found - failed to create auction");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User seller = optional.get();
		
		String createdDateTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		
		product.setSeller(seller);
		product.setCreatedDate(createdDateTime);
		product.setStatus(ProductStatus.AVAILABLE.value());

		Product savedProduct = this.productDao.save(product);

		if (savedProduct == null) {
			response.setResponseMessage("failed to create the auction");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			response.setProduct(savedProduct);
			response.setResponseMessage("Auction created successful");
			response.setSuccess(true);

			messagingTemplate.convertAndSend("/topic/auctionCreated", savedProduct);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
		}

	}

	@GetMapping("fetch")
	public ResponseEntity<ProductResponseDto> fetchProductById(@RequestParam(name = "productId") int productId) {
		ProductResponseDto response = new ProductResponseDto();

		if (productId == 0) {
			response.setResponseMessage("missing product id");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Optional<Product> optional = productDao.findById(productId);

		if (optional.isEmpty()) {
			response.setResponseMessage("product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = optional.get();

		if (product == null) {
			response.setResponseMessage("product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}
		
		List<ProductOffer> offers = this.productOfferDao.findByProduct(product);
		product.setOffers(offers);
		
		response.setProduct(product);
		response.setResponseMessage("Product fetched successful");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);

	}

	@GetMapping("fetch/all")
	public ResponseEntity<ProductResponseDto> fetchAllProduct(@RequestParam(name = "status") String status) {
		ProductResponseDto response = new ProductResponseDto();

		if (status == null) {
			response.setResponseMessage("missing parameter");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Product> products = this.productDao.findByStatusIn(Arrays.asList(status));
		
		if(products == null) {
			response.setResponseMessage("products not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
		}
		
		response.setProducts(products);
		response.setResponseMessage("Products fetched successful");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);

	}

	@GetMapping("fetch/seller-wise")
	public ResponseEntity<ProductResponseDto> fetchAllSellerProduct(@RequestParam(name = "sellerId") int sellerId,
			@RequestParam(name = "status") String status) {
		ProductResponseDto response = new ProductResponseDto();

		if (status == null || sellerId == 0) {
			response.setResponseMessage("missing parameter");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Optional<User> optional = userDao.findById(sellerId);

		if (optional.isEmpty()) {
			response.setResponseMessage("seller not found - failed to fetch auctions");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User seller = optional.get();
		
		List<Product> products = this.productDao.findBySellerAndStatusIn(seller, Arrays.asList(status));
		
		if(products == null) {
			response.setResponseMessage("products not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
		}
		
		response.setProducts(products);
		response.setResponseMessage("Products fetched successful");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);

	}

}
