package org.imadsnetwork.fsmgnt;

import java.util.Map;

public interface FileManagement {
    /* Dit interface geeft een "implementation guide" van welke functies er moet geïmplementeerd worden. */
    Map<String, Object> getFileMetadata(String fileName);
    void createFile(String fileName);
    void deleteFile(String fileName, boolean permanent);
}
