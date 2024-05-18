package eci.arsw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eci.arsw.model.Product;
import eci.arsw.model.ProductOffer;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffer, Integer> {
	
	List<ProductOffer> findByProductAndStatusIn(Product product, List<String> status);
	List<ProductOffer> findByProduct(Product product);
	List<ProductOffer> findByProductOrderByIdDesc(Product product);

}
