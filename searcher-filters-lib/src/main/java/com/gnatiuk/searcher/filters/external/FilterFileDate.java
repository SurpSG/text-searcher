package com.gnatiuk.searcher.filters.external;

import com.gnatiuk.searcher.filters.CompareStatus;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;

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
        if (file.lastModified() >= fromDate.getTime() && file.lastModified() <= toDate.getTime()) {
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

    @Override
    public CompareStatus compareToFilter(IFilter filter) {
        if(this.getClass() != filter.getClass()){
            return CompareStatus.NOT_EQUALS;
        }

        FilterFileDate dateFilter = (FilterFileDate) filter;
        return this.fromDate.equals(dateFilter.fromDate) && this.toDate.equals(dateFilter.toDate)
                ? CompareStatus.EQUALS : CompareStatus.NOT_EQUALS;
    }

}
