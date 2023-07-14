package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ComplianceService {

  private final ResourceReaderService resourceReaderService;

  public ComplianceService(ResourceReaderService resourceReaderService) {
    this.resourceReaderService = resourceReaderService;
  }


  public List<String> retrieveDisallowedPages(String domainName) {
    String robotsContent = resourceReaderService.readResource(
        removeTrailingSlashIfExists(domainName) + "/robots.txt");

    String[] disallowedEntries = robotsContent.split("Disallow:");

    List<String> disallowedPages = new ArrayList<>();
    for (int i = 1; i < disallowedEntries.length; i++) {
      disallowedPages.add(disallowedEntries[i].trim());
    }
    return disallowedPages  ;
  }


  private String removeTrailingSlashIfExists(String domainName) {
    if (domainName.endsWith("/")) {
      domainName = domainName.substring(0, domainName.length() - 1);
    }
    return domainName;
  }

}
