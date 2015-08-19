package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sergiy on 8/8/2015.
 */
public abstract class ASearchTextFilterComponent extends ASearchFilterComponent {

    protected ListView<String> keywords;
    protected CheckBox ignoreCaseCheck;
    protected CheckBox regexCheck;

    public ASearchTextFilterComponent() {
        ignoreCaseCheck = new CheckBox("Ignore case");
        regexCheck = new CheckBox("Regex");
        keywords = new KeywordsListViewWrapper().getListView();
    }

    protected abstract ATextFilter build(List<String> keywords);

    @Override
    protected Pane layoutComponents(List<Control> components) {
        components.add(keywords);
        components.add(ignoreCaseCheck);
        components.add(regexCheck);
        return super.layoutComponents(components);
    }

    @Override
    public Pane getSearchCriteriaComponentsPane() {
        List<Control> components = new ArrayList<>();
        return layoutComponents(components);
    }

    @Override
    public IFilter buildFilter() {

        if(keywords.getItems().isEmpty()){
            return IFilter.NONE_FILTER;
        }

        ATextFilter filter = build(keywords.getItems());
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
