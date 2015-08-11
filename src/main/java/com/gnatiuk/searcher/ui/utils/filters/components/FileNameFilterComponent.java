package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileName;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class FileNameFilterComponent extends ASearchTextFilterComponent {

    public static final String NAME = "Files with name";

    protected String componentName = "File name options";

    protected TextField fileNameFilterField;

    public FileNameFilterComponent(){
        fileNameFilterField = new TextField(".log");
        fileNameFilterField.setPrefSize(200, 20);
    }

    public Pane getSearchCriteriaComponentsPane() {
        List<Control> components = new ArrayList<>();
        components.add(fileNameFilterField);
        return layoutComponents(components, componentName, BoxLayout.X_AXIS);
    }

    protected ITextPreprocessor getTextProcessor() {
        if (ignoreCaseCheck.isSelected()) {
            return ITextPreprocessor.LOWERCASE_PROCESSOR;
        }
        return ITextPreprocessor.NONE_PROCESSOR;
    }

    @Override
    public String getName() {
        return NAME;
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
        if (regexCheck.isSelected()) {
            return new FilterFileNameRegex(fileNames);
        }
        return new FilterFileName(fileNames);
    }

}
