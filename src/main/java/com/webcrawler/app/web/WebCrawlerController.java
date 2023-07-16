package com.webcrawler.app.web;

import com.webcrawler.app.service.ValidationService;
import com.webcrawler.app.service.WebCrawlerService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebCrawlerController {

private final WebCrawlerService crawlerService;
private final ValidationService validationService;

private final String DEFAULT_URL_LIMIT_CRAWL = "10";

  private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerController.class);

public WebCrawlerController(WebCrawlerService crawlerService,
	ValidationService validationService) {
	this.crawlerService = crawlerService;
	this.validationService = validationService;
}

@GetMapping(value = "/crawl")
public ResponseEntity crawlUrl(@RequestParam String url,
	@RequestParam(required = false, defaultValue = DEFAULT_URL_LIMIT_CRAWL) Integer limit) {
  LOGGER.info("Crawling webpage: {} ", url);

  List<String> errorMessages = validationService.validate(url,limit);

	if (!errorMessages.isEmpty()) {
	return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity<>(crawlerService.crawl(url, limit), HttpStatus.OK);
}


}
