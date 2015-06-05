package com.gnatiuk.searcher.ui.utils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

public class FileJTreePanel extends JPanel {

    private static final Color SELC = Color.GREEN;
    private static final TreePath[] EMPTY_TREE_PATH_ARRAY = {};
    private JTree fileTree;
    private FileSystemModel fileSystemModel;

    public FileJTreePanel() {
        this(FileSystemView.getFileSystemView().getRoots()[0]);
//        System.out.println(Arrays.asList(FileSystemView.getFileSystemView().getRoots()));
//        try {
//            System.out.println(InetAddress.getLocalHost().getHostName());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    public FileJTreePanel(File directory) {
        fileSystemModel = new FileSystemModel(directory);
        fileTree = new FileJTree(fileSystemModel);
//        fileTree.setEditable(true);

        fileTree.addFocusListener(new FileTreeFocusListener());
        fileTree.setCellRenderer(new FileTreeRenderer());
        fileTree.setOpaque(false);
        add(fileTree);

    }

    public FileJTreePanel(String directory) {
        this(new File(directory));
    }

    public TreePath[] getSelectedTreePaths(){
        TreePath[] paths = fileTree.getSelectionPaths();
        if(paths != null){
            return paths;
        }
        return EMPTY_TREE_PATH_ARRAY;
    }

    public List<String> getSelectedFilePaths(){
        List<String> paths = new ArrayList<>();

        for (TreePath treePath : getSelectedTreePaths()) {
            paths.add(convertTreePathToStringPath(treePath));
        }

        return paths;
    }

    public static String convertTreePathToStringPath(TreePath treePath) {
        StringBuilder sb = new StringBuilder();
        for (Object node : treePath.getPath()) {
            sb.append(File.separatorChar).append(node.toString());
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        System.out.println(Arrays.asList(FileSystemView.getFileSystemView().getRoots()));
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println();
        new FileJTreePanel("/");
    }


    private class FileJTree extends JTree{

        public FileJTree(TreeModel treeModel) {
            super(treeModel);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (getSelectionCount() > 0) {
                g.setColor(SELC);
                for (int i : getSelectionRows()) {
                    Rectangle rowBounds = getRowBounds(i);
                    g.fillRect(rowBounds.x, rowBounds.y, (int) ((getWidth() - rowBounds.x)), rowBounds.height);
                }
            }
            super.paintComponent(g);
            if (getLeadSelectionPath() != null) {
                Rectangle r = getRowBounds(getRowForPath(getLeadSelectionPath()));
                g.setColor(hasFocus() ? SELC.darker() : SELC);
                g.drawRect(r.x, r.y, getWidth() - r.x - 1, r.height - 1);
            }
        }
    }

    private class FileTreeRenderer extends DefaultTreeCellRenderer{
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {

//                System.out.println(tree.getPathForRow(row).getLastPathComponent());
//                System.out.println();

            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, false);
            label.setBackground(selected ? SELC : tree.getBackground());
            label.setOpaque(true);
            return label;
        }
    }

    private class FileTreeFocusListener implements FocusListener{
        @Override
        public void focusGained(FocusEvent e) {
            e.getComponent().repaint();
        }


        @Override
        public void focusLost(FocusEvent e) {
            e.getComponent().repaint();
        }
    }
}
