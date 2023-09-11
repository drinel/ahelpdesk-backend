package com.william.ahelpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.william.ahelpdesk.services.DBService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	DBService dbService;
	
	@Bean  
	public void instanciaDB() {
		this.dbService.instanciaDB();
	}
}
