package com.gnatiuk.searcher.core.utils;

import com.gnatiuk.searcher.ui.utils.FileSearchStatus;

import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class TaskCompleteEvent {

    private List<String> processedFiles;

    private FileSearchStatus statusColor;

    public TaskCompleteEvent(List<String> processedFiles, FileSearchStatus statusColor) {
        this.processedFiles = processedFiles;
        this.statusColor = statusColor;
    }

    public List<String> getProcessedFiles() {
        return processedFiles;
    }

    public FileSearchStatus getStatusColor() {
        return statusColor;
    }
}
