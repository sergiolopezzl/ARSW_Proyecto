package eci.arsw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eci.arsw.model.Product;
import eci.arsw.model.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByStatusIn(List<String> status);

	List<Product> findBySellerAndStatusIn(User seller, List<String> status);
	
	List<Product> findByStatusInAndEndDateBetween(List<String> status, String startTime, String endTime);
	
}
