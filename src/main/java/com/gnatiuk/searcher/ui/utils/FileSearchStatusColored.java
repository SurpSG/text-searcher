package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.core.utils.FileSearchStatus;
import javafx.scene.paint.Color;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public enum FileSearchStatusColored {

    DEFAULT_COLOR(FileSearchStatus.NOT_PROCESSED, Color.SILVER),
    PROCESSED_FOUND_COLOR(FileSearchStatus.PROCESSED_FOUND, Color.DARKGREEN),
    PROCESSED_NOT_FOUND_COLOR(FileSearchStatus.PROCESSED_NOT_FOUND, Color.RED),
    IN_PROGRESS_COLOR(FileSearchStatus.IN_PROGRESS, Color.YELLOW);

    private FileSearchStatus fileSearchStatus;
    private Color statusColor;

    FileSearchStatusColored(FileSearchStatus fileSearchStatus, Color statusColor){
        this.fileSearchStatus = fileSearchStatus;
        this.statusColor = statusColor;
    }

    public Color getStatusColor() {
        return statusColor;
    }


    public static FileSearchStatusColored determineFileSearchStatusColored(FileSearchStatus fileSearchStatus){
        for (FileSearchStatusColored fileSearchStatusColored : values()) {
            if(fileSearchStatusColored.fileSearchStatus == fileSearchStatus){
                return fileSearchStatusColored;
            }
        }
        throw new IllegalArgumentException(String.format("FileSearchStatus '%s' is not determined in current enum set",
                fileSearchStatus));
    }
}
