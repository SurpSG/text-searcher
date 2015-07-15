package com.gnatiuk.searcher.core.filters;

import com.gnatiuk.searcher.core.utils.FileFoundEvent;
import com.gnatiuk.searcher.core.utils.FoundOption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 7/4/2015.
 */
public abstract class ATextFilter implements IFilter {
    protected ITextPreprocessor textPreprocessor;
    protected List<String> keywords;

    public ATextFilter(List<String> keywords){
        this(keywords, ITextPreprocessor.NONE_PROCESSOR);
    }

    public ATextFilter(List<String> keywords, ITextPreprocessor textPreprocessor){
        this.textPreprocessor = textPreprocessor;
        this.keywords = processKeywords(keywords);
    }

    private List<String> processKeywords(List<String> keywords){
        List<String> preprocessedKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            preprocessedKeywords.add(textPreprocessor.process(keyword));
        }
        return preprocessedKeywords;
    }

    public void setTextPreprocessor(ITextPreprocessor textPreprocessor) {
        this.textPreprocessor = textPreprocessor;
    }
}
