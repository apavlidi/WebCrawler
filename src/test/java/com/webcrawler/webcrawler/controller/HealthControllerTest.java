package com.webcrawler.webcrawler.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.webcrawler.webcrawler.HealthController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class HealthControllerTest {

  @Test
  public void should_return_status_ok_if_service_is_healthy() {
    HealthController healthController = new HealthController();
    ResponseEntity<String> responseEntity = healthController.getHealthStatus();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals("The service is healthy", responseEntity.getBody());
  }
}
