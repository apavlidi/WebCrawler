package com.webcrawler.webcrawler.controllers;

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

public WebCrawlerController(WebCrawlerService crawlerService) {
	this.crawlerService = crawlerService;
}

@GetMapping(value = "/crawl")
public ResponseEntity<List<String>> crawlUrl(@RequestParam String url,@RequestParam(required = false) Integer limit){
	return new ResponseEntity<>(crawlerService.crawl(url, limit), HttpStatus.OK);
}


}
