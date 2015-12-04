package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import com.gnatiuk.searcher.ui.utils.ImageLoader;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by sgnatiuk on 9/10/15.
 */
public class OptionedTitledPane extends TitledPane {

    private static final String OPTION_IMAGE_PATH = "option.png";

    private MenuButton optionMenuButton;


    public OptionedTitledPane(String title) {
        super(title, null);

        this.optionMenuButton = createMenuButton();
        setGraphic(optionMenuButton);
        setContentDisplay(ContentDisplay.RIGHT);

// apply css and force layout of nodes
//        applyCss();
//        layout();
//
// title region
//        Node titleRegion = lookup(".title");
// padding
//        Insets padding = ((StackPane)titleRegion).getPadding();
// image width
//        double graphicWidth=imageView.getLayoutBounds().getWidth();
// arrow
//        double arrowWidth=titleRegion.lookup(".arrow-button").getLayoutBounds().getWidth();
// text
//        double labelWidth=titleRegion.lookup(".text").getLayoutBounds().getWidth();
//
//        double nodesWidth = graphicWidth+padding.getLeft()+padding.getRight()+arrowWidth+labelWidth;

        graphicTextGapProperty().bind(widthProperty().subtract(200));
    }

    protected MenuButton createMenuButton(){
        return new MenuButton(null, new ImageView(
                    ImageLoader.loadImageByPath(Paths.get(ImageLoader.IMAGE_RESOURCES, OPTION_IMAGE_PATH))
        ));
    }

    public void setPaneContent(Node node){
        setContent(node);
    }

    public void addMenuItem(MenuItem menuItem){
        optionMenuButton.getItems().add(menuItem);
    }

    public void addMenuItems(Collection<MenuItem> items){
        optionMenuButton.getItems().addAll(items);
    }

    public void addMenuItems(MenuItem... items){
        optionMenuButton.getItems().addAll(items);
    }

    public void hideMenuItems(){
        optionMenuButton.hide();
    }
}
