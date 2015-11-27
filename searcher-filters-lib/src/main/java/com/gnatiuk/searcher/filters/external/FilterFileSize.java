package com.gnatiuk.searcher.filters.external;

import com.gnatiuk.searcher.filters.CompareStatus;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import utils.SizeMeasure;

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

    @Override
    public CompareStatus compareToFilter(IFilter filter) {
        if(filter == null || this.getClass() != filter.getClass()){
            return CompareStatus.NOT_EQUALS;
        }

        FilterFileSize sizeFilter = (FilterFileSize) filter;
        return (this.lowInterval == sizeFilter.lowInterval
                && this.upInterval == sizeFilter.upInterval)
                ? CompareStatus.EQUALS : CompareStatus.NOT_EQUALS;
    }
}
