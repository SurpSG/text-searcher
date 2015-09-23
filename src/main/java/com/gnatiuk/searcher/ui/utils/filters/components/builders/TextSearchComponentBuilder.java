package com.gnatiuk.searcher.ui.utils.filters.components.builders;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.ui.utils.filters.components.ASearchTextFilterComponent;


/**
 * Created by sgnatiuk on 9/23/15.
 */
public abstract class TextSearchComponentBuilder extends ASearchComponentBuilder {
    @Override
    public ASearchTextFilterComponent buildByFilter(IFilter filter) {
        ATextFilter filterFileName = (ATextFilter) filter;

        ASearchTextFilterComponent textFilterComponent = buildDefault();
        textFilterComponent.addKeywords(filterFileName.getKeywords());
        if(filterFileName.getTextPreprocessor().getClass() == ITextPreprocessor.LOWERCASE_PROCESSOR.getClass()){
            textFilterComponent.setIgnoreCase(true);
        }else if(filterFileName.getTextPreprocessor().getClass() == ITextPreprocessor.NONE_PROCESSOR.getClass()){
            textFilterComponent.setIgnoreCase(false);
        }else{
            throw new RuntimeException(String.format("Unknown text processor %s", filterFileName.getTextPreprocessor().getClass().getName()));
        }
        textFilterComponent.setRegexCheck(false);
        return textFilterComponent;
    }

    protected abstract ASearchTextFilterComponent buildDefault();
}
