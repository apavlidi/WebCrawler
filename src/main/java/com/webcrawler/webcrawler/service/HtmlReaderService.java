package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class HtmlReaderService implements ResourceReaderService {


  public String readResource(String url) {
    StringBuilder rawHTML = new StringBuilder();

    try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        rawHTML.append(inputLine);
      }
    } catch (IOException e) {
      // Handle the exception appropriately (e.g., throw a custom exception or log the error)
      // throw e;
      System.out.println(e);
    }

    return rawHTML.toString();
  }


}
