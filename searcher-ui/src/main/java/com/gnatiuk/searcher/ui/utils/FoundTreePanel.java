package com.gnatiuk.searcher.ui.utils;

import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import com.gnatiuk.searcher.filters.util.SearchOption;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sgnatiuk on 6/10/15.
 */
public class FoundTreePanel{

    private static final String FILE_ICON_PATH = "searcher-ui/src/main/resources/file-icon.png";
    private static final String FOLDER_ICON_PATH = "searcher-ui/src/main/resources/folder-icon.png";

    private static final Image fileIcon = loadImage(FILE_ICON_PATH);
    private static final Image folderIcon = loadImage(FOLDER_ICON_PATH);

    private  static Image loadImage(String imagePath){
        try {
            return new Image(new File(imagePath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TreeItem<String> rootNode;
    private TreeView<String> treeView;

    private FileOpener fileOpener;

    public FoundTreePanel(){
        rootNode = new TreeItem<>("Results");
        treeView = new TreeView<>(rootNode);

        fileOpener = new FileOpener();
        addDoubleClickListener();
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }

    public synchronized void addItem(FileSearchEvent item){
        TreeItem<String> foundFile = addNewItem(item.getFilePath());
        for (SearchOption searchOption : item.getSearchOptions()) {
            foundFile.getChildren().add(new TreeItem<>(searchOption.getFoundOption() + ": " + searchOption.getFoundValue()));
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
                throw new RuntimeException(String.format("A path '%s' is not absolute", aPath));
            }
        }
    }

    /**
     * Method for adding a new node to tree.
     * @param path new file to add
     * @return a new node item
     */
    private TreeItem<String> addNewItem(Path path) {
        TreeItem<String> mostCorrespondingParent = findMostCorrespondingParentFor(path);
        TreeItem<String> newChildTreeItem;
        if (buildPathForNode(mostCorrespondingParent) != null) {
            Path newChildPath;
            if (mostCorrespondingParent != rootNode) {
                Path mostCorrespondingParentPath = getNodeAbsolutePath(mostCorrespondingParent);
                newChildPath = extractChildPath(path, mostCorrespondingParentPath);
            } else {
                newChildPath = path;
            }
            newChildTreeItem = new TreeItem<>(newChildPath.toString(), new ImageView(fileIcon));
        } else {
            newChildTreeItem = new TreeItem<>(path.toString(), new ImageView(fileIcon));
        }
        mostCorrespondingParent.getChildren().add(newChildTreeItem);
        return extractFileNameToLeaf(newChildTreeItem);
    }

    private TreeItem<String> findMostCorrespondingParentFor(Path pathValue) {
        return findMostCorrespondingParentFor(pathValue, rootNode);
    }

    private TreeItem<String> findMostCorrespondingParentFor(Path pathValue, TreeItem<String> currentMostCorrespond) {

        for (TreeItem<String> child : currentMostCorrespond.getChildren()) {

            Path absoluteChildPath = getNodeAbsolutePath(child);
            Path commonPath = getCommonPath(pathValue, absoluteChildPath);
            if (currentMostCorrespond == rootNode
                    || commonPath.getNameCount() > getNodeAbsolutePath(currentMostCorrespond).getNameCount()) {
                if (commonPath.getNameCount() < absoluteChildPath.getNameCount()) {
                    extractNewChildFromTreeItem(child, commonPath, absoluteChildPath);
                }
                currentMostCorrespond = findMostCorrespondingParentFor(pathValue, child);
                break;
            }
        }
        return currentMostCorrespond;
    }

    private void extractNewChildFromTreeItem(TreeItem<String> itemExtractFrom, Path parentForNewChild, Path childPath) {
        verifyPathIsAbsolute(parentForNewChild, childPath);

        Path newExtractedChild = extractChildPath(childPath, parentForNewChild);

        TreeItem<String> newExtractedItem = new TreeItem<>(newExtractedChild.toString(), new ImageView(folderIcon));
        moveAllChildren(itemExtractFrom, newExtractedItem);

        String itemExtractFromValue = itemExtractFrom.getValue();
        itemExtractFrom.setValue(itemExtractFromValue.substring(0, itemExtractFromValue.lastIndexOf(File.separator + newExtractedItem.getValue())));
        itemExtractFrom.getChildren().add(newExtractedItem);
    }

    private void moveAllChildren(TreeItem<String> nodeChildrenFrom, TreeItem<String> nodeChildrenTo){
        ObservableList<TreeItem<String>> children = nodeChildrenFrom.getChildren();
        nodeChildrenFrom.getChildren().clear();
        nodeChildrenTo.getChildren().addAll(children);
    }

    /**
     * Method extracts treeItem to separate node as a leaf if treeItem value is a file.
     * If file is extracted nothing to do.
     *
     * @param treeItem item to divide.
     * @return new extracted item or item from argument
     */
    private TreeItem<String> extractFileNameToLeaf(TreeItem<String> treeItem) {
        String currentItemValue = treeItem.getValue();
        File itemFile = new File(buildPathForNode(treeItem));
        if (itemFile.getName().equals(currentItemValue) || itemFile.isDirectory()) {
            return treeItem;
        }

        treeItem.setValue(treeItem.getValue().substring(0, treeItem.getValue().lastIndexOf(itemFile.getName())));
        treeItem.setGraphic(new ImageView(folderIcon));
        TreeItem<String> fileNameTreeItem = new TreeItem<>(itemFile.getName(), new ImageView(fileIcon));
        treeItem.getChildren().add(fileNameTreeItem);
        return fileNameTreeItem;
    }

    private String buildPathForNode(TreeItem<String> node){
        if(node == null || node == rootNode){
            return "";
        }
        return buildPathForNode(node.getParent()) + File.separator + node.getValue();
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
                    System.out.println(treeItem);
                    printTree(treeItem, "");

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

    /**
     * just for debug
     */
    private void printTree() {
        printTree(rootNode, "");
    }

    private void printTree(TreeItem<String> treeItem, String shift) {
        System.out.println(shift + treeItem.getValue());
        for (TreeItem<String> stringTreeItem : treeItem.getChildren()) {
            printTree(stringTreeItem, "    " + shift);
        }
    }

    public static void main(String[] args) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FoundTreePanel foundTreePanel = new FoundTreePanel();

                List<String> files = getFiles(new File("/home/sgnatiuk/workspace/t-searcher/text-searcher"));
                Collections.shuffle(files);
                for (String s : files) {
                    foundTreePanel.addItem(new FileSearchEvent(new File(s)));
                    foundTreePanel.printTree();
                }
                System.exit(0);
            }
        });
    }

    private static java.util.List<String> getFiles(File root){
        List<String> result = new ArrayList<>();

        String[] children = root.list();
        if(children == null){
            return result;
        }
        for (String child : children) {
            File childFile = new File(root.getAbsolutePath()+ File.separator + child);
            if (childFile.isDirectory()){
                result.addAll(getFiles(childFile));
            }else{

            result.add(childFile.getAbsolutePath());
            }
        }
        return result;
    }


}
