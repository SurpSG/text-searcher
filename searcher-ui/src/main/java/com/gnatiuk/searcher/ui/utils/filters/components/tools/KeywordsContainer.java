package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemRemovedListener;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sgnatiuk on 8/12/15.
 */
public class KeywordsContainer {

    protected static final int ROW_HEIGHT = 24;
    protected static final int MAX_ROW_COUNT = 4;
    protected static final int MIN_ROW_COUNT = 1;


    protected List<AKeywordItem> aKeywordItems;

    protected VBox keywordsContainerBox;
    private TitledPane titledPane;

    public KeywordsContainer(){
        keywordsContainerBox = new VBox(10);//TODO remove magic
        titledPane = new TitledPane("Keywords",keywordsContainerBox);
        aKeywordItems = new ArrayList<>();
    }

    public Node getKeywordsContainer() {
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setFitToWidth(true);
//        scrollPane.setContent(titledPane);
//        return scrollPane;
        return keywordsContainerBox;
    }

    public List<String> getKeywords(){
        return aKeywordItems.stream().map(AKeywordItem::getData).collect(Collectors.toList());
    }

    public void addKeyword(String keyword){
        addKeywordItem(buildKeywordItem(keyword));
    }

    protected SimpleKeywordItem buildKeywordItem(String keyword){
        SimpleKeywordItem keywordItem = new SimpleKeywordItem();
        keywordItem.setData(keyword);
        keywordItem.addKeywordRemovedListener(new KeywordItemRemovedListener() {
            @Override
            public void keywordRemoved() {
                keywordsContainerBox.getChildren().remove(keywordItem.getKeywordNode());
                aKeywordItems.remove(keywordItem);
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


    protected void addKeywordItem(AKeywordItem keywordItem){
        aKeywordItems.add(keywordItem);
        keywordsContainerBox.getChildren().add(keywordItem.getKeywordNode());
    }

}
