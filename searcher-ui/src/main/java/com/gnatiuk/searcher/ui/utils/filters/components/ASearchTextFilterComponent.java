package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.EditableKeywordsContainer;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import utils.KeywordsLibManager;

import java.util.List;


/**
 * Created by Sergiy on 8/8/2015.
 */
public abstract class ASearchTextFilterComponent extends ASearchFilterComponent {

    protected EditableKeywordsContainer keywordsContainer;
    protected CheckBox ignoreCaseCheck;
    protected CheckBox regexCheck;

    public ASearchTextFilterComponent() {
        ignoreCaseCheck = new CheckBox("Ignore case");
        ignoreCaseCheck.setSelected(true);
        regexCheck = new CheckBox("Regex");
        keywordsContainer = new EditableKeywordsContainer();
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
    public IFilter buildFilter() {

        if(keywordsContainer.getKeywords().isEmpty()){
            return IFilter.NONE_FILTER;
        }

        KeywordsLibManager.getInstance().saveWords(keywordsContainer.getKeywords());

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

    public void addKeyword(String keyword) {
        this.keywordsContainer.addKeyword(keyword);
    }

    public void addKeywords(List<String> keywords) {
        for (String keyword : keywords) {
            addKeyword(keyword);
        }
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCaseCheck.setSelected(ignoreCase);
    }

    public void setRegexCheck(boolean regex) {
        this.regexCheck.setSelected(regex);
    }

}
