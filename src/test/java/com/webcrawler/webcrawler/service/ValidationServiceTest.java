package com.webcrawler.webcrawler.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webcrawler.webcrawler.service.ValidationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ValidationServiceTest {

  public static final String INVALID_URL = "example.com";
  public static final String VALID_URL = "https://example.com";
  public static final Integer VALID_LIMIT = 10;
  public static final Integer INVALID_LIMIT = -5;
  private ValidationService validationService;

  @BeforeEach
  public void setup() {
    validationService = new ValidationService();
  }
  @Test
  public void should_return_no_errors_when_valid_url_and_limit() {
    List<String> errorMessages = validationService.validate(VALID_URL, VALID_LIMIT);
    assertTrue(errorMessages.isEmpty());
  }

  @Test
  public void should_return_error_message_when_invalid_url() {
    List<String> errorMessages = validationService.validate(INVALID_URL, VALID_LIMIT);
    assertTrue(errorMessages.contains(String.format("The url:%s provided is invalid", INVALID_URL)));
  }

  @Test
  public void should_return_error_message_when_negative_limit() {
    List<String> errorMessages = validationService.validate(VALID_URL, INVALID_LIMIT);
    assertTrue(errorMessages.contains("Limit has to be greater than 0"));
  }

}
