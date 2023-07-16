package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.service.ComplianceService;
import com.webcrawler.webcrawler.service.ResourceReaderService;
import com.webcrawler.webcrawler.service.WebCrawlerService;
import com.webcrawler.webcrawler.web.UrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class WebCrawlerServiceTest {

  public static final String ROOT_URL = "http://example.com";
  private WebCrawlerService webCrawlerService;

  @Mock
  private ComplianceService complianceService;

  @Mock
  private ResourceReaderService resourceReader;

  @BeforeEach
  void setUp() {
    complianceService = mock(ComplianceService.class);
    resourceReader = mock(ResourceReaderService.class);
    webCrawlerService = new WebCrawlerService(complianceService, resourceReader);

    when(resourceReader.readResource(ROOT_URL)).thenReturn("<html><a href=\"http://example.com/page1\"></a></html>");
    when(resourceReader.readResource("http://example.com/page1")).thenReturn("<html><a href=\"http://example.com/page2\"></a></html>");
    when(resourceReader.readResource("http://example.com/page2")).thenReturn("");
  }

  @Test
  void should_crawl_rootUrl() {
    int limit = 10;
    List<String> disallowedPages = new ArrayList<>();
    disallowedPages.add("http://example.com/disallowed");

    when(complianceService.retrieveDisallowedPages(ROOT_URL)).thenReturn(disallowedPages);

    List<UrlResponse> result = webCrawlerService.crawl(ROOT_URL, limit);

    assertEquals(3, result.size());
    assertEquals(ROOT_URL, result.get(0).getUrl());
    assertEquals(1, result.get(0).getLinks().size());
    assertEquals("http://example.com/page1", result.get(0).getLinks().get(0));
    assertEquals("http://example.com/page1", result.get(1).getUrl());
    assertEquals(1, result.get(1).getLinks().size());
    assertEquals("http://example.com/page2", result.get(1).getLinks().get(0));

    verify(complianceService, times(1)).retrieveDisallowedPages(ROOT_URL);
    verify(resourceReader, times(1)).readResource(ROOT_URL);
    verify(resourceReader, times(1)).readResource("http://example.com/page1");
    verify(resourceReader, times(1)).readResource("http://example.com/page2");
    verifyNoMoreInteractions(resourceReader);
  }
  @Test
  void should_crawl_limited_number_of_pages() {
    int limit = 2;

    List<UrlResponse> result = webCrawlerService.crawl(ROOT_URL, limit);

    assertEquals(2, result.size());

    verify(complianceService, times(1)).retrieveDisallowedPages(ROOT_URL);
    verify(resourceReader, times(1)).readResource(ROOT_URL);
    verify(resourceReader, times(1)).readResource("http://example.com/page1");
    verify(resourceReader, times(0)).readResource("http://example.com/page2");
  }

  @Test
  void should_not_crawl_disallowed_pages() {
    String rootURL = "http://example.com";
    int limit = 10;
    List<String> disallowedPages = new ArrayList<>();
    disallowedPages.add("http://example.com/disallowed");

    when(complianceService.retrieveDisallowedPages(rootURL)).thenReturn(disallowedPages);
    when(resourceReader.readResource(rootURL)).thenReturn("<html><a href=\"http://example.com/page1\"></a></html>");
    when(resourceReader.readResource("http://example.com/page1")).thenReturn("<html><a href=\"http://example.com/page2\"></a></html>");
    when(resourceReader.readResource("http://example.com/page2")).thenReturn("<html><a href=\"http://example.com/disallowed\"></a></html>");
    when(resourceReader.readResource("http://example.com/disallowed")).thenReturn("<html></html>");

    List<UrlResponse> result = webCrawlerService.crawl(rootURL, limit);

    assertEquals(3, result.size());
    assertEquals(rootURL, result.get(0).getUrl());
    assertEquals(1, result.get(0).getLinks().size());
    assertEquals("http://example.com/page1", result.get(0).getLinks().get(0));
    assertEquals(1, result.get(1).getLinks().size());
    assertEquals("http://example.com/page1", result.get(1).getUrl());
    assertEquals("http://example.com/page2", result.get(1).getLinks().get(0));

    verify(complianceService, times(1)).retrieveDisallowedPages(rootURL);
    verify(resourceReader, times(1)).readResource(rootURL);
    verify(resourceReader, times(1)).readResource("http://example.com/page1");
    verify(resourceReader, times(1)).readResource("http://example.com/page2");
    verifyNoMoreInteractions(resourceReader);
  }
}