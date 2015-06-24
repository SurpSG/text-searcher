package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.AFilter;
import com.gnatiuk.searcher.core.filters.internal_file_filter.*;

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
    public AFilter buildFilter() {
        String keyword = keywordsJComboBox.getSelectedItem().toString();
        List<String> keywords = Arrays.asList(keyword);
        if(keywordRegexCheck.isSelected()){
            if(keywordIgnoreCaseCheck.isSelected()){
                return new FilterFileKeywordRegexIgnoreCase(keywords);
            }else{
                return new FilterFileKeywordRegexCaseSensitive(keywords);
            }
        }else{
            if(keywordIgnoreCaseCheck.isSelected()){
                return new FilterFileKeywordIgnoreCase(keywords);
            }else{
                return new FilterFileKeywordCaseSensitive(keywords);
            }
        }
    }

    private JComboBox createKeywordsJComboBox(){
        final JComboBox keywordsJComboBox = new JComboBox();
        keywordsJComboBox.addItem("private");
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
