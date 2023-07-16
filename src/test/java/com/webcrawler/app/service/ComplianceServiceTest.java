package com.webcrawler.app.service;

import com.webcrawler.app.exception.ResourceReadException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ComplianceServiceTest {

  public static final String DOMAIN_NAME = "https://example.com";
  public static final String DOMAIN_NAME_WITH_TRAILING_SLASH = "https://example.com/";
  private ComplianceService complianceService;
  private ResourceReaderService resourceReaderService;

  @BeforeEach
  void setUp() {
    resourceReaderService = mock(ResourceReaderService.class);
    complianceService = new ComplianceService(resourceReaderService);
  }

  @Test
  void should_retrieve_disallowed_pages() throws ResourceReadException {
    String robotsContent = "# robotstxt.org/User-agent: *\n" +
        "Disallow: /docs/\n" +
        "Disallow: /referral/\n" +
        "Disallow: /-staging-referral/\n" +
        "Disallow: /install/\n" +
        "Disallow: /blog/authors/\n" +
        "Disallow: /-deeplinks/";

    when(resourceReaderService.readResource(DOMAIN_NAME + "/robots.txt")).thenReturn(robotsContent);

    List<String> disallowedPages = complianceService.retrieveDisallowedPages(DOMAIN_NAME);

    List<String> expectedDisallowedPages = Arrays.asList(
        "/docs/",
        "/referral/",
        "/-staging-referral/",
        "/install/",
        "/blog/authors/",
        "/-deeplinks/"
    );
    assertEquals(expectedDisallowedPages, disallowedPages);
  }

  @Test
  void should_retrieve_disallowed_pages_if_url_has_a_trailing_slash() throws ResourceReadException {
    String robotsContent = "# robotstxt.org/User-agent: *\n" +
        "Disallow: /docs/\n" +
        "Disallow: /referral/\n" +
        "Disallow: /-staging-referral/\n" +
        "Disallow: /install/\n" +
        "Disallow: /blog/authors/\n" +
        "Disallow: /-deeplinks/";

    when(resourceReaderService.readResource(DOMAIN_NAME + "/robots.txt")).thenReturn(robotsContent);

    List<String> disallowedPages = complianceService.retrieveDisallowedPages(DOMAIN_NAME_WITH_TRAILING_SLASH);

    List<String> expectedDisallowedPages = Arrays.asList(
        "/docs/",
        "/referral/",
        "/-staging-referral/",
        "/install/",
        "/blog/authors/",
        "/-deeplinks/"
    );
    assertEquals(expectedDisallowedPages, disallowedPages);
  }

  @Test
  void should_return_empty_list_if_robots_file_is_empty() throws ResourceReadException {
    String robotsContent = "";

    when(resourceReaderService.readResource(DOMAIN_NAME + "/robots.txt")).thenReturn(robotsContent);

    List<String> disallowedPages = complianceService.retrieveDisallowedPages(DOMAIN_NAME);

    assertEquals(0, disallowedPages.size());
  }

  @Test
  void should_return_empty_list_if_robots_file_has_now_disallowed_pages()
      throws ResourceReadException {
    String robotsContent = "# robotstxt.org/User-agent: *";

    when(resourceReaderService.readResource(DOMAIN_NAME + "/robots.txt")).thenReturn(robotsContent);

    List<String> disallowedPages = complianceService.retrieveDisallowedPages(DOMAIN_NAME);

    assertEquals(0, disallowedPages.size());
  }

  @Test
  public void should_return_empty_list_if_reading_resource_throws_an_exception() throws ResourceReadException {
    when(resourceReaderService.readResource(anyString())).thenThrow(new ResourceReadException("An error occurred",any()));

    List<String> disallowedPages = complianceService.retrieveDisallowedPages(DOMAIN_NAME);

    assertEquals(Collections.emptyList(), disallowedPages);
  }

}
