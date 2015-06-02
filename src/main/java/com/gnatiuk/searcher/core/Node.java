package com.gnatiuk.searcher.core;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/28/15
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class Node implements Iterable {

    private File file;
    private Node rootNode;
    private List<Node> children = new ArrayList<>();

    public Node(File file) {
        this.file = file;
        rootNode = null;
        createTree(file, this);
    }

    private Node(Node rootNode) {
        this.rootNode = rootNode;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private void createTree(File root, Node rootNode) {
        if (root.isDirectory()) {
            String[] list = root.list();
            if(list == null){
                return;
            }
            for (String s : list) {
                File childFile = new File(root.getAbsolutePath() + "\\" + s);
                Node childNode = new Node(rootNode);
                childNode.setFile(childFile);
                rootNode.children.add(childNode);
                createTree(childFile, childNode);
            }
        }
    }

    public void printTree() {
        printTree("");
    }

    private void printTree(String row) {
        System.out.println(row + file.getName());
        for (Node child : children) {
            child.printTree("\t" + row);
        }
    }


    public List<Node> getNodesInWidthOrder() {
        return getNodesInWidthOrder(Arrays.asList(this));
    }

    public List<Node> getNodesInWidthOrder(List<Node> nodes) {
        if (nodes.size() == 0) {
            return Collections.emptyList();
        }
        List<Node> childNodes = new ArrayList<>();
        List<Node> result = new ArrayList<>();
        for (Node child : nodes) {
            childNodes.addAll(child.children);
            result.add(child);
        }
        result.addAll(getNodesInWidthOrder(childNodes));
        return result;
    }

    public void widthIter() {
        System.out.println(file);
        widthIter(children);
    }


    private void widthIter(List<Node> nodes) {
        if (nodes.size() == 0) {
            return;
        }
        List<Node> childNodes = new ArrayList<>();
        for (Node child : nodes) {
            System.out.println(child.file);
            childNodes.addAll(child.children);
        }
        widthIter(childNodes);
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\segn1014\\";
        File root = new File(path);
        Node rootNode = new Node(root);
        long time = System.currentTimeMillis();
        for (Object objNode : rootNode) {
//            System.out.println(objNode);
        }
        System.out.println("time1: = "+(System.currentTimeMillis()-time));
    }

    @Override
    public String toString() {
        return "Node{" +
                "file=" + file +
                '}';
    }

    @Override
    public Iterator iterator() {
        return new TreeWidthIterator();
    }

    private class TreeDepthIterator implements Iterator{

        private List<Node> iteratedNodes;
        private Node currentNode;

        private TreeDepthIterator() {
            iteratedNodes = new ArrayList<>();
            currentNode = Node.this;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }

    private class TreeWidthIterator implements Iterator{

        private List<Node> nodes;

        private int index;

        private TreeWidthIterator() {
            this.nodes = Arrays.asList(Node.this);
            index = 0;
        }

        private List<Node> getAllChildrenForNodes(List<Node> nodes) {
            List<Node> childNodes = new ArrayList<>();
            for (Node child : nodes) {
                childNodes.addAll(child.children);
            }
            return childNodes;
        }

        @Override
        public boolean hasNext() {
            return nodes.size() != 0 && index < nodes.size();
        }

        @Override
        public Node next() {
            Node node = nodes.get(index);
            index++;
            if(!hasNext()){
                nodes = getAllChildrenForNodes(nodes);
                index = 0;
            }
            return node;
        }

        @Override
        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
