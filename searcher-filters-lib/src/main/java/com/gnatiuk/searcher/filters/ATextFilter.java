package com.gnatiuk.searcher.filters;

import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
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
        this.keywords = keywords;
        setTextPreprocessor(textPreprocessor);
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
        this.keywords = processKeywords(keywords);
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = processKeywords(keywords);
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

    @Override
    public String filterHash(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(textPreprocessor != null ? textPreprocessor.getClass().getSimpleName() : "");
        for (String keyword : keywords) {
            for (char character : keyword.toCharArray()) {
                stringBuilder.append((int)character);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public CompareStatus compareToFilter(IFilter filter) {
        if(this.getClass() != filter.getClass()){
            return CompareStatus.NOT_EQUALS;
        }

        ATextFilter textFilter = (ATextFilter) filter;
        if(textPreprocessor != textFilter.textPreprocessor){
            return CompareStatus.NOT_EQUALS;
        }

        for (String keyword : textFilter.keywords) {
            if (!keywords.contains(keyword)){
                return CompareStatus.NOT_EQUALS;
            }
        }

        return this.keywords.size() == textFilter.keywords.size() ? CompareStatus.EQUALS : CompareStatus.EXTENDED;
    }
}
