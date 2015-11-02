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
        rootNode = new FilesTreeItem("Results", null);
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
            foundFile.getChildren().add(new FilesTreeItem(searchOption.getFoundOption() + ": " + searchOption.getFoundValue(), null));
        }
    }

    private Path extractChildPath(Path fullChildPath, Path fullParentPath) {
        verifyPathIsAbsolute(fullChildPath, fullParentPath);
        return fullParentPath.relativize(fullChildPath);
    }

    /**
     * @param path1 absolute path
     * @param path2 absolute path
     * @return common path for path1 and path2
     */
    private Path getCommonPath(Path path1, Path path2) {
        verifyPathIsAbsolute(path1, path2);
        if (path1.startsWith(path2)) {
            return path2;
        } else if (path2.startsWith(path1)) {
            return path1;
        } else {
            return getCommonPath(path1.getParent(), path2.getParent());
        }
    }

    private void verifyPathIsAbsolute(Path... path) {
        for (Path aPath : path) {
            if (!aPath.isAbsolute()) {
                throw new RuntimeException(String.format("A path '%s' is not absolute"));
            }
        }
    }

    /**
     * Method for adding a new node to tree.
     * @param path new file to add
     * @return a new node item
     */
    private FilesTreeItem addNewItem(Path path) {
        FilesTreeItem mostCorrespondingParent = findMostCorrespondingParentFor(path);
        FilesTreeItem newChildTreeItem;
        if (mostCorrespondingParent.getItemPath() != null) {
            Path newChildPath = extractChildPath(path, new File(buildPathForNode(mostCorrespondingParent)).toPath());
            newChildTreeItem = new FilesTreeItem(newChildPath.toString(), newChildPath);
        } else {
            newChildTreeItem = new FilesTreeItem(path.toString(), path);
        }
        mostCorrespondingParent.getChildren().add(newChildTreeItem);
        return extractFileNameToLeaf(newChildTreeItem);
    }

    private FilesTreeItem findMostCorrespondingParentFor(Path pathValue) {
        return findMostCorrespondingParentFor(pathValue, rootNode);
    }

    private FilesTreeItem findMostCorrespondingParentFor(Path pathValue, FilesTreeItem currentMostCorrespond) {

        for (TreeItem<String> child : currentMostCorrespond.getChildren()) {
            FilesTreeItem treeItemChild = (FilesTreeItem) child;

            System.err.println(treeItemChild + " 111111111");
            Path absoluteChildPath = new File(buildPathForNode(treeItemChild)).toPath();
            Path commonPath = getCommonPath(pathValue, absoluteChildPath);
            if (currentMostCorrespond == rootNode || commonPath.getNameCount() > getNodeAbsolutePath(currentMostCorrespond).getNameCount()) {
                if (commonPath.getNameCount() < absoluteChildPath.getNameCount()) {
                    extractNewChildFromTreeItem(treeItemChild, commonPath, absoluteChildPath);
                }
                currentMostCorrespond = findMostCorrespondingParentFor(pathValue, treeItemChild);
            }
        }
        return currentMostCorrespond;
    }

    private void extractNewChildFromTreeItem(FilesTreeItem itemExtractFrom, Path parentForNewChild, Path childPath) {
        verifyPathIsAbsolute(parentForNewChild, childPath);
        Path newExtractedChild = extractChildPath(childPath, parentForNewChild);

        FilesTreeItem newExtractedItem = new FilesTreeItem(newExtractedChild.toString(), newExtractedChild);
        newExtractedItem.getChildren().addAll(itemExtractFrom.getChildren());
        itemExtractFrom.getChildren().clear();
        itemExtractFrom.getChildren().add(newExtractedItem);
        itemExtractFrom.setItemPath(parentForNewChild);
        itemExtractFrom.setValue(parentForNewChild.toString());
    }

    /**
     * Method extracts treeItem to separate node as a leaf if treeItem value is a file.
     * If file is extracted nothing to do.
     *
     * @param treeItem item to divide.
     * @return new extracted item or item from argument
     */
    private FilesTreeItem extractFileNameToLeaf(FilesTreeItem treeItem) {
        String currentItemValue = treeItem.getValue();
        File itemFile = new File(buildPathForNode(treeItem));
        if (itemFile.getName().equals(currentItemValue) || itemFile.isDirectory()) {
            return treeItem;
        }

        Path itemPath = itemFile.toPath();

//        treeItem.setValue(treeItem.getItemPath().getParent().toString());
        treeItem.setValue(itemPath.getParent().toString());
        treeItem.setItemPath(itemPath.getParent());

        FilesTreeItem fileNameTreeItem = new FilesTreeItem(itemPath.getFileName().toString(), itemPath.getFileName());
        treeItem.getChildren().add(fileNameTreeItem);
        return fileNameTreeItem;
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

    private Path getNodeAbsolutePath(TreeItem<String> node) {
        return new File(buildPathForNode(node)).toPath();
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

    private void printTree() {
        printTree(rootNode, "");
    }

    private void printTree(FilesTreeItem filesTreeItem, String shift) {
        System.out.println(shift + filesTreeItem.getValue());
        for (TreeItem<String> stringTreeItem : filesTreeItem.getChildren()) {
            printTree((FilesTreeItem) stringTreeItem, "    " + shift);
        }
    }

    private class FilesTreeItem extends TreeItem<String> {

        private Path itemPath;

        public FilesTreeItem(String value, Path itemPath) {
            super(value);
            this.itemPath = itemPath;
        }

        public void setItemPath(Path itemPath) {
            this.itemPath = itemPath;
        }

        public Path getItemPath() {
            return itemPath;
        }
    }

}
