package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class HtmlReaderService implements ResourceReaderService {

  private final DefaultBufferedReaderFactory bufferedReaderFactory;

  public HtmlReaderService(DefaultBufferedReaderFactory bufferedReaderFactory) {
    this.bufferedReaderFactory = bufferedReaderFactory;
  }

  public String readResource(String url) throws ResourceReadException {
    StringBuilder rawHTML = new StringBuilder();

    try (BufferedReader in = bufferedReaderFactory.createBufferedReader(new URL(url).openStream())) {
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
