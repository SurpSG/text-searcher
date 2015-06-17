package com.gnatiuk.searcher.core.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeywordRegexCaseSensitive extends FilterFileReader {

    private List<Pattern> keywordsPatterns;

    public FilterFileKeywordRegexCaseSensitive(List<String> keywordsRegex) {
        keywordsPatterns = createPatterns(keywordsRegex);
    }

    private List<Pattern> createPatterns(List<String> keywordsRegex){
        List<Pattern> patterns = new ArrayList<>();
        for (String keywordRegex : keywordsRegex) {
            patterns.add(createPattern(keywordRegex));
        }
        return patterns;
    }

    protected Pattern createPattern(String row){
        return Pattern.compile(row);
    }

    @Override
    protected boolean isLineContainsKeywords(String line) {
        for (Pattern keywordsPattern : keywordsPatterns) {
            if(keywordsPattern.matcher(line).find()){
                return true;
            }
        }
        return false;
    }
}
