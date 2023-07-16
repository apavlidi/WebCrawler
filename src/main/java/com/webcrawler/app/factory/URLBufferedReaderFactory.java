package com.webcrawler.app.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class URLBufferedReaderFactory implements BufferedReaderFactory {

  @Override
  public BufferedReader createBufferedReader(String url) throws IOException {
    return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
  }
}
