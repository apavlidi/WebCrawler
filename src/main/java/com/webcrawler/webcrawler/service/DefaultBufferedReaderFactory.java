package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.stereotype.Component;

@Component
public class DefaultBufferedReaderFactory implements BufferedReaderFactory {


  @Override
  public BufferedReader createBufferedReader(InputStream inputStream)  {
    return new BufferedReader(new InputStreamReader(inputStream));
  }
}
