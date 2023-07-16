package com.webcrawler.app.controller;

import com.webcrawler.app.web.WebCrawlerController;
import com.webcrawler.app.service.ValidationService;
import com.webcrawler.app.service.WebCrawlerService;
import com.webcrawler.app.web.UrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebCrawlerControllerTest {

  public static final String INVALID_URL = "invalidUrl";
  public static final int INVALID_LIMIT = -1;
  public static final int VALID_LIMIT = 10;
  public static final String VALID_URL = "https://example.com";
  @Mock
  private WebCrawlerService crawlerService;

  @Mock
  private ValidationService validationService;
  private WebCrawlerController webCrawlerController;

  @BeforeEach
  public void initialise() {
    webCrawlerController = new WebCrawlerController(crawlerService, validationService);
  }

  @Test
  public void should_crawl_root_url_and_return_urls_crawled() {
    List<UrlResponse> urlResponse = new ArrayList<>(
        List.of(
            new UrlResponse("https://monzo.com", List.of("https://monzo.com/careers"))));

    List<String> errorMessages = new ArrayList<>();
    when(validationService.validate(VALID_URL, VALID_LIMIT)).thenReturn(errorMessages);
    when(crawlerService.crawl(VALID_URL, VALID_LIMIT)).thenReturn(urlResponse);

    ResponseEntity response = webCrawlerController.crawlUrl(VALID_URL, VALID_LIMIT);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(urlResponse, response.getBody());
  }

  @Test
  public void should_return_bad_request_when_input_is_invalid() {
    List<String> errorMessages = new ArrayList<>(asList("Invalid URL", "Invalid limit"));
    when(validationService.validate(INVALID_URL, INVALID_LIMIT)).thenReturn(errorMessages);

    ResponseEntity response = webCrawlerController.crawlUrl(INVALID_URL, INVALID_LIMIT);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(errorMessages, response.getBody());
  }

  @Test
  public void should_return_internal_server_error_if_error_is_thrown() {
    WebCrawlerController controller = new WebCrawlerController(crawlerService, validationService);
    List<String> errorMessages = new ArrayList<>();
    when(validationService.validate(VALID_URL, VALID_LIMIT)).thenReturn(errorMessages);
    when(crawlerService.crawl(VALID_URL, VALID_LIMIT)).thenThrow(new RuntimeException("Internal Server Error"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.crawlUrl(VALID_URL, VALID_LIMIT));
    assertEquals("Internal Server Error", exception.getMessage());

    verify(validationService).validate(VALID_URL, VALID_LIMIT);
    verify(crawlerService).crawl(VALID_URL, VALID_LIMIT);
    verifyNoMoreInteractions(validationService, crawlerService);
  }

}