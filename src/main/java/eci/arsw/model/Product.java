package eci.arsw.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String description;

	private BigDecimal price; // starting price

	private String status;

	private String createdDate; // in current millis

	private String endDate; // in current millis

	@ManyToOne
	@JoinColumn(name = "seller_user_id")
	private User seller;
	
	@ManyToOne
	@JoinColumn(name = "buyer_user_id")
	private User buyer;
	
	@Transient
	private int sellerId;

	@Transient
	private int buyerId;
	
	@Transient
	private List<ProductOffer> offers;

}
