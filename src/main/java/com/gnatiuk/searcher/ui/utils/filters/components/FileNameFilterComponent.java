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
        fileNameFilterField = new JTextField(".*");
        fileNameFilterField.setPreferredSize(new Dimension(200, 20));
        fileNameRegexCheck = new JCheckBox("Regex");
        fileNameIgnoreCaseCheck = new JCheckBox("Ignore case");
    }

    public JPanel getSearchCriteriaComponentsPanel(){
        List<JComponent> components = new ArrayList<>();
        components.add(fileNameFilterField);
        components.add(fileNameRegexCheck);
        components.add(fileNameIgnoreCaseCheck);
        return layoutComponents(components, componentName, BoxLayout.X_AXIS);
    }

    @Override
    public IFilter buildFilter() {
        String fileName = fileNameFilterField.getText();
        List<String> fileNames = Arrays.asList(fileName);
        ATextFilter textFilter;
        if(fileNameRegexCheck.isSelected()){
            textFilter = new FilterFileNameRegex(fileNames);
        }else{
            textFilter = new FilterFileName(fileNames);
        }

        if(fileNameIgnoreCaseCheck.isSelected()){
            textFilter.setTextPreprocessor(ITextPreprocessor.LOWERCASE_PROCESSOR);
        }
        return textFilter;
    }

}
