package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sgnatiuk on 11/10/15.
 */
public class SimpleKeywordItem extends AKeywordItem {
    protected Label keywordLabel;

    public SimpleKeywordItem(){
        keywordLabel = new Label();
    }

    @Override
    public String getData() {
        return keywordLabel.getText();
    }

    @Override
    public void setData(String data) {
        keywordLabel.setText(data);
    }

    @Override
    protected List<Node> getKeywordItemNodes() {
        return Arrays.asList(keywordLabel);
    }
}
