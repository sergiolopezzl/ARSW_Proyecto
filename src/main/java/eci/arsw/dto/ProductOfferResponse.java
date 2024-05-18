package eci.arsw.dto;

import java.util.ArrayList;
import java.util.List;

import eci.arsw.model.ProductOffer;
import lombok.Data;

@Data
public class ProductOfferResponse extends CommonApiResponse {
	
	List<ProductOffer> offers = new ArrayList<>();

}
