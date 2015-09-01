package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Comparator;

/**
 * Created by sgnatiuk on 8/12/15.
 */
public class KeywordsListViewWrapper {

    private static final Background ENABLED_CELL = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background DISABLED_CELL = new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY));

    private static final int ROW_HEIGHT = 24;
    private static final int MAX_ROW_COUNT = 4;
    private static final int MIN_ROW_COUNT = 1;

    private static final String ADD_KEYWORD_BUTTON_LABEL = "Add keyword";

    private ListView<IndexedData<String>> listView;
    private IndexedDataComparator comparator;

    public KeywordsListViewWrapper(){
        initListView();
    }

    private void initListView(){


        comparator = new IndexedDataComparator();

        listView = new ListView<>();
        listView.getItems().add(new IndexedData<>(ADD_KEYWORD_BUTTON_LABEL));

        listView.setCellFactory(param -> new MutableListCell());

        listView.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                maximizeListView();
            }else{
                minimizeListView();
            }
        });

        minimizeListView();
    }

    public ListView getListView() {
        return listView;
    }

    private void minimizeListView(){
        int size = listView.getItems().size();
        int listRowCount = (size >= MIN_ROW_COUNT) ? size : MIN_ROW_COUNT;
        listRowCount = (listRowCount > MAX_ROW_COUNT) ? MAX_ROW_COUNT : listRowCount;
        setVisibleRowCount(listRowCount);
    }

    private void maximizeListView(){
        setVisibleRowCount(listView.getItems().size());
    }

    private void setVisibleRowCount(int count){
        listView.setPrefHeight(count * ROW_HEIGHT + 20);
    }


    private class MutableListCell extends ListCell<IndexedData<String>> {



        private TextField textField;
        private Button addNewButton;

        private CheckBox enableKeyword;
        
        private IndexedData<String> currentValue;

        private boolean editing;

        public MutableListCell() {
            initTextField();
            initAddKeywordButton();
            setOnMouseClicked(event -> startEdit());

            enableKeyword = new CheckBox();
            enableKeyword.setSelected(true);
            enableKeyword.setOnAction(event -> updateItem(currentValue, false));
            editing = false;
        }

        private void initAddKeywordButton(){
            addNewButton = new Button(ADD_KEYWORD_BUTTON_LABEL);
            addNewButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    maximizeListView();
                    startEdit();
                }
            });
        }

        private void initTextField(){
            textField = new TextField();
            setEditable(true);

            textField.setOnKeyPressed(keyEvent -> {
                if (editing && keyEvent.getCode() == KeyCode.ENTER) {
                    cancelEdit();
                    listView.requestFocus();
                }
            });

            textField.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
                if (newPropertyValue) {
                    maximizeListView();
                }

                if(oldPropertyValue){//focus lose
                    minimizeListView();
                    if(editing){
                        cancelEdit();
                    }
                }
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (isEmpty() || !enableKeyword.isSelected()) {
                return;
            }

            editing = true;
            setText(null);
            if(currentValue.getData().equals(ADD_KEYWORD_BUTTON_LABEL)){
                textField.setText("");
            }else{
                textField.setText(currentValue.getData());
            }
            setGraphic(textField);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void updateItem(IndexedData<String> item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) return;

            currentValue = item;
            if (item.getData().equals(ADD_KEYWORD_BUTTON_LABEL)){
                setGraphic(addNewButton);
                setText(null);
            }else{
                if(enableKeyword.isSelected()){
                    setBackground(ENABLED_CELL);
                }else{
                    setBackground(DISABLED_CELL);
                }
                super.updateItem(item,false);
                setGraphic(enableKeyword);
                setText(currentValue.getData());
            }

        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            //value not changed. Just return previous cell state
            if(textField.getText().equals(currentValue.getData())){
                updateItem(currentValue, false);
                return;
            }

            if(currentValue.getData().equals(ADD_KEYWORD_BUTTON_LABEL)){
                IndexedData<String> newItem = new IndexedData<>(textField.getText());
                listView.getItems().add(newItem);
                listView.getItems().sort(comparator);
            }else{
                currentValue.setData(textField.getText());
                updateItem(currentValue,false);
            }

            editing = false;
        }

    }

    private static class IndexedDataComparator implements Comparator<IndexedData<String>>{

        @Override
        public int compare(IndexedData<String> o1, IndexedData<String> o2) {
            if(o1.getData().equals(ADD_KEYWORD_BUTTON_LABEL)){
                return 1;
            }else if(o2.getData().equals(ADD_KEYWORD_BUTTON_LABEL)){
                return -1;
            }

            return o1.getId() - o2.getId();
        }
    }



}
