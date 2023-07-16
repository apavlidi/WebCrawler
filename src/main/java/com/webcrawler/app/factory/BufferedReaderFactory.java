package com.webcrawler.app.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface BufferedReaderFactory {
  BufferedReader createBufferedReader(InputStream inputStream) throws IOException;
}
