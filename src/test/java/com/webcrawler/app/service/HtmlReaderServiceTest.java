package com.webcrawler.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.webcrawler.app.exception.ResourceReadException;
import com.webcrawler.app.factory.URLBufferedReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HtmlReaderServiceTest {

  public static final String URL = "https://example.com";

  @Mock
  private URLBufferedReaderFactory bufferedReaderFactoryMock;

  @Mock
  private BufferedReader bufferedReaderMock;

  private HtmlReaderService htmlReaderService;

  @BeforeEach
  void setUp() throws IOException {
    when(bufferedReaderFactoryMock.createBufferedReader(any()))
        .thenReturn(bufferedReaderMock);
    htmlReaderService = new HtmlReaderService(bufferedReaderFactoryMock);
  }

  @Test
  void should_return_html_of_a_given_url() throws IOException, ResourceReadException {
    String expectedHtml = "<html><body>Hello, world!</body></html>";

    when(bufferedReaderMock.readLine())
        .thenReturn(expectedHtml)
        .thenReturn(null);

    assertEquals(expectedHtml, htmlReaderService.readResource(URL));
  }

  @Test
  void should_throw_exception_if_io_exception_while_reading_lines() throws IOException {
    when(bufferedReaderMock.readLine())
        .thenThrow(new IOException("Failed to read line"));

    assertThrows(ResourceReadException.class, () -> htmlReaderService.readResource(URL));
  }
}
