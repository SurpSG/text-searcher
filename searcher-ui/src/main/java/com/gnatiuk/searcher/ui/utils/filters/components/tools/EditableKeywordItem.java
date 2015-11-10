package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.AutoCompleteTextField;
import com.gnatiuk.searcher.ui.utils.filters.components.tools.listeners.IKeywordItemChangedListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import utils.KeywordsLibManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 8/27/15.
 */
public class EditableKeywordItem extends SimpleKeywordItem {

    private static final Background ENABLE_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background DISABLE_BACKGROUND = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));

    private CheckBox enable;


    protected TextField textField;

    private boolean editNow = false;

    public EditableKeywordItem() {

        textField = new AutoCompleteTextField(KeywordsLibManager.getInstance().getKeywords());

        this.enable = new CheckBox();
        enable.setSelected(true);
        enable.setOnAction(event -> {
            Background backgroundToSet = enable.isSelected() ? ENABLE_BACKGROUND : DISABLE_BACKGROUND;
            keywordItemBoxWrapper.getKeywordItemBox().setBackground(backgroundToSet);
        });

        initListeners();

        enable.focusedProperty().addListener(focusChangeListener);
        keywordLabel.focusedProperty().addListener(focusChangeListener);
        textField.focusedProperty().addListener(focusChangeListener);
    }

    @Override
    protected List<Node> getKeywordItemNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(enable);
        nodes.addAll(super.getKeywordItemNodes());
        return nodes;
    }


    protected void initListeners(){
        Node keywordItemNode = keywordLabel;
        keywordItemNode.setOnMouseClicked(
                event -> {
                    if (!editNow) {
                        startEdit();
                    }
                }
        );

        textField.setOnKeyPressed(
                event -> {
                    if (editNow && event.getCode() == KeyCode.ENTER) {
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
        if(enable.isSelected()){
            editNow = true;
            ObservableList<Node> nodes = keywordItemBoxWrapper.getKeywordItemBox().getChildren();
            nodes.remove(keywordLabel);
            nodes.add(textField);
            textField.setText(getData());
            textField.requestFocus();
            textField.selectAll();
        }
    }


    protected void cancelEdit(){
        if(!editNow){
            return;
        }
        ObservableList<Node> nodes = keywordItemBoxWrapper.getKeywordItemBox().getChildren();
        int index = nodes.indexOf(textField);
        nodes.remove(textField);
        setData(textField.getText());
        nodes.add(index, keywordLabel);
        editNow = false;
        keywordItemChangedListeners.forEach(IKeywordItemChangedListener::valueChanged);
        textField.requestFocus();
    }

}
