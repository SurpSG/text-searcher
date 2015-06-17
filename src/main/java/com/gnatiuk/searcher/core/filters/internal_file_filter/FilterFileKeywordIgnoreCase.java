package com.gnatiuk.searcher.core.filters.internal_file_filter;

import java.util.List;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeywordIgnoreCase extends FilterFileKeywordCaseSensitive {

    public FilterFileKeywordIgnoreCase(List<String> textsToFind) {
        super(textsToFind);
    }

    protected boolean isLineContainsKeyword(String line, String keyword) {
        return line.toLowerCase().contains(keyword.toLowerCase());
    }
}
