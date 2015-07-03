package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegex;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameRegexExclude;

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
    public IFilter buildFilter() {
        String fileName = fileNameFilterField.getText();
        List<String> fileNames = Arrays.asList(fileName);
        FilterFileNameRegexExclude fileNameRegexExclude = new FilterFileNameRegexExclude(fileNames);
        if(fileNameIgnoreCaseCheck.isSelected()){
            fileNameRegexExclude.setTextPreprocessor(ITextPreprocessor.LOWERCASE_PROCESSOR);
        }
        return fileNameRegexExclude;
    }

}
