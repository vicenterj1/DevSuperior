package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	/*
	 * @MockBean 
	 * private ProductRepository repository2;
	 */

	private long existingId;
	private long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		
		// configurar comportamento simulado com o Mockito
		// ex.  não faça nada quando mandar deletar no bd
		Mockito.doNothing().when(repository).deleteById(existingId);
		// ex.  gerar uma exceção quando não existir
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNotingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);	
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
