package com.gnatiuk.searcher.ui.utils.dialog;

import com.gnatiuk.searcher.core.filters.IFilter;
import com.gnatiuk.searcher.utils.json.FilterJsonProcessor;
import javafx.scene.control.SelectionMode;

import java.io.File;
import java.io.IOException;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class FilterSaveDialog extends FilterFileControlDialog {

    private static final String CONTROL_BUTTON_NAME = "Save";

    private IFilter filter;

    public FilterSaveDialog(IFilter filter) {
        this.filter = filter;
        filesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    protected String getControlButtonName() {
        return CONTROL_BUTTON_NAME;
    }

    @Override
    protected void onControlButtonAction() {
        try {
            String filePath = getFilesSaveDir() + File.separator + textField.getText();
            FilterJsonProcessor.serializeFilterToFile(filePath, filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
