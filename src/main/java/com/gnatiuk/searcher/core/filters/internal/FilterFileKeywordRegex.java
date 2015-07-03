package com.gnatiuk.searcher.core.filters.internal;

import com.gnatiuk.searcher.core.filters.ITextPreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/17/15.
 */
public class FilterFileKeywordRegex extends FilterFileReader {

    private List<Pattern> keywordsPatterns;

    public FilterFileKeywordRegex(List<String> keywords) {
        this(keywords, ITextPreprocessor.LOWERCASE_PROCESSOR);
    }

    public FilterFileKeywordRegex(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
        keywordsPatterns = createPatterns(this.keywords);
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
