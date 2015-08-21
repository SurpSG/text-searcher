package com.gnatiuk.searcher.ui.utils.filters.components;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * Created by sgnatiuk on 8/12/15.
 */
public class KeywordsListViewWrapper {

    private static final int ROW_HEIGHT = 24;
    private static final int MAX_ROW_COUNT = 4;
    private static final int MIN_ROW_COUNT = 1;

    private static final String EMPTY_ROW = " ";

    private ListView<String> listView;

    public KeywordsListViewWrapper(){
        listView = new ListView<>();
        listView.getItems().add(EMPTY_ROW);

        listView.setCellFactory(param -> new MutableListCell<>());

        resizeListView();
    }

    public ListView<String> getListView() {
        return listView;
    }

    private void resizeListView(){
        int size = listView.getItems().size();
        int listRowCount = (size >= MIN_ROW_COUNT) ? size : MIN_ROW_COUNT;
        listRowCount = (listRowCount > MAX_ROW_COUNT) ? MAX_ROW_COUNT : listRowCount;

        listView.setPrefHeight(listRowCount * ROW_HEIGHT + 10);
    }


    private class MutableListCell<T> extends ListCell<T> {

        private TextField textField;
        private String currentValue;

        private boolean editing;

        public MutableListCell() {
            initTextField();
            editing = false;
            setOnMouseClicked(event -> startEdit());
        }

        private void initTextField(){
            textField = new TextField();
            setEditable(true);

            textField.setOnKeyPressed(keyEvent -> {
                if (editing && keyEvent.getCode() == KeyCode.ENTER) {
                    cancelEdit();
                }
            });

            textField.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
                if (!newPropertyValue) {
                    cancelEdit();
                }
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();
            editing = true;

            if (isEmpty()) {
                return;
            }

            setText(null);
            currentValue = textField.getText();
            setGraphic(textField);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) return;

            setGraphic(null);
            setText(currentValue);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setGraphic(null);

            if(textField.getText().isEmpty()){
                return;
            }

            currentValue = textField.getText();

            listView.getItems().remove(EMPTY_ROW);
            listView.getItems().remove(currentValue);
            listView.getItems().add(currentValue);
            listView.getItems().add(EMPTY_ROW);

            resizeListView();
            editing = false;
        }
    }
}
