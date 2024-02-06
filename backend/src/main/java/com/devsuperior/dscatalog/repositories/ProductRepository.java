package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
	
	@Query("SELECT DISTINCT obj FROM Product obj " // igual ao nome da entidade
			+ "INNER JOIN obj.categories cats WHERE "  // join entre as tabelas pela relação das entidades  
			// QUANDO FOI USAR NO POSTGRESQL, NÃO FUNCIONOU A LINHA ABAIXO
			// ALTERADO PARA USAR LISTA
			//+ " (:category IS NULL or :category in cats) "
			+ "( COALESCE(:categories) IS NULL OR cats IN :categories) "  
			+ " AND (:name = '' OR LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) ) ")  
	//Page<Product> find(Category category, String name, Pageable pageable);
	Page<Product> find(List<Category> categories, String name, Pageable pageable);
}
