package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class FileNameFilterComponent extends ASearchFilterComponent {

    protected String componentName = "File name options";

    protected JTextField fileNameFilterField;
    protected JCheckBox fileNameRegexCheck;
    protected JCheckBox fileNameIgnoreCaseCheck;

    public FileNameFilterComponent(){
        fileNameFilterField = new JTextField(".log");
        fileNameFilterField.setPreferredSize(new Dimension(200, 20));
        fileNameRegexCheck = new JCheckBox("Regex");
        fileNameRegexCheck.setSelected(true);
        fileNameIgnoreCaseCheck = new JCheckBox("Ignore case");
    }

    public JPanel getSearchCriteriaComponentsPanel(){
        List<JComponent> components = new ArrayList<>();
        components.add(fileNameFilterField);
        components.add(fileNameRegexCheck);
        components.add(fileNameIgnoreCaseCheck);
        return layoutComponents(components, componentName, BoxLayout.X_AXIS);
    }

    protected ITextPreprocessor getTextProcessor() {
        if(fileNameIgnoreCaseCheck.isSelected()){
            return ITextPreprocessor.LOWERCASE_PROCESSOR;
        }
        return ITextPreprocessor.NONE_PROCESSOR;
    }

    @Override
    public IFilter buildFilter() {
        String fileName = fileNameFilterField.getText();

        if(fileName.isEmpty()){
            return IFilter.NONE_FILTER;
        }

        ATextFilter textFilter = build(Arrays.asList(fileName));
        textFilter.setTextPreprocessor(getTextProcessor());
        return textFilter;
    }




    protected ATextFilter build(List<String> fileNames){
        if(fileNameRegexCheck.isSelected()){
            return new FilterFileNameRegex(fileNames);
        }
        return new FilterFileName(fileNames);
    }

}
