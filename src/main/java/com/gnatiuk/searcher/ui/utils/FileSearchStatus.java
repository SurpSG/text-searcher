package com.gnatiuk.searcher.ui.utils;

import javafx.scene.paint.Color;

/**
 * Created by Sergiy on 6/8/2015.
 */
public enum FileSearchStatus {

    DEFAULT_COLOR(Color.SILVER),
    FILTERED_COLOR(Color.LIGHTGRAY),
    READ_FOUND_COLOR(Color.DARKGREEN),
    IN_SEARCH_COLOR(Color.YELLOW),
    READ_NOT_FOUND_COLOR(Color.NAVY);

    private Color statusColor;

    private FileSearchStatus(Color statusColor){
        this.statusColor = statusColor;
    }

    public Color getStatusColor() {
        return statusColor;
    }
}
