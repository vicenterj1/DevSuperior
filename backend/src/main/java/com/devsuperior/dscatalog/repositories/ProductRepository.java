package com.devsuperior.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
	
	@Query("SELECT obj FROM Product obj " // igual ao nome da entidade
			+ "INNER JOIN obj.categories cats "  // join entre as tabelas pela relação das entidades  
			+ "WHERE :category in cats")  
	Page<Product> find(Category category, Pageable pageable);
}
