package com.gnatiuk.searcher.ui.utils.dialog;

import com.gnatiuk.searcher.filters.IFilter;
import com.gnatiuk.searcher.utils.FilterFileUtils;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class FilterLoadDialog extends FilterFileControlDialog{

    private static final String CONTROL_BUTTON_NAME = "Load";

    private List<IFilter> filters;

    public FilterLoadDialog(){
        filesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    protected String getControlButtonName() {
        return CONTROL_BUTTON_NAME;
    }

    @Override
    protected void onControlButtonAction() {
        filters = new ArrayList<>();
        for (String selectedItem : filesListView.getSelectionModel().getSelectedItems()) {
            filters.add(FilterFileUtils.loadFilterByName(selectedItem));
        }
    }

    public List<IFilter> getFilters() {
        return filters != null ? filters : Collections.emptyList();
    }
}
