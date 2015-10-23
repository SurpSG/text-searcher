package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.utils.FileSearchEvent;

import java.io.File;
import java.util.Date;

/**
 * Created by Батинчук on 23.10.2015.
 */
public class FilterFileDate implements ExternalFilterMarker, IFilter {

    private Date fromDate;
    private Date toDate;

    public FilterFileDate(Date fromDate, Date toDate){
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        if(fromDate.getTime() >= file.lastModified() && toDate.getTime() <= file.lastModified()){
            return new FileSearchEvent(file);
        }
        return FileSearchEvent.NOT_FOUND;
    }

    @Override
    public String filterHash() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(fromDate.getTime());
        stringBuilder.append(toDate.getTime());
        return stringBuilder.toString();
    }

}
