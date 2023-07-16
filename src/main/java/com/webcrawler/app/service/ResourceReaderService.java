package com.webcrawler.app.service;

import com.webcrawler.app.exception.ResourceReadException;

public interface ResourceReaderService {

  String readResource(String input) throws ResourceReadException;

}
