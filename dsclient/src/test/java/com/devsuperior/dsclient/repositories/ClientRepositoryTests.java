package com.devsuperior.dsclient.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dsclient.entity.Client;
import com.devsuperior.dsclient.tests.Factory;

@DataJpaTest
public class ClientRepositoryTests {

	@Autowired
	private ClientRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalElements;
	
	private Client client;
	
	@BeforeEach
	void setUp() throws Exception{
		client = Factory.createClient();
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalElements = 20L;
	}
	
	@Test
	public void findAllShouldReturnAllClients() {
		
		List<Client> list = repository.findAll();
		
		Assertions.assertNotNull(list);
	}
	
	@Test
	public void findByIdShouldReturnOptionalClientWhenIdExist() {
		
		Optional<Client> obj = repository.findById(existingId);
		
		Assertions.assertTrue(obj.isPresent());
		Assertions.assertNotNull(obj.get());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
		
		Optional<Client> obj = repository.findById(nonExistingId);
		
		Assertions.assertTrue(obj.isEmpty());
	}
	
	@Test 
	public void saveShouldSaveObjectAndReturnObject() {
		
		client.setId(null);
		client = repository.save(client);
		
		Assertions.assertNotNull(client);
		Assertions.assertEquals(countTotalElements + 1, client.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		repository.deleteById(existingId);
		Optional<Client> obj = repository.findById(existingId);
		
		Assertions.assertTrue(obj.isEmpty());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			repository.deleteById(nonExistingId);
		});
	}
}
