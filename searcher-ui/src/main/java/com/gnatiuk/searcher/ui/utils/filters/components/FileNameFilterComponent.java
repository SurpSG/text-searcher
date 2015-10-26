package com.gnatiuk.searcher.ui.utils.filters.components;


import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.external.FilterFileName;
import com.gnatiuk.searcher.filters.external.FilterFileNameRegex;

import java.util.List;

/**
 * Created by sgnatiuk on 6/24/15.
 */
public class FileNameFilterComponent extends ASearchTextFilterComponent {

    public static final String NAME = "Files with name";


    protected ATextFilter build(List<String> fileNames){
        if (regexCheck.isSelected()) {
            return new FilterFileNameRegex(fileNames);
        }
        return new FilterFileName(fileNames);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
