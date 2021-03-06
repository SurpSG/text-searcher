package com.gnatiuk.filters.external;

import com.gnatiuk.filters.AFilterTest;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.external.FilterFileSize;
import org.junit.Before;
import org.junit.Test;
import utils.SizeMeasure;

import static org.junit.Assert.assertTrue;
/**
 * Created by sgnatiuk on 10/23/15.
 */
public class FilterFileSizeTest extends AFilterTest {


    private int upperInterval;
    private int lowerInterval;
    private SizeMeasure sizeMeasure;

    private FilterFileSize filterFileSize;

    @Before
    public void initFilterFileSizeTest(){
        lowerInterval = 0;
        upperInterval = 1;
        sizeMeasure = SizeMeasure.KB;
        filterFileSize = new FilterFileSize(lowerInterval, upperInterval, sizeMeasure);
    }

    @Override
    protected IFilter getFilter() {
        return filterFileSize;
    }

    @Test
    public void upperIntervalTest() {
        assertTrue(filterFileSize.doFilter(testFile).getFilePath().toFile().length() <= sizeMeasure.convertToBytes(upperInterval));
    }

    @Test
    public void lowerIntervalTest() {
        assertTrue(filterFileSize.doFilter(testFile).getFilePath().toFile().length() >= sizeMeasure.convertToBytes(lowerInterval));
    }

}
