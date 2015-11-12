package com.gnatiuk.searcher.ui.utils;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by sgnatiuk on 6/22/15.
 */
public class FileTreeItem extends TreeItem<TreeFile> {

    private static final String FILE_ICON_PATH = "searcher-ui/src/main/resources/file-icon.png";
    private static final String FOLDER_ICON_PATH = "searcher-ui/src/main/resources/folder-icon.png";

    public static final Image FILE_ICON = loadImage(FILE_ICON_PATH);
    public static final Image FOLDER_ICON = loadImage(FOLDER_ICON_PATH);

    public FileTreeItem(TreeFile treeFile) {
        super(treeFile);
    }

    public FileTreeItem(TreeFile treeFile, Node graphics) {
        super(treeFile, graphics);
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }




    private  static Image loadImage(String imagePath){
        try {
            return new Image(new File(imagePath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
