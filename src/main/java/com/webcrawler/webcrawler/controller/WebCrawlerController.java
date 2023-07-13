package com.webcrawler.webcrawler.controller;

import com.webcrawler.webcrawler.service.ValidationService;
import com.webcrawler.webcrawler.service.WebCrawlerService;
import java.util.List;
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

public WebCrawlerController(WebCrawlerService crawlerService,
	ValidationService validationService) {
	this.crawlerService = crawlerService;
	this.validationService = validationService;
}

@GetMapping(value = "/crawl")
public ResponseEntity crawlUrl(@RequestParam String url,
	@RequestParam(required = false, defaultValue = DEFAULT_URL_LIMIT_CRAWL) Integer limit) {
	List<String> errorMessages = validationService.validate(url,limit);

	if (!errorMessages.isEmpty()) {
	return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity<>(crawlerService.crawl(url, limit), HttpStatus.OK);
}


}
