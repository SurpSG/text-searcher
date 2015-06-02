package com.gnatiuk.searcher.ui.utils;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

class FileSystemModel implements TreeModel {

    private static final String[] EMPTY_STRING_ARRAY = {};

    private File root;

    private Vector listeners = new Vector();

    public FileSystemModel(File rootDirectory) {
        root = rootDirectory;
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        return new TreeFile((File) parent, getChildren(parent)[index]);
    }

    public int getChildCount(Object parent) {
        return getChildren(parent).length;
    }

    private String[] getChildren(Object directory){
        File file = (File) directory;
        if (file.isDirectory()) {
            String[] sortedFiles = file.list();
            Arrays.sort(sortedFiles);
            return sortedFiles;
        }
        return EMPTY_STRING_ARRAY;
    }

    public boolean isLeaf(Object node) {
        File file = (File) node;
        return file.isFile();
    }

    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File file = (File) child;
        String[] children = directory.list();
        for (int i = 0; i < children.length; i++) {
            if (file.getName().equals(children[i])) {
                return i;
            }
        }
        return -1;

    }

    public void valueForPathChanged(TreePath path, Object value) {
    }

    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    private class TreeFile extends File {
        public TreeFile(File parent, String child) {
            super(parent, child);
        }

        public String toString() {
            return getName();
        }
    }
}