package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import com.gnatiuk.searcher.filters.util.SearchOption;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundTreePanel{

    private FilesTreeItem rootNode;
    private TreeView<String> treeView;

    private FileOpener fileOpener;

    public FoundTreePanel(){
        rootNode = new FilesTreeItem("Results");
        rootNode.setExpanded(true);
        treeView = new TreeView<>(rootNode);

        fileOpener = new FileOpener();
        addDoubleClickListener();
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }

    public synchronized void addItem(FileSearchEvent item){

        FilesTreeItem foundFile = addNewItem(item.getFilePath());
        for (SearchOption searchOption : item.getSearchOptions()) {
            foundFile.getChildren().add(new FilesTreeItem(searchOption.getFoundOption()+": "+searchOption.getFoundValue()));
        }
    }

    /**
     * Method for adding a new node to tree.
     * @param path new file to add
     * @param node node that is currently checked to find most correspond node
     * @return a new node item
     */
    private FilesTreeItem addNewItem(String path, TreeItem<String> node){
        for (TreeItem<String> child : node.getChildren()) {
            String[] pathElements = path.split(File.separator);
            String[] childElements = child.getValue().split(File.separator);
            StringBuilder commonPath = new StringBuilder("");
            for (int i = 0; i < Math.min(pathElements.length, childElements.length); i++) {

                if(!pathElements[i].equals(childElements[i])){
                    break;
                }
                if(pathElements[i].length() > 0){
                    commonPath.append(File.separator);
                    commonPath.append(pathElements[i]);
                }

            }
            if(commonPath.length() > 1){
                String subtractedString = child.getValue().substring(child.getValue().indexOf(commonPath.toString())+commonPath.toString().length());
                String pathLeft = path.substring(path.indexOf(commonPath.toString()) + commonPath.toString().length());
                if(subtractedString.length() > 0){
                    node.getChildren().remove(child);
                    FilesTreeItem dividedNode = new FilesTreeItem(commonPath.toString());
                    FilesTreeItem oldNode = new FilesTreeItem(subtractedString);
                    oldNode.getChildren().addAll(child.getChildren());
                    FilesTreeItem nodeToAdd = new FilesTreeItem(pathLeft);
                    dividedNode.getChildren().addAll(oldNode, nodeToAdd);
                    node.getChildren().add(dividedNode);
                    return nodeToAdd;
                }
                return addNewItem(pathLeft, child);
            }
        }
        FilesTreeItem treeItemChild = new FilesTreeItem(path);
        node.getChildren().add(treeItemChild);
        return treeItemChild;
    }

    private FilesTreeItem addNewItem(Path path){
        return extractFileNameToLeaf(addNewItem(path.toString(), rootNode));
    }

    private String buildPathForNode(TreeItem<String> node){
        String path = node.getValue();
        TreeItem<String> currentParent = node.getParent();
        while (currentParent != null && currentParent != rootNode){
            path = currentParent.getValue() + File.separator + path;
            currentParent = currentParent.getParent();
        }
        return path;
    }

    public void clear(){
        rootNode.getChildren().clear();
    }

    private void addDoubleClickListener(){
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
                    if (treeItem == rootNode) {
                        return;
                    }
                    File file = new File(buildPathForNode(treeItem));

                    if (fileOpener.isSupported()) {
                        fileOpener.openFile(file);
                    }
                }
            }
        });
    }

    /**
     * Method extracts treeItem to separate node as a leaf if treeItem value is a file.
     * If file is extracted nothing to do.
     * @param treeItem item to divide.
     * @return new extracted item or item from argument
     */
    private FilesTreeItem extractFileNameToLeaf(FilesTreeItem treeItem){
        String currentItemValue = treeItem.getValue();
        File itemFile = new File(buildPathForNode(treeItem));
        if(itemFile.getName().equals(currentItemValue) || itemFile.isDirectory()){
           return treeItem;
        }

        String newItemValue = currentItemValue.substring(0, currentItemValue.indexOf(itemFile.getName()));
        treeItem.setValue(newItemValue);

        FilesTreeItem fileNameTreeItem = new FilesTreeItem(itemFile.getName());
        treeItem.getChildren().add(fileNameTreeItem);
        return fileNameTreeItem;
    }


    private class FileOpener{

        private Desktop desktop;
        private boolean supported;

        public FileOpener(){
            desktop = Desktop.getDesktop();
            supported = Desktop.isDesktopSupported();
        }

        public boolean isSupported(){
            return supported;
        }

        public void openFile(File file){

            if(!supported){
                return;
            }

            if(!file.exists()){
                throw new RuntimeException(String.format("File %s does not exist. check data that comes from listView!!!", file.getAbsolutePath()));
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        desktop.open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private class FilesTreeItem extends TreeItem<String>{

        public FilesTreeItem(String value){
            super(value);
        }

        @Override
        public boolean isLeaf(){
            File file = new File(buildPathForNode(this));
            return file.exists() && file.isFile();
        }
    }
}
