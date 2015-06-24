package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.AFilter;
import com.gnatiuk.searcher.core.filters.external_file_filter.FilterFileName;
import com.gnatiuk.searcher.core.filters.external_file_filter.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external_file_filter.FilterFileNameRegexExclude;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class FileNameExcludeFilterComponent extends FileNameFilterComponent {


    public FileNameExcludeFilterComponent(){
        super();
        componentName = "File name exclude options";
    }

    @Override
    public AFilter buildFilter() {
        String fileName = fileNameFilterField.getText();
        List<String> fileNames = Arrays.asList(fileName);
        if(fileNameRegexCheck.isSelected()){
            return new FilterFileNameRegexExclude(fileNames);
        }else{
            return new FilterFileNameRegex(fileNames);
        }
    }

}
