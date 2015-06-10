package com.gnatiuk.searcher.core.utils;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FileFoundEvent {
    private String filePath;
    private String fileRow;

    public FileFoundEvent(String filePath, String fileRow) {
        this.filePath = filePath;
        this.fileRow = fileRow;
    }

    public String getFilePath() {
        return filePath;
    }
}
