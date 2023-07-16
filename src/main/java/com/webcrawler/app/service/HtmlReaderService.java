package com.webcrawler.app.service;

import com.webcrawler.app.exception.ResourceReadException;
import com.webcrawler.app.factory.URLBufferedReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class HtmlReaderService implements ResourceReaderService {

  private final URLBufferedReaderFactory bufferedReaderFactory;

  public HtmlReaderService(URLBufferedReaderFactory bufferedReaderFactory) {
    this.bufferedReaderFactory = bufferedReaderFactory;
  }

  public String readResource(String url) throws ResourceReadException {
    StringBuilder rawHTML = new StringBuilder();

    try (BufferedReader in = bufferedReaderFactory.createBufferedReader(url)) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        rawHTML.append(inputLine);
      }
    } catch (IOException e) {
      throw new ResourceReadException("Failed to read HTML resource: " + url, e);
    }

    return rawHTML.toString();
  }


}
