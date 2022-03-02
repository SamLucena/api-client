package com.devsuperior.dsclient.tests;

import java.time.Instant;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entities.Client;

public class Factory {

	public static Client createClient() {
		return new Client(1L, "Sonia Braga", "233.256.459-16", 2330.0, Instant.parse("2021-09-22T00:00:00Z"), 3);
	}
	
	public static ClientDTO createClientDTO() {
		return new ClientDTO(createClient());
	}
}
