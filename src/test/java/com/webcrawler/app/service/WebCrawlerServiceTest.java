package com.webcrawler.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcrawler.app.exception.ResourceReadException;
import com.webcrawler.app.web.UrlResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


public class WebCrawlerServiceTest {

  public static final String ROOT_URL = "http://example.com";
  public static final int LIMIT = 10;
  private WebCrawlerService webCrawlerService;

  @Mock
  private ComplianceService complianceService;

  @Mock
  private ResourceReaderService resourceReader;

  @BeforeEach
  void setUp() throws ResourceReadException {
    complianceService = mock(ComplianceService.class);
    resourceReader = mock(ResourceReaderService.class);
    webCrawlerService = new WebCrawlerService(complianceService, resourceReader);

    when(resourceReader.readResource(ROOT_URL)).thenReturn("<html><a href=\"http://example.com/page1\"></a></html>");
    when(resourceReader.readResource("http://example.com/page1")).thenReturn("<html><a href=\"http://example.com/page2\"></a></html>");
    when(resourceReader.readResource("http://example.com/page2")).thenReturn("");
  }

  @Test
  void should_crawl_rootUrl() throws ResourceReadException {
    List<String> disallowedPages = new ArrayList<>();
    disallowedPages.add("http://example.com/disallowed");

    when(complianceService.retrieveDisallowedPages(ROOT_URL)).thenReturn(disallowedPages);

    List<UrlResponse> result = webCrawlerService.crawl(ROOT_URL, LIMIT);

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
  }
  @Test
  void should_crawl_limited_number_of_pages() throws ResourceReadException {
    int limit = 2;

    List<UrlResponse> result = webCrawlerService.crawl(ROOT_URL, limit);

    assertEquals(2, result.size());

    verify(complianceService, times(1)).retrieveDisallowedPages(ROOT_URL);
    verify(resourceReader, times(1)).readResource(ROOT_URL);
    verify(resourceReader, times(1)).readResource("http://example.com/page1");
    verify(resourceReader, times(0)).readResource("http://example.com/page2");
  }

  @Test
  void should_not_crawl_disallowed_pages() throws ResourceReadException {
    List<String> disallowedPages = new ArrayList<>();
    disallowedPages.add("http://example.com/disallowed");

    when(complianceService.retrieveDisallowedPages(ROOT_URL)).thenReturn(disallowedPages);
    when(resourceReader.readResource(ROOT_URL)).thenReturn("<html><a href=\"http://example.com/page1\"></a></html>");
    when(resourceReader.readResource("http://example.com/page1")).thenReturn("<html><a href=\"http://example.com/page2\"></a></html>");
    when(resourceReader.readResource("http://example.com/page2")).thenReturn("<html><a href=\"http://example.com/disallowed\"></a></html>");
    when(resourceReader.readResource("http://example.com/disallowed")).thenReturn("<html></html>");

    List<UrlResponse> result = webCrawlerService.crawl(ROOT_URL, LIMIT);

    assertEquals(3, result.size());
    assertEquals(ROOT_URL, result.get(0).getUrl());
    assertEquals(1, result.get(0).getLinks().size());
    assertEquals("http://example.com/page1", result.get(0).getLinks().get(0));
    assertEquals(1, result.get(1).getLinks().size());
    assertEquals("http://example.com/page1", result.get(1).getUrl());
    assertEquals("http://example.com/page2", result.get(1).getLinks().get(0));

    verify(complianceService, times(1)).retrieveDisallowedPages(ROOT_URL);
    verify(resourceReader, times(1)).readResource(ROOT_URL);
    verify(resourceReader, times(1)).readResource("http://example.com/page1");
    verify(resourceReader, times(1)).readResource("http://example.com/page2");
  }

  @Test
  void crawl_ShouldThrowRuntimeException_WhenResourceReadExceptionOccurs() throws ResourceReadException {
    when(complianceService.retrieveDisallowedPages(ROOT_URL)).thenReturn(new ArrayList<>());
    when(resourceReader.readResource(anyString())).thenThrow(new ResourceReadException("An error occured",any()));

    assertThrows(RuntimeException.class, () -> webCrawlerService.crawl(ROOT_URL, LIMIT));
  }
}