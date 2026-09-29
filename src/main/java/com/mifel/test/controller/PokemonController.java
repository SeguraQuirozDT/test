package com.mifel.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RequestMapping(path = "/api/pokemon")
@RestController
public class PokemonController {
	
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	@GetMapping
	public ResponseEntity<String> getPokemonDitto() {
		
		String URI = "https://pokeapi.co/api/v2/pokemon/ditto";
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(URI, String.class);
		
		return ResponseEntity.ok(result);
	}
}
