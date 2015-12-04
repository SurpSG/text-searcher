package com.gnatiuk.searcher.ui.utils;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sgnatiuk on 6/22/15.
 */
public class FileTreeItem extends TreeItem<TreeFile> {

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

    public static enum FileType{

        FILE(Paths.get(ImageLoader.IMAGE_RESOURCES, "file-icon.png")),
        DIRECTORY(Paths.get(ImageLoader.IMAGE_RESOURCES, "folder-icon.png"));

        private Image image;

        FileType(Path imagePath){
            image = ImageLoader.loadImageByPath(imagePath);
        }

        public Image getImage() {
            return image;
        }
    }
}
