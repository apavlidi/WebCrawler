package com.webcrawler.webcrawler.service;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComplianceService {

  private final ResourceReaderService resourceReaderService;

  public ComplianceService(ResourceReaderService resourceReaderService) {
    this.resourceReaderService = resourceReaderService;
  }


  public List<String> retrieveDisallowedPages(String domainName) {
    List<String> disallowedPages = new ArrayList<>();
    try {
      String robotsContent = resourceReaderService.readResource(
          removeTrailingSlashIfExists(domainName) + "/robots.txt");

      String[] disallowedEntries = robotsContent.split("Disallow:");

      for (int i = 1; i < disallowedEntries.length; i++) {
        disallowedPages.add(disallowedEntries[i].trim());
      }
    } catch (ResourceReadException e) {
      return emptyList();
    }
    return disallowedPages;
  }


  private String removeTrailingSlashIfExists(String domainName) {
    if (domainName.endsWith("/")) {
      domainName = domainName.substring(0, domainName.length() - 1);
    }
    return domainName;
  }

}
