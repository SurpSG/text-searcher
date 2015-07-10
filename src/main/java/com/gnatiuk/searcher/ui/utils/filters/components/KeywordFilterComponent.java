package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.internal.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class KeywordFilterComponent extends ASearchFilterComponent {

    private JComboBox<String> keywordsJComboBox;
    private JCheckBox keywordRegexCheck;
    private JCheckBox keywordIgnoreCaseCheck;

    public KeywordFilterComponent(){
        keywordsJComboBox = createKeywordsJComboBox();
        keywordRegexCheck = new JCheckBox("Regex");
        keywordRegexCheck.setSelected(true);
        keywordIgnoreCaseCheck = new JCheckBox("Ignore case");
    }

    public JPanel getSearchCriteriaComponentsPanel(){
        List<JComponent> components = new ArrayList<>();
        components.add(keywordsJComboBox);
        components.add(keywordRegexCheck);
        components.add(keywordIgnoreCaseCheck);
        return layoutComponents(components, "Keywords", BoxLayout.X_AXIS);
    }

    /**
     * TODO change filter creation strategy!!!
     * @return
     */
    @Override
    public IFilter buildFilter() {
        String keyword = keywordsJComboBox.getSelectedItem().toString();

        if(keyword.isEmpty()){
            return IFilter.NONE_FILTER;
        }

        List<String> keywords = Arrays.asList(keyword);
        ATextFilter filter = build(keywords);

        if(keywordIgnoreCaseCheck.isSelected()){
            filter.setTextPreprocessor(ITextPreprocessor.LOWERCASE_PROCESSOR);
        }
        return filter;
    }

    private ATextFilter build(List<String> keywords){
        if(keywordRegexCheck.isSelected()){
            return new FilterFileKeywordRegex(keywords);
        }
        return new FilterFileKeyword(keywords);
    }

    private JComboBox createKeywordsJComboBox(){
        final JComboBox keywordsJComboBox = new JComboBox();
        keywordsJComboBox.addItem("starting");
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
