package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.internal.FilterFileKeyword;
import com.gnatiuk.searcher.filters.internal.FilterFileKeywordRegex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class KeywordFilterComponent extends ASearchTextFilterComponent {

    public static final String NAME = "File contains keyword";

    @Override
    public String getName() {
        return NAME;
    }

    protected ATextFilter build(List<String> keywords){
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
