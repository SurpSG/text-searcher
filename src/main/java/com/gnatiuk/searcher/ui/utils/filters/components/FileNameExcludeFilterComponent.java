package com.gnatiuk.searcher.ui.utils.filters.components;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;
import com.gnatiuk.searcher.core.filters.external.FilterFileNameExclude;
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
        fileNameFilterField.setText("");
    }

    @Override
    protected ATextFilter build(List<String> fileNames){
        if (fileNameRegexCheck.isSelected()){
            return new FilterFileNameRegexExclude(fileNames);
        }
        return new FilterFileNameExclude(fileNames);
    }

}
