package eci.arsw.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductOfferRequestDto {

	private int productId;

	private int userId;

	private BigDecimal amount;

}
