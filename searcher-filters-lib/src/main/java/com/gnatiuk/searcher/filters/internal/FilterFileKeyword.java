package com.gnatiuk.searcher.filters.internal;

import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;

import java.util.List;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeyword extends FilterFileReader {


    public FilterFileKeyword() {
        super();
    }

    public FilterFileKeyword(List<String> keywords) {
        super(keywords);
    }

    public FilterFileKeyword(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    protected boolean isLineContainsKeywords(String line) {
        for (String textToFind : keywords) {
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
