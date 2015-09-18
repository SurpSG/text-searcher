package com.gnatiuk.searcher.ui.utils.dialog;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.json.FilterJsonProcessor;
import javafx.scene.control.SelectionMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            String filePath = getFilesSaveDir() + File.separator + selectedItem;
            try {
                filters.add(FilterJsonProcessor.deserializeFilterFromFile(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<IFilter> getFilter() {
        return filters;
    }
}
