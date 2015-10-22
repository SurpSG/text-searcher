package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;

import java.io.File;

/**
 * Created by Батинчук on 22.10.2015.
 */
public class FilterFileSize implements ExternalFilterMarker, IFilter {

    private int lowInterval;
    private int upInterval;

    public FilterFileSize(int lowInterval, int upInterval) {
        this.lowInterval = lowInterval;
        this.upInterval = upInterval;
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        if (file.length() < lowInterval && file.length() > upInterval) {
            return new FileSearchEvent(file);
        }
        return FileSearchEvent.NOT_FOUND;
    }

    @Override
    public String filterHash() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(lowInterval);
        stringBuilder.append(upInterval);
        return stringBuilder.toString();
    }

}
