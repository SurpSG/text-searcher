package com.gnatiuk.filters.internal;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.internal.FilterFileKeyword;

/**
 * Created by Батинчук on 28.10.2015.
 */
public class FilterFileKeywordTest extends AFilterFileReaderTest {

    @Override
    protected ATextFilter getTextFilter() {
        return new FilterFileKeyword(keywords);
    }
}
