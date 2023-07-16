package com.webcrawler.app.exception;

public class CrawlException extends RuntimeException {

  public CrawlException(String message, Throwable cause) {
    super(message, cause);
  }
}
