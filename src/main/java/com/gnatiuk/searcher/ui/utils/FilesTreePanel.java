package com.gnatiuk.searcher.ui.utils;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class FilesTreePanel{
    private TreeItem<TreeFile> rootNode;
    private TreeView<TreeFile> treeView;

    public FilesTreePanel() {

        initTree();

        rootNode.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler<TreeItem.TreeModificationEvent<Object>>() {
            public void handle(TreeItem.TreeModificationEvent<Object> event) {
                TreeItem expandedTreeItem = event.getTreeItem();
                initChildren(expandedTreeItem);
            }
        });

        rootNode.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler<TreeItem.TreeModificationEvent<Object>>() {
            public void handle(TreeItem.TreeModificationEvent<Object> event) {
                TreeItem collapsedTreeItem = event.getTreeItem();
                if (collapsedTreeItem != rootNode) {
                    collapsedTreeItem.getChildren().clear();
                }
            }
        });

        rootNode.setExpanded(true);
    }

    public Node getTreeNode() {
        return treeView;
    }

    private void initTree() {
        rootNode = createRootItem();
//        initChildren(rootNode);
        treeView = new TreeView<>(rootNode);
        treeView.setEditable(false);
        treeView.setCellFactory(new Callback<TreeView<TreeFile>, TreeCell<TreeFile>>() {

            @Override
            public TreeCell<TreeFile> call(TreeView<TreeFile> param) {
                return new TextFieldTreeCellImpl();
            }
        });
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initChildren(TreeItem<TreeFile> rootItem) {
        File rootFile = rootItem.getValue();
        if (rootFile.isDirectory()) {
            String[] children = rootFile.list();
            if(children == null){
                return;
            }
            ObservableList<TreeItem<TreeFile>> rootChildren = rootItem.getChildren();
            for (String child : children) {
                TreeFile childFile = createFile(child, rootFile.getAbsolutePath());
                if(childFile.isDirectory()){
                    TreeItem<TreeFile> treeItem = new FileTreeItem(childFile);
                    rootChildren.add(treeItem);
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
//                setBackground(new Background(new BackgroundFill(item.getBackgroundColorStatus().getStatusColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                setText(item.getName());
            }
        }

    }

    public List<String> getSelectedPaths(){
        List<String> selectedPaths = new ArrayList<>();
        for (TreeItem<TreeFile> treeFileTreeItem : treeView.getSelectionModel().getSelectedItems()) {
            selectedPaths.add(treeFileTreeItem.getValue().getAbsolutePath());
        }
        return selectedPaths;
    }

    private static TreeItem<TreeFile> createRootItem(){


        String computerName= null;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        TreeItem<TreeFile> root = new TreeItem<>(new TreeFile(computerName));
        for (File file : FileSystemView.getFileSystemView().getRoots()) {
            root.getChildren().add(new FileTreeItem(new TreeFile(file.getAbsolutePath())));
        }

        return root;
    }

}