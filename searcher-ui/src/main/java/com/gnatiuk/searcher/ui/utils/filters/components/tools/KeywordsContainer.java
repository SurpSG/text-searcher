package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemFocusListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemRemovedListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sgnatiuk on 8/12/15.
 */
public class KeywordsContainer {

    private static final int ROW_HEIGHT = 24;
    private static final int MAX_ROW_COUNT = 4;
    private static final int MIN_ROW_COUNT = 1;


    private List<AKeywordItem> aKeywordItems;

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
        aKeywordItems = new ArrayList<>();

        addKeywordButton = new Button("Add keyword");
        addKeywordButton.setMaxWidth(Double.valueOf(300));
        addKeywordButton.setOnAction(
                event -> {
                    AKeywordItem keywordItem = buildKeywordItem("");
                    addKeywordItem(keywordItem);
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
        return aKeywordItems.stream().map(AKeywordItem::getData).collect(Collectors.toList());
    }

    public void addKeyword(String keyword){
        addKeywordItem(buildKeywordItem(keyword));
    }

    private void minimizeListView(){
        int size = keywordsContainerBox.getChildren().size();
        int listRowCount = (size >= MIN_ROW_COUNT) ? size : MIN_ROW_COUNT;
        listRowCount = (listRowCount > MAX_ROW_COUNT) ? MAX_ROW_COUNT : listRowCount;
//        setVisibleRowCount(listRowCount);
    }

    private void maximizeListView(){
//        setVisibleRowCount(keywordsContainerBox.getChildren().size());
//        setVisibleRowCount(2);
    }

    private void setVisibleRowCount(int count){
//        titledPane.setPrefHeight(ROW_HEIGHT * count);
    }

    private AKeywordItem buildKeywordItem(String keyword){
        CheckableKeywordItem keywordItem = new CheckableKeywordItem();
        keywordItem.setData(keyword);
        keywordItem.addKeywordRemovedListener(new KeywordItemRemovedListener() {
            @Override
            public void keywordRemoved() {
                keywordsContainerBox.getChildren().remove(keywordItem.getKeywordNode());
                aKeywordItems.remove(keywordItem);
            }
        });
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
                    keywordsContainerBox.getChildren().remove(keywordItem.getKeywordNode());
                }
            }
        });
        return keywordItem;
    }

    private void addKeywordItem(AKeywordItem keywordItem){
        int buttonIndex = keywordsContainerBox.getChildren().indexOf(addKeywordButton);
        aKeywordItems.add(keywordItem);
        keywordsContainerBox.getChildren().add(buttonIndex, keywordItem.getKeywordNode());
    }

}
