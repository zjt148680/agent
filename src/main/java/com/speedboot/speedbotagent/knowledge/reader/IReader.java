package com.speedboot.speedbotagent.knowledge.reader;

import java.io.InputStream;

public interface IReader {
    String read(String url);

    String read(InputStream inputStream);
}