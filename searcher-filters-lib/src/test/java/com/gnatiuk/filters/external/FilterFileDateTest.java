package com.gnatiuk.filters.external;

import com.gnatiuk.filters.AFilterTest;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.external.FilterFileDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Батинчук on 23.10.2015.
 */
public class FilterFileDateTest extends AFilterTest {

    private Date fromDate;
    private Date toDate;

    private FilterFileDate filterFileDate;

    @Before
    public void init() {
        super.init();
        fromDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        toDate = new Date(System.currentTimeMillis());
        filterFileDate = new FilterFileDate(fromDate, toDate);
    }

    @Override
    protected IFilter getFilter() {
        return filterFileDate;
    }

    @Test
    public void upperIntervalTest() {
        assertTrue(filterFileDate.doFilter(testFile).getFilePath().toFile().lastModified() <= toDate.getTime());
    }

    @Test
    public void lowerIntervalTest() {
        assertTrue(filterFileDate.doFilter(testFile).getFilePath().toFile().lastModified() >= fromDate.getTime());
    }
}
