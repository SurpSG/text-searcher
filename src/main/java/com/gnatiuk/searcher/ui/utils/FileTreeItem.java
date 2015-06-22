package com.gnatiuk.searcher.ui.utils;

import javafx.scene.control.TreeItem;

/**
 * Created by sgnatiuk on 6/22/15.
 */
public class FileTreeItem extends TreeItem<TreeFile> {

    public FileTreeItem(TreeFile treeFile) {
        super(treeFile);
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }
}
