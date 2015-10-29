package com.gnatiuk.filters.internal;

import com.gnatiuk.filters.AFilterTest;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.internal.FilterFileKeyword;

/**
 * Created by Батинчук on 28.10.2015.
 */
public class FilterFileKeywordTest extends AFilterTest {
    @Override
    protected IFilter getFilter() {
        return new FilterFileKeyword();
    }
}
