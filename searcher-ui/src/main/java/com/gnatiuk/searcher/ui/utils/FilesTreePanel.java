package com.gnatiuk.searcher.ui.utils;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilesTreePanel{
    private TreeItem<TreeFile> rootNode;
    private SelectedTreeView treeView;

    private SelectedListView recentChosenPaths;

    private SplitPane contentRootNode;
    private TabPane tabPaneNode;
    private SelectedListView chosenPaths;


    public FilesTreePanel() {
        initContentRootNode();
    }

    public Node getTreeNode() {
        return contentRootNode;
    }

    private void initContentRootNode() {
        contentRootNode = new SplitPane();
        contentRootNode.setOrientation(Orientation.VERTICAL);
        initTabbedNode();
        initChosenPathsNode();
        contentRootNode.getItems().addAll(
                tabPaneNode,
                chosenPaths
        );
    }

    private void initTabbedNode() {
        initFilesTreeNode();
        initRecentChosenNode();
        tabPaneNode = new TabPane();
        Tab filesTreeTab = new Tab("Files tree");
        filesTreeTab.setContent(treeView);
        Tab recentChosenTab = new Tab("Recent");
        recentChosenTab.setContent(recentChosenPaths);
        tabPaneNode.getTabs().addAll(filesTreeTab, recentChosenTab);
    }

    private void initChosenPathsNode() {
        chosenPaths = new SelectedListView();
    }

    private void initRecentChosenNode() {
        recentChosenPaths = new SelectedListView();
        recentChosenPaths.getItems().addAll(OnBootManager.getRecentPaths());
        recentChosenPaths.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        recentChosenPaths.setOnKeyPressed(new AddPathKeyEventHandler(recentChosenPaths));
    }

    private void initFilesTreeNode() {
        initRootItem();
        treeView = new SelectedTreeView(rootNode);
        treeView.setEditable(false);
        treeView.setCellFactory(new Callback<TreeView<TreeFile>, TreeCell<TreeFile>>() {
            @Override
            public TreeCell<TreeFile> call(TreeView<TreeFile> param) {
                return new TextFieldTreeCellImpl();
            }
        });
        treeView.setOnKeyPressed(new AddPathKeyEventHandler(treeView));
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
            rootChildren.sort(new Comparator<TreeItem<TreeFile>>() {
                @Override
                public int compare(TreeItem<TreeFile> o1, TreeItem<TreeFile> o2) {
                    return o1.getValue().getAbsolutePath().compareTo(o2.getValue().getAbsolutePath());
                }
            });
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
                setText(item.getName());
                setGraphic(new ImageView(FileTreeItem.folderIcon));
            }
        }

    }

    public List<String> getSelectedPaths(){
        return chosenPaths.getItems();
    }

    private TreeItem<TreeFile> initRootItem(){

        rootNode = new TreeItem<>(new TreeFile(getRootItemName()));
        for (File file : FileSystemView.getFileSystemView().getRoots()) {
            rootNode.getChildren().add(new FileTreeItem(new TreeFile(file.getAbsolutePath())));
        }

        rootNode.addEventHandler(TreeItem.branchExpandedEvent(), event -> {
            TreeItem expandedTreeItem = event.getTreeItem();
            initChildren(expandedTreeItem);
        });

        rootNode.addEventHandler(TreeItem.branchCollapsedEvent(), event -> {
            TreeItem collapsedTreeItem = event.getTreeItem();
            if (collapsedTreeItem != rootNode) {
                collapsedTreeItem.getChildren().clear();
            }
        });

        rootNode.setExpanded(true);
        return rootNode;
    }

    private static String getRootItemName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot choose name for root item");
    }

    private class AddPathKeyEventHandler implements EventHandler<KeyEvent>{

        ItemsView itemsView;

        public AddPathKeyEventHandler(ItemsView itemsView) {
            this.itemsView = itemsView;
        }

        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.ADD){
                ObservableList<String> items = FilesTreePanel.this.chosenPaths.getItems();
                for (String selectedItem : itemsView.getSelectedItems()) {
                    if(!items.contains(selectedItem)){
                        items.add(selectedItem);
                    }
                }
                items.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
            }
        }
    }

    private interface ItemsView {
        List<String> getSelectedItems();
    }

    private class SelectedListView extends ListView<String> implements ItemsView {

        @Override
        public List<String> getSelectedItems() {
            return getSelectionModel().getSelectedItems();
        }


    }

    private class SelectedTreeView extends TreeView<TreeFile> implements ItemsView {

        public SelectedTreeView(TreeItem<TreeFile> root){
            super(root);
        }

        @Override
        public List<String> getSelectedItems() {
            return getSelectionModel().getSelectedItems()
                    .stream()
                    .map(treeFileTreeItem -> treeFileTreeItem.getValue().getAbsolutePath())
                    .collect(Collectors.toList());
        }
    }
}