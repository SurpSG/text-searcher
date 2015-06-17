package com.gnatiuk.searcher.core.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeywordRegexIgnoreCase extends FilterFileKeywordRegexCaseSensitive {


    public FilterFileKeywordRegexIgnoreCase(List<String> keywordsRegex) {
        super(keywordsRegex);
    }

    protected Pattern createPattern(String row){
        return super.createPattern(row.toLowerCase());
    }

    @Override
    protected boolean isLineContainsKeywords(String line) {
        return super.isLineContainsKeywords(line.toLowerCase());
    }
}
