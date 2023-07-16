package com.webcrawler.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.webcrawler.app.web.HealthController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
