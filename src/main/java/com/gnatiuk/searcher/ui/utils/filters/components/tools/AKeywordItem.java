package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemFocusListener;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.KeywordItemRemovedListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 8/27/15.
 */
public abstract class AKeywordItem {

    protected TextField textField;
    protected HBox keywordItemBox;
    protected Button removeItselfButton;
    protected List<IKeywordItemChangedListener> keywordItemChangedListeners;
    protected List<KeywordItemFocusListener> focusListeners;
    protected FocusChangeListener focusChangeListener;
    protected List<KeywordItemRemovedListener> itemRemovedListeners;

    boolean editNow = false;

    public AKeywordItem(){
        keywordItemChangedListeners = new ArrayList<>();
        focusListeners = new ArrayList<>();
        focusChangeListener = new FocusChangeListener();
        itemRemovedListeners = new ArrayList<>();

        removeItselfButton = new Button("-");
        removeItselfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemRemovedListeners.forEach(KeywordItemRemovedListener::keywordRemoved);
            }
        });

        keywordItemBox = new HBox(10);//TODO remove magic
        keywordItemBox.setStyle(
                "-fx-border-style: solid;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-color: gray;"
        );
        keywordItemBox.setAlignment(Pos.CENTER_LEFT);
        keywordItemBox.setMinHeight(Double.valueOf(30));//TODO do something with magic
        keywordItemBox.getChildren().add(removeItselfButton);
        textField = new TextField();
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
        keywordItemBox.focusedProperty().addListener(focusChangeListener);
        textField.focusedProperty().addListener(focusChangeListener);
    }


    public Node getKeywordItem() {
        return keywordItemBox;
    }

    protected void initListeners(){
        Node keywordItemNode = getKeywordItemNode();
        keywordItemNode.setOnMouseClicked(
                event -> {
                    if (!editNow) {
                        startEdit();
                    }
                }
        );

        textField.setOnKeyPressed(
                event -> {
                    if (editNow && event.getCode() == KeyCode.ENTER){
                        cancelEdit();
                    }
                }
        );

        keywordItemNode.setOnKeyPressed(
                event -> {
                    if (!editNow && event.getCode() == KeyCode.ENTER) {
                        startEdit();
                    }
                }
        );
    }

    protected void startEdit(){
        editNow = true;
        ObservableList<Node> nodes = keywordItemBox.getChildren();
        nodes.remove(getKeywordItemNode());
        textField.setText(getData());
        nodes.add(textField);
        textField.requestFocus();
        textField.selectAll();
    }


    protected void cancelEdit(){
        if(!editNow){
            return;
        }
        ObservableList<Node> nodes = keywordItemBox.getChildren();
        int index = nodes.indexOf(textField);
        nodes.remove(textField);
        setData(textField.getText());
        nodes.add(index, getKeywordItemNode());
        editNow = false;
        keywordItemChangedListeners.forEach(IKeywordItemChangedListener::valueChanged);
        textField.requestFocus();
    }

    public List<IKeywordItemChangedListener> getKeywordItemChangedListener() {
        return keywordItemChangedListeners;
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
    protected abstract Node getKeywordItemNode();

    private class FocusChangeListener implements ChangeListener<Boolean>{

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
}
