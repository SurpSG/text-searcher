package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;
import com.gnatiuk.searcher.utils.SizeMeasure;

import java.io.File;

/**
 * Created by Батинчук on 22.10.2015.
 */
public class FilterFileSize implements ExternalFilterMarker, IFilter {

    private long lowInterval;
    private long upInterval;

    public FilterFileSize(int lowInterval, int upInterval, SizeMeasure sizeMeasure) {
        this.lowInterval = sizeMeasure.convertToBytes(lowInterval);
        this.upInterval = sizeMeasure.convertToBytes(upInterval);
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        if (file.length() >= lowInterval && file.length() <= upInterval) {
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

    @Override
    public String toString() {
        return "FilterFileSize{" +
                "lowInterval=" + lowInterval + " bytes" +
                ", upInterval=" + upInterval + " bytes" +
                '}';
    }
}
