package com.gnatiuk.searcher.ui.utils;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JFXFilesTreePanel extends JFXPanel {
    private TreeItem<TreeFile> rootNode;
    private TreeView<TreeFile> treeView;

    public JFXFilesTreePanel(String rootDir) {

        initTree(new TreeFile(rootDir));
        rootNode.setExpanded(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                StackPane root = new StackPane();
                root.getChildren().add(treeView);
                Scene scene = new Scene(root, 400, 400);
                setScene(scene);
            }
        });

    }

    private void initTree(TreeFile root) {
        rootNode = new TreeItem<>(root);
        initChildren(rootNode);
        treeView = new TreeView<>(rootNode);
        treeView.setEditable(false);
        treeView.setCellFactory(new Callback<TreeView<TreeFile>, TreeCell<TreeFile>>() {

            @Override
            public TreeCell<TreeFile> call(TreeView<TreeFile> param) {
                return new TextFieldTreeCellImpl();
            }
        });
    }

    private void initChildren(TreeItem<TreeFile> rootItem) {
        File rootFile = rootItem.getValue();
        if (rootFile.isDirectory()) {
            String[] children = rootFile.list();
            ObservableList<TreeItem<TreeFile>> rootChildren = rootItem.getChildren();
            for (String child : children) {
                TreeFile childFile = createFile(child, rootFile.getAbsolutePath());
                if(childFile.isDirectory()){
                    TreeItem<TreeFile> treeItem = new TreeItem<>(childFile);
                    rootChildren.add(treeItem);
                    initChildren(treeItem);
                }
            }
        }
    }

    private TreeFile createFile(String fileName, String rootAbsolutePath) {
        StringBuilder stringBuilder = new StringBuilder(rootAbsolutePath);
        stringBuilder.append("/")
                .append(fileName);
        return new TreeFile(stringBuilder.toString());
    }

    class TextFieldTreeCellImpl extends TreeCell<TreeFile> {

        @Override
        public void updateItem(TreeFile item, boolean empty) {
            super.updateItem(item, empty);

            if(item != null){
                setBackground(new Background(new BackgroundFill(item.getBackgroundColorStatus().getStatusColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                setText(item.getName());
            }
        }

    }

    public void setNodeStatusByPath(String filePath, FileSearchStatus searchStatus){
        setNodeStatusByPath(rootNode, Paths.get(filePath), searchStatus);
    }

    public boolean setNodeStatusByPath(TreeItem<TreeFile> currentRootNode, Path filePath, FileSearchStatus searchStatus){

        TreeFile currentRootNodeFile = currentRootNode.getValue();
        Path currentRootNodePath = currentRootNodeFile.toPath();
        if(currentRootNodePath.equals(filePath)){
            currentRootNodeFile.setSearchStatus(searchStatus);
            if(!currentRootNode.isLeaf() && currentRootNode.getValue().isDirectory() && !currentRootNode.isExpanded()){
                System.out.println(currentRootNodeFile);
                currentRootNode.setExpanded(true);
            }
            currentRootNode.setValue(null);
            currentRootNode.setValue(currentRootNodeFile);
            return true;
        }else if(filePath.startsWith(currentRootNodePath)){
            for (TreeItem<TreeFile> treeFileTreeItem : currentRootNode.getChildren()) {
                if(setNodeStatusByPath(treeFileTreeItem, filePath, searchStatus)){
                    return true;
                }
            }
        }
        return false;
    }
}