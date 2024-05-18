package eci.arsw.scheduler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import eci.arsw.model.Product;
import eci.arsw.model.ProductOffer;
import eci.arsw.model.User;
import eci.arsw.repository.ProductOfferRepository;
import eci.arsw.repository.ProductRepository;
import eci.arsw.repository.UsuarioRepository;
import eci.arsw.utility.Constants.ProductOfferStatus;
import eci.arsw.utility.Constants.ProductStatus;

@Component
@EnableScheduling
public class UpdateProductOrderTask {
	
	private final Logger LOG = LoggerFactory.getLogger(UpdateProductOrderTask.class);

	@Autowired
	private ProductRepository productDao;
	
	@Autowired
	private ProductOfferRepository productOfferDao;
	
	@Autowired
	private UsuarioRepository userDao;

	@Scheduled(fixedRate = 60000)
	public void updateProductOffers() {
		
		//LOG.info("PRODUCT ORDER UPDATE TASK START");

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime todayMidnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);

		String startTime = String.valueOf(todayMidnight.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		String endTime = String.valueOf(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Product> products = this.productDao.findByStatusInAndEndDateBetween(
				Arrays.asList(ProductStatus.AVAILABLE.value()), startTime, endTime);
		
		if(CollectionUtils.isEmpty(products)) {
			//LOG.info("No available products found");
			//LOG.info("PRODUCT ORDER UPDATE TASK END");
			return;
		}

		for (Product product : products) {

			List<ProductOffer> offers = this.productOfferDao.findByProductAndStatusIn(product, Arrays.asList(ProductOfferStatus.ACTIVE.value()));

			if (CollectionUtils.isEmpty(offers)) {

				product.setStatus(ProductStatus.UNSOLD.value());
				productDao.save(product);

				continue;
			}

			// Find the largest amount using Java Stream
			Optional<ProductOffer> maxOffer = offers.stream()
					.max((offer1, offer2) -> offer1.getAmount().compareTo(offer2.getAmount()));

			ProductOffer biggestOffer = maxOffer.get();

			// updating product
			product.setStatus(ProductStatus.SOLD.value());
			product.setBuyer(maxOffer.get().getUser());
			productDao.save(product);
			
			//LOG.info("Product Updated");

			for (ProductOffer offer : offers) {

				if (offer == biggestOffer) {
					// updating product offer entry
					offer.setStatus(ProductOfferStatus.WIN.value());
					productOfferDao.save(offer);
				}

				else {
					// updating product offer entry
					offer.setStatus(ProductOfferStatus.LOSE.value());
					productOfferDao.save(offer);
				}
			}
			//LOG.info("Product Offer Updated");
			
			User customer = biggestOffer.getUser();
			customer.setWalletAmount(customer.getWalletAmount().subtract(biggestOffer.getAmount()));
			
			this.userDao.save(customer);
			
			User seller = product.getSeller();
			seller.setWalletAmount(seller.getWalletAmount().add(biggestOffer.getAmount()));
			
			this.userDao.save(seller);
		
			//LOG.info("Debited the Wallet amount from BUYER and CREDITED the amount in Seller Wallet");

		}
		
		//LOG.info("PRODUCT ORDER UPDATE TASK END");

	}

}
