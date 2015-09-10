package com.gnatiuk.searcher.ui.utils.filters.components.tools;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;

/**
 * Created by sgnatiuk on 9/10/15.
 */
public class OptionedTitledPane extends TitledPane {

    private static final String OPTION_IMAGE_PATH = "src/img/option.png";

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
        return new MenuButton(null, getImageView(getImageUri(OPTION_IMAGE_PATH)));
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

    private static Node getImageView(String imageUri){
        try{
            return new ImageView(new Image(imageUri));
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return new Label("Option");
    }

    private static String getImageUri(String filePath){
        File imageFile = new File(filePath);
        if(imageFile.exists()){
            try {
                return imageFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Bad Url", e);
            }
        }else{
            throw new RuntimeException("Image does not exists. Please, check path: "+imageFile.getAbsolutePath());
        }
    }
}
