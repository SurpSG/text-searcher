package com.gnatiuk.searcher.ui.utils;


import java.io.File;

/**
 * Created by Sergiy on 6/8/2015.
 */
public class TreeFile extends File {


    public TreeFile(String pathname) {
        super(pathname);
    }

    @Override
    public String toString() {
        return "TreeFile{name=" +getName()+
                '}';
    }
}
