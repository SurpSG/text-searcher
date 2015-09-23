package com.gnatiuk.searcher.core.filters;

import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 7/4/2015.
 */
public abstract class ATextFilter implements IFilter {
    @JsonProperty("textPreprocessor")
    protected ITextPreprocessor textPreprocessor;

    @JsonProperty("keywords")
    protected List<String> keywords;

    public ATextFilter(){
        this(new ArrayList<>());
    }

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

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" +
                "textPreprocessor=" + textPreprocessor +
                ", keywords=" + keywords +
                '}';
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @JsonIgnore
    public ITextPreprocessor getTextPreprocessor() {
        return textPreprocessor;
    }
}
