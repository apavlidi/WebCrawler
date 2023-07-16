package com.webcrawler.app.service;

import com.webcrawler.app.exception.ResourceReadException;
import com.webcrawler.app.web.UrlResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {

  private Queue<String> urlQueue;

  private static final Logger logger = LogManager.getLogger(WebCrawlerService.class);

  private Set<String> visitedURLs;

  private final ComplianceService complianceService;
  private final ResourceReaderService resourceReader;

  @Autowired
  public WebCrawlerService(ComplianceService complianceService,
      ResourceReaderService resourceReader) {
    this.complianceService = complianceService;
    this.resourceReader = resourceReader;
  }

  public List<UrlResponse> crawl(String rootURL, int limit) {
    initializeCrawl(rootURL);

    List<UrlResponse> urlResponses = new ArrayList<>();
    List<String> disallowedPages = complianceService.retrieveDisallowedPages(rootURL);
    Pattern pattern = retrieveDomainUrlPattern(rootURL);

    while (!urlQueue.isEmpty() && limit > 0) {
      String currentUrl = urlQueue.remove();
      try {
        String rawHTML = resourceReader.readResource(currentUrl);
        List<String> links = extractLinks(pattern.matcher(rawHTML), disallowedPages);
        urlResponses.add(new UrlResponse(currentUrl, links));
        limit--;
      } catch (ResourceReadException e) {
        logger.error("Failed to read resource: " + currentUrl, e);
      }
    }

    return urlResponses;
  }

  private Pattern retrieveDomainUrlPattern(String rootURL) {
    String domainName = getDomainName(rootURL);
    String urlPattern = "((?:https?|http)://(?:www\\.)?" + Pattern.quote(domainName) + "[^\\s\"]*)";
    return Pattern.compile(urlPattern);
  }

  private void initializeCrawl(String rootURL) {
    urlQueue = new LinkedList<>();
    visitedURLs = new HashSet<>();
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
