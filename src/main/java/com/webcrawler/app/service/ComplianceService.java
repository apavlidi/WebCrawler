package com.webcrawler.app.service;

import static java.util.Collections.emptyList;

import com.webcrawler.app.exception.ResourceReadException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ComplianceService {

  public static final String ROBOTS_TXT = "/robots.txt";
  private final ResourceReaderService resourceReaderService;

  public ComplianceService(ResourceReaderService resourceReaderService) {
    this.resourceReaderService = resourceReaderService;
  }


  public List<String> retrieveDisallowedPages(String domainName) {
    try {
      String robotsContent = resourceReaderService.readResource(
          removeTrailingSlashIfExists(domainName) + ROBOTS_TXT);

      String[] disallowedEntries = robotsContent.split("Disallow:");

      return Arrays.stream(disallowedEntries)
          .skip(1)
          .map(String::trim)
          .collect(Collectors.toList());

    } catch (ResourceReadException e) {
      return emptyList();
    }
  }


  private String removeTrailingSlashIfExists(String domainName) {
    if (domainName.endsWith("/")) {
      domainName = domainName.substring(0, domainName.length() - 1);
    }
    return domainName;
  }

}
