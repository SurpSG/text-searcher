package com.gnatiuk.searcher.ui.utils.filters.components;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;


/**
 * Created by Sergiy on 8/8/2015.
 */
public abstract class ASearchTextFilterComponent extends ASearchFilterComponent {

    private static final int ROW_HEIGHT = 24;
    protected ListView<String> keywords;
    protected CheckBox ignoreCaseCheck;
    protected CheckBox regexCheck;

    public ASearchTextFilterComponent() {
        ignoreCaseCheck = new CheckBox("Ignore case");
        regexCheck = new CheckBox("Regex");
    }

    @Override
    protected Pane layoutComponents(List<Control> components, String title, int axis) {
        components.add(ignoreCaseCheck);
        components.add(regexCheck);
        return super.layoutComponents(components, title, axis);
    }

    @Override
    public Control getControl() {
        if (keywords == null) {
            Platform.runLater(() -> {
                keywords = new ListView<>();
                keywords.setEditable(true);
                keywords.getItems().addListener(new ListChangeListener<String>() {
                    @Override
                    public void onChanged(Change<? extends String> c) {
                        int size = keywords.getItems().size();
                        keywords.setPrefHeight(((size > 0) ? size : 1) * ROW_HEIGHT + 2);
                    }
                });
                keywords.getItems().addAll("1");
                keywords.getItems().addAll("2");
                keywords.getItems().addAll("3");
            });
        }
        return keywords;
    }
}
