package com.devsuperior.dscatalog.services;

import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {


	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;
	
	/*
	 * @MockBean 
	 * private ProductRepository repository2;
	 */

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		productDTO = Factory.createProductDTO();
		
		Mockito.when(repository.findAll((Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.find(any(), any(), any())).thenReturn(page);
		
		// configurar comportamento simulado com o Mockito
		// ex.  não faça nada quando mandar deletar no bd
		Mockito.doNothing().when(repository).deleteById(existingId);
		// ex.  gerar uma exceção quando não existir
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		// ex.  gerar uma exceção quando não existir
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

	
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(repository.save(product)).thenReturn(product);
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0,10);
		
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageable);
		
		Assertions.assertNotNull(result);
		//Mockito.verify(repository).findAll(pageable);
	}
	
	
	@Test
	public void updateShouldResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId,	productDTO);
			
		});
	}
	
	@Test
	public void updateShouldReturnDTOWhenIdExists() {
	
		ProductDTO result = service.update(existingId,	productDTO);
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
	}
	
	@Test
	public void findByIdShouldResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);	
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}	

	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);	
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}	
	
	@Test
	public void deleteShouldDoNotingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);	
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
