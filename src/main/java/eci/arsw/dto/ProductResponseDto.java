package eci.arsw.dto;

import java.util.ArrayList;
import java.util.List;

import eci.arsw.model.Product;
import lombok.Data;

@Data
public class ProductResponseDto extends CommonApiResponse {
	
	private List<Product> products = new ArrayList<>();
	
	private Product product;

}
