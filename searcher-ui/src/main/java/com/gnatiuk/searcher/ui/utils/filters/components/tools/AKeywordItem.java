package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.ImageLoader;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemFocusListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemRemovedListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sgnatiuk on 8/27/15.
 */
public abstract class AKeywordItem {

    protected KeywordItemBoxWrapper keywordItemBoxWrapper;

    protected Button removeItselfButton;

    protected List<IKeywordItemChangedListener> keywordItemChangedListeners;
    protected List<KeywordItemFocusListener> focusListeners;
    protected FocusChangeListener focusChangeListener;
    protected List<KeywordItemRemovedListener> itemRemovedListeners;

    public AKeywordItem(){
        keywordItemChangedListeners = new ArrayList<>();
        focusListeners = new ArrayList<>();
        focusChangeListener = new FocusChangeListener();
        itemRemovedListeners = new ArrayList<>();
//        keywordItemBox.focusedProperty().addListener(focusChangeListener);


    }

    private void buildKeywordNode(){


        initRemoveButton();

        keywordItemBoxWrapper.addNode(removeItselfButton);
        keywordItemBoxWrapper.addAll(getKeywordItemNodes());
    }

    private void initRemoveButton(){
        removeItselfButton = new Button("", new ImageView(
                ImageLoader.loadImageByPath(Paths.get(ImageLoader.IMAGE_RESOURCES, "delete16.png"))
            )
        );
        removeItselfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemRemovedListeners.forEach(KeywordItemRemovedListener::keywordRemoved);
            }
        });
    }

    public final Node getKeywordNode() {
        if(keywordItemBoxWrapper == null){
            keywordItemBoxWrapper = new KeywordItemBoxWrapper();
            buildKeywordNode();
        }
        return keywordItemBoxWrapper.getKeywordItemBox();
    }

    public void addKeywordItemChangedListener(IKeywordItemChangedListener keywordItemChangedListener) {
        keywordItemChangedListeners.add(keywordItemChangedListener);
    }

    public void addKeywordRemovedListener(KeywordItemRemovedListener listener){
        itemRemovedListeners.add(listener);
    }

    public void addFocusListener(KeywordItemFocusListener focusListener){
        focusListeners.add(focusListener);
    }

    public abstract String getData();
    public abstract void setData(String data);
    protected abstract List<Node> getKeywordItemNodes();

    protected class FocusChangeListener implements ChangeListener<Boolean>{

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(!focusListeners.isEmpty()){
                for (KeywordItemFocusListener focusListener : focusListeners) {
                    if(newValue){
                        focusListener.focusGained();
                    }else{
                        focusListener.focusLost();
                    }
                }
            }
        }
    }


    protected class KeywordItemBoxWrapper{
        private HBox keywordItemBox;

        public KeywordItemBoxWrapper(){
            keywordItemBox = new HBox(10);//TODO remove magic
            keywordItemBox.setMinHeight(Double.valueOf(30));//TODO do something with magic
            keywordItemBox.setAlignment(Pos.CENTER_LEFT);
            keywordItemBox.setStyle(
                    "-fx-border-style: solid;"
                            + "-fx-border-width: 1;"
                            + "-fx-border-color: gray;"
            );
        }

        private void addNode(Node node){
            keywordItemBox.getChildren().add(node);
        }

        private void addAll(Node... node){
            keywordItemBox.getChildren().addAll(node);
        }

        private void addAll(Collection<Node> nodes){
            keywordItemBox.getChildren().addAll(nodes);
        }

        public HBox getKeywordItemBox() {
            return keywordItemBox;
        }
    }
}
