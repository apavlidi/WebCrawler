package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.web.UrlResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {

private Queue<String> urlQueue;
private List<String> visitedURLs;

private final ComplianceService complianceService;
private final HtmlReaderService htmlReader;

public WebCrawlerService(ComplianceService complianceService, HtmlReaderService htmlReader) {
	this.complianceService = complianceService;
	this.htmlReader = htmlReader;
}

public List<UrlResponse> crawl(String rootURL, int limit) {
	initializeCrawl(rootURL);

	List<UrlResponse> urlsResponse = new ArrayList<>();
	List<String> disallowedPages = complianceService.retrieveDisallowedPages(rootURL);
	Pattern pattern = retrieveDomainUrlPattern(rootURL);

	while (!urlQueue.isEmpty() && limit > 0) {
	String currentUrl = urlQueue.remove();
	String rawHTML = htmlReader.getRawHTML(currentUrl);
	List<String> links = extractLinks(pattern.matcher(rawHTML), disallowedPages);
	urlsResponse.add(new UrlResponse(currentUrl, links));
	limit--;
	}

	return urlsResponse;
}

private Pattern retrieveDomainUrlPattern(String rootURL) {
	String domainName = getDomainName(rootURL);
	String urlPattern = "((?:https?|http)://(?:www\\.)?" + Pattern.quote(domainName) + "[^\\s\"]*)";
	return Pattern.compile(urlPattern);
}

private void initializeCrawl(String rootURL) {
	urlQueue = new LinkedList<>();
	visitedURLs = new ArrayList<>();
	urlQueue.add(rootURL);
	visitedURLs.add(rootURL);
}

private List<String> extractLinks(Matcher matcher, List<String> disallowedPages) {
	List<String> links = new ArrayList<>();

	while (matcher.find()) {
	String actualURL = matcher.group();
	if (!visitedURLs.contains(actualURL) && !disallowedPages.contains(actualURL)) {
		visitedURLs.add(actualURL);
		urlQueue.add(actualURL);
		links.add(actualURL);
	}
	}

	return links;
}

private String getDomainName(String rootURL) {
	return rootURL.replaceAll("http(s)?://|www\\.|/.*", "");
}

}
