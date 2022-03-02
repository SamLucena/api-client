package com.devsuperior.dsclient.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entities.Client;
import com.devsuperior.dsclient.repositories.ClientRepository;
import com.devsuperior.dsclient.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsclient.tests.Factory;

@SpringBootTest
@Transactional
public class ClientServiceIT {
	
	@Autowired
	private ClientService service;
	
	@Autowired
	private ClientRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private ClientDTO clientDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 100L;
		clientDTO = Factory.createClientDTO();
	}
	
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ClientDTO> page = service.findAllPaged(pageRequest);
	
		Assertions.assertTrue(page.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ClientDTO> page = service.findAllPaged(pageRequest);
	
		Assertions.assertFalse(page.isEmpty());
	}
	
	@Test
	public void deleteShouldDeleteClientDTOWhenIdExist() {
		
		service.delete(existingId);
		
		Optional<Client> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, clientDTO);
		});
		
	}
	
	@Test
	public void updateShouldUpdateClientDTOWhenIdExist() {
		
		clientDTO = service.update(existingId, clientDTO);
		
		Assertions.assertNotNull(clientDTO);
		Assertions.assertEquals(existingId, clientDTO.getId());
	}
	
	@Test
	public void insertShouldInsertObject() {
		
		clientDTO = service.insert(clientDTO);
		
		Assertions.assertNotNull(clientDTO);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findByIdShouldReturnClientDTOWhenIdExist() {
		
		ClientDTO dto = service.findById(existingId);
		
		Assertions.assertEquals(existingId, dto.getId());
		Assertions.assertNotNull(dto);
		
	}
}
