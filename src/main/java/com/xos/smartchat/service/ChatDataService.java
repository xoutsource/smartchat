package com.xos.smartchat.service;

import java.io.IOException;
import java.util.List;

public interface ChatDataService {
    List<String> getPrompts(String question) throws IOException;

    String getAnswer(String question) throws IOException;
}
