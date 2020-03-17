package com.xos.smartchat.service;

import java.io.IOException;

public interface DataImportService {
    void createIndices() throws IOException;

    void syncEs() throws IOException;
}
