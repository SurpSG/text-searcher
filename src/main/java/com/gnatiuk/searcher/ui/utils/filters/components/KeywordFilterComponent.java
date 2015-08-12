package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.core.filters.internal.FilterFileKeywordRegex;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class KeywordFilterComponent extends ASearchTextFilterComponent {

    public static final String NAME = "File contains keyword";

    private ComboBox<String> keywordsComboBox;

    public KeywordFilterComponent(){
        keywordsComboBox = new ComboBox<>();
    }

    public Pane getSearchCriteriaComponentsPane() {
        List<javafx.scene.control.Control> components = new ArrayList<>();
        components.add(keywordsComboBox);
        return layoutComponents(components, "Keywords", BoxLayout.X_AXIS);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * TODO change filter creation strategy!!!
     * @return
     */
    @Override
    public IFilter buildFilter() {
        String keyword = keywordsComboBox.getSelectionModel().getSelectedItem().toString();

        if(keyword.isEmpty()){
            return IFilter.NONE_FILTER;
        }

        List<String> keywords = Arrays.asList(keyword);
        ATextFilter filter = build(keywords);
        filter.setTextPreprocessor(getTextProcessor());
        return filter;
    }

    protected ITextPreprocessor getTextProcessor() {
        if (ignoreCaseCheck.isSelected()) {
            return ITextPreprocessor.LOWERCASE_PROCESSOR;
        }
        return ITextPreprocessor.NONE_PROCESSOR;
    }

    private ATextFilter build(List<String> keywords){
        if (regexCheck.isSelected()) {
            return new FilterFileKeywordRegex(keywords);
        }
        return new FilterFileKeyword(keywords);
    }

    private JComboBox createKeywordsJComboBox(){
        final JComboBox keywordsJComboBox = new JComboBox();
        keywordsJComboBox.addItem("last message repeated");
        keywordsJComboBox.setEditable(true);
        keywordsJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    keywordsJComboBox.addItem(keywordsJComboBox.getSelectedItem().toString());
                }
            }
        });
        return keywordsJComboBox;
    }
}
