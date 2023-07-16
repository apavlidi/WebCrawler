package com.webcrawler.app.factory;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderFactory {
  BufferedReader createBufferedReader(String url) throws IOException;
}
