package com.devsuperior.dsclient.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.services.ClientService;
import com.devsuperior.dsclient.services.exceptions.DatabaseException;
import com.devsuperior.dsclient.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsclient.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClientResource.class)
public class ClientResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ClientService service;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private ClientDTO clientDTO;
	private PageImpl<ClientDTO> page;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 100L;
		dependentId = 2L;
		clientDTO = Factory.createClientDTO();
		page = new PageImpl<>(List.of(clientDTO));
		
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
		doNothing().when(service).delete(existingId);
		
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		when(service.update(eq(existingId), any())).thenReturn(clientDTO);
		
		when(service.findAllPaged((PageRequest)any())).thenReturn(page);
		
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		when(service.findById(existingId)).thenReturn(clientDTO);
		
		when(service.insert(any())).thenReturn(clientDTO);
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
	
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenDependentId() throws Exception{
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", dependentId)
				.accept(MediaType.APPLICATION_JSON));
	
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExist() throws Exception{
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
	
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(clientDTO);
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnClientWhenIdExist() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(clientDTO);
		
		ResultActions result = mockMvc.perform(put("/clients/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/clients").accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
	}
	
	@Test
	public void findByIdShouldThrowNotFoundWhenIdExist() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExist() throws Exception{
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void insertShouldInsertClient() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(clientDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(clientDTO.getId()));
	}
}
