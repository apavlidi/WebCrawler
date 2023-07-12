package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {

private final Queue<String> urlQueue;
private final List<String> visitedURLs;

private final ComplianceService complianceService;

private final List<String> validUrls;

public WebCrawlerService(ComplianceService complianceService) {
	this.complianceService = complianceService;
	urlQueue = new LinkedList<>();
	visitedURLs = new ArrayList<>();
	validUrls = new ArrayList<>();
}

public List<String> crawl(String rootURL, int limit) {
	List<String> disallowedPages = complianceService.retrieveDisallowedPages(rootURL);
	String domainName = getDomainName(rootURL);

	urlQueue.add(rootURL);
	visitedURLs.add(rootURL);

	while (!urlQueue.isEmpty()) {

	String s = urlQueue.remove();
	String rawHTML = "";
	try {
		URL url = new URL(s);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine = in.readLine();

		while (inputLine != null) {
		rawHTML += inputLine;

		inputLine = in.readLine();
		}
		in.close();
	} catch (Exception e) {
		e.printStackTrace();
	}

	String urlPattern =
		"((?:https?|http)://(?:www\\.)?" + Pattern.quote(domainName) + "[^\\s\"]*)";
	Pattern pattern = Pattern.compile(urlPattern);
	Matcher matcher = pattern.matcher(rawHTML);

	limit = getUrl(limit, matcher, disallowedPages);

	if (limit == 0) {
		break;
	}
	}

	return validUrls;
}

private static String getDomainName(String rootURL) {
	return rootURL.replaceAll("http(s)?://|www\\.|/.*", "");
}

private int getUrl(int limit, Matcher urlMatcher, List<String> disallowedPages) {
	while (urlMatcher.find()) {
	String actualURL = urlMatcher.group();

	if (!visitedURLs.contains(actualURL) && !disallowedPages.contains(actualURL)) {
		visitedURLs.add(actualURL);
		validUrls.add(actualURL);
		urlQueue.add(actualURL);
	}

	if (limit == 0) {
		break;
	}
	limit--;
	}
	return limit;
}
}
