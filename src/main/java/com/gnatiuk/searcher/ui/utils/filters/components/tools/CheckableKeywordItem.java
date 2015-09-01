package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Created by sgnatiuk on 8/27/15.
 */
public class CheckableKeywordItem extends AKeywordItem {

    private static final Background ENABLE_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background DISABLE_BACKGROUND = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));

    private CheckBox enable;
    private Label keywordLabel;


    public CheckableKeywordItem() {
        this.enable = new CheckBox();
        enable.setSelected(true);
        enable.setOnAction(event -> {
            if(enable.isSelected()){
                keywordItemBox.setBackground(ENABLE_BACKGROUND);
            }else{
                keywordItemBox.setBackground(DISABLE_BACKGROUND);

            }
        });
        keywordLabel = new Label();
        keywordItemBox.getChildren().add(enable);
        keywordItemBox.getChildren().add(keywordLabel);
        initListeners();

        enable.focusedProperty().addListener(focusChangeListener);
        keywordLabel.focusedProperty().addListener(focusChangeListener);
    }

    @Override
    protected void startEdit() {
        if(enable.isSelected()){
            super.startEdit();
        }
    }

    public void setData(String data){
        keywordLabel.setText(data);
    }

    @Override
    protected Node getKeywordItemNode() {
        return keywordLabel;
    }

    @Override
    public String getData() {
        return keywordLabel.getText();
    }
}
