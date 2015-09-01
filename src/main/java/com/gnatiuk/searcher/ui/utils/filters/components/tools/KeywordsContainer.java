package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemFocusListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 8/12/15.
 */
public class KeywordsContainer {

    private static final int ROW_HEIGHT = 24;
    private static final int MAX_ROW_COUNT = 4;
    private static final int MIN_ROW_COUNT = 1;


    private List<String> keywords;

    private Button addKeywordButton;
    private VBox keywordsContainerBox;
    private TitledPane titledPane;

    public KeywordsContainer(){
        keywordsContainerBox = new VBox(10);//TODO remove magic
        keywordsContainerBox.setStyle(
                "-fx-border-style: solid;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-color: black;"
        );
        titledPane = new TitledPane("Keywords",keywordsContainerBox);
        keywords = new ArrayList<>();

        addKeywordButton = new Button("Add keyword");
        addKeywordButton.setMaxWidth(Double.MAX_VALUE);
        addKeywordButton.setOnAction(
                event -> {
                    int buttonIndex = keywordsContainerBox.getChildren().indexOf(addKeywordButton);
                    CheckableKeywordItem keywordItem = new CheckableKeywordItem();
                    keywordItem.addFocusListener(new KeywordItemFocusListener() {
                        @Override
                        public void focusGained() {
                            maximizeListView();
                        }

                        @Override
                        public void focusLost() {
                            minimizeListView();
                            keywordItem.cancelEdit();
                        }
                    });
                    keywordItem.addKeywordItemChangedListener(new IKeywordItemChangedListener() {
                        @Override
                        public void valueChanged() {
                            if (keywordItem.getData().isEmpty()) {
                                keywordsContainerBox.getChildren().remove(keywordItem.getKeywordItem());
                            }
                        }
                    });
                    keywordsContainerBox.getChildren().add(buttonIndex, keywordItem.getKeywordItem());
                    keywordItem.startEdit();
                }
        );


        keywordsContainerBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    maximizeListView();
                } else {
                    minimizeListView();
                }
            }
        });

        keywordsContainerBox.getChildren().add(addKeywordButton);
    }

    public Node getKeywordsContainer() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(titledPane);
        return scrollPane;
    }

    public List<String> getKeywords(){
        return keywords;
    }

    private void minimizeListView(){
        int size = keywordsContainerBox.getChildren().size();
        int listRowCount = (size >= MIN_ROW_COUNT) ? size : MIN_ROW_COUNT;
        listRowCount = (listRowCount > MAX_ROW_COUNT) ? MAX_ROW_COUNT : listRowCount;
//        setVisibleRowCount(listRowCount);
    }

    private void maximizeListView(){
//        setVisibleRowCount(keywordsContainerBox.getChildren().size());
//        System.out.println();
//        setVisibleRowCount(2);
    }

    private void setVisibleRowCount(int count){
//        titledPane.setPrefHeight(ROW_HEIGHT * count);
    }

}
