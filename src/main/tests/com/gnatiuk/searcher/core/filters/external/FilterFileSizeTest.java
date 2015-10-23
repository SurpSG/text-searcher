package com.gnatiuk.searcher.core.filters.external;

import com.gnatiuk.searcher.core.filters.AFilterTest;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.SizeMeasure;
import org.junit.Before;
import org.junit.Test;

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
    public void init(){
        super.init();
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
