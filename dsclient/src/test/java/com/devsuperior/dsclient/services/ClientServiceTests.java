package com.devsuperior.dsclient.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entities.Client;
import com.devsuperior.dsclient.repositories.ClientRepository;
import com.devsuperior.dsclient.services.exceptions.DatabaseException;
import com.devsuperior.dsclient.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsclient.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

	@InjectMocks
	private ClientService service;
	
	@Mock
	private ClientRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private Client client;
	private ClientDTO clientDTO;
	private PageImpl<Client> clients;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 100L;
		dependentId = 2L;
		client = Factory.createClient();
		clientDTO = Factory.createClientDTO();
		clients = new PageImpl<>(List.of(client));
		
		when(repository.findAll((Pageable)any())).thenReturn(clients);
		
		when(repository.save(any())).thenReturn(client);
		
		when(repository.getOne(existingId)).thenReturn(client);
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		when(repository.findById(existingId)).thenReturn(Optional.of(client));
		
		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void insertShouldInsertClient() {
		
		clientDTO = service.insert(clientDTO);
		
		Assertions.assertNotNull(clientDTO);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, clientDTO);
		});
	}
	
	@Test
	public void updateShouldUpdateClientAndReturnClientWhenIdExist() {
		
		clientDTO = service.update(existingId, clientDTO);
	
		Assertions.assertNotNull(clientDTO);
		Assertions.assertEquals(existingId, clientDTO.getId());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			clientDTO = service.findById(nonExistingId);
		});
		
		verify(repository, times(1)).findById(nonExistingId);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {
		
		clientDTO = service.findById(existingId);
		
		Assertions.assertNotNull(clientDTO);
		Assertions.assertEquals(clientDTO.getId(), existingId);
		
		verify(repository, times(1)).findById(existingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
		verify(repository, times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		verify(repository, times(1)).deleteById(existingId);
	}
}
