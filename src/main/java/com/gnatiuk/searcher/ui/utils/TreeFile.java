package com.gnatiuk.searcher.ui.utils;


import java.io.File;

/**
 * Created by Sergiy on 6/8/2015.
 */
public class TreeFile extends File {

    private FileSearchStatusColored fileSearchStatus;

    public TreeFile(String pathname) {
        super(pathname);
        fileSearchStatus = FileSearchStatusColored.DEFAULT_COLOR;
    }

    public FileSearchStatusColored getBackgroundColorStatus() {
        return fileSearchStatus;
    }

    public void setSearchStatus(FileSearchStatusColored fileSearchStatus) {
        this.fileSearchStatus = fileSearchStatus;
    }

    @Override
    public String toString() {
        return "TreeFile{name=" +getName()+
                ", fileSearchStatus=" + fileSearchStatus +
                '}';
    }
}
