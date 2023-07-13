package com.webcrawler.webcrawler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

@GetMapping(value = "/health")
public ResponseEntity getHealthStatus(){
	return new ResponseEntity<>("The service is healthy", HttpStatus.OK);
}

}
