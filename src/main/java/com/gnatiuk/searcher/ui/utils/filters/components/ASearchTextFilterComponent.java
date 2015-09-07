package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.KeywordsContainer;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sergiy on 8/8/2015.
 */
public abstract class ASearchTextFilterComponent extends ASearchFilterComponent {

    protected KeywordsContainer keywordsContainer;
    protected CheckBox ignoreCaseCheck;
    protected CheckBox regexCheck;

    public ASearchTextFilterComponent() {
        ignoreCaseCheck = new CheckBox("Ignore case");
        ignoreCaseCheck.setSelected(true);
        regexCheck = new CheckBox("Regex");
        keywordsContainer = new KeywordsContainer();
    }

    protected abstract ATextFilter build(List<String> keywords);

    @Override
    protected Node layoutComponents(List<Node> components) {
        components.add(keywordsContainer.getKeywordsContainer());
        components.add(ignoreCaseCheck);
        components.add(regexCheck);
        return super.layoutComponents(components);
    }

    @Override
    public Node getSearchCriteriaComponentsPane() {
        List<Node> components = new ArrayList<>();
        return layoutComponents(components);
    }

    @Override
    public IFilter buildFilter() {

        if(keywordsContainer.getKeywords().isEmpty()){
            return IFilter.NONE_FILTER;
        }

        ATextFilter filter = build(keywordsContainer.getKeywords());
        filter.setTextPreprocessor(getTextProcessor());
        return filter;
    }

    protected ITextPreprocessor getTextProcessor() {
        if (ignoreCaseCheck.isSelected()) {
            return ITextPreprocessor.LOWERCASE_PROCESSOR;
        }
        return ITextPreprocessor.NONE_PROCESSOR;
    }
}
