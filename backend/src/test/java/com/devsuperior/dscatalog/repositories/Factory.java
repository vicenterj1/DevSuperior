package com.devsuperior.dscatalog.repositories;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L,
										"Phone",
										"Good Phone",
										800.0,
										"https://img.com/img.png",
										Instant.parse("2023-02-05T16:45:00Z"));
		product.getCategories().add(new Category(2L,"Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
