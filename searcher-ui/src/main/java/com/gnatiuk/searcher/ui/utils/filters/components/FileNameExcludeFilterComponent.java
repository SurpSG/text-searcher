package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.external.FilterFileNameExclude;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegexExclude;

import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class FileNameExcludeFilterComponent extends FileNameFilterComponent {

    public static final String NAME = "Exclude files with name";

    @Override
    protected ATextFilter build(List<String> fileNames){
        if (regexCheck.isSelected()) {
            return new FilterFileNameRegexExclude(fileNames);
        }
        return new FilterFileNameExclude(fileNames);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
