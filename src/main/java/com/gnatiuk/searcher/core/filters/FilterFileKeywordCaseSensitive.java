package com.gnatiuk.searcher.core.filters;

import java.util.List;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeywordCaseSensitive extends FilterFileReader {
    private List<String> textsToFind;

    public FilterFileKeywordCaseSensitive(List<String> textsToFind) {
        this.textsToFind = textsToFind;
    }

    protected boolean isLineContainsKeywords(String line) {
        for (String textToFind : textsToFind) {
            if (isLineContainsKeyword(line, textToFind)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isLineContainsKeyword(String line, String keyword) {
        return line.contains(keyword);
    }
}
