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

import java.util.ArrayList;

/**
 * Created by sgnatiuk on 11/10/15.
 */
public class EditableKeywordsContainer extends KeywordsContainer {

    private Button addKeywordButton;
    private TitledPane titledPane;

    public EditableKeywordsContainer(){
        aKeywordItems = new ArrayList<>();

        addKeywordButton = new Button("Add keyword");
        addKeywordButton.setMaxWidth(Double.valueOf(300));
        addKeywordButton.setOnAction(
                event -> {
                    EditableKeywordItem keywordItem = buildKeywordItem("");
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

        titledPane = new TitledPane("Keywords",keywordsContainerBox);

        keywordsContainerBox.getChildren().add(addKeywordButton);
    }

    public Node getKeywordsContainer() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(titledPane);
        return scrollPane;
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

    protected EditableKeywordItem buildKeywordItem(String keyword){
        EditableKeywordItem keywordItem = new EditableKeywordItem();
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

    @Override
    protected void addKeywordItem(AKeywordItem keywordItem){
        int buttonIndex = keywordsContainerBox.getChildren().indexOf(addKeywordButton);
        aKeywordItems.add(keywordItem);
        keywordsContainerBox.getChildren().add(buttonIndex, keywordItem.getKeywordNode());
    }


}
