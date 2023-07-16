package com.webcrawler.webcrawler.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface BufferedReaderFactory {
  BufferedReader createBufferedReader(InputStream inputStream) throws IOException;
}
