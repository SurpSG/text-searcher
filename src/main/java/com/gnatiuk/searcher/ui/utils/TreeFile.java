package com.gnatiuk.searcher.ui.utils;


import java.io.File;

/**
 * Created by Sergiy on 6/8/2015.
 */
public class TreeFile extends File {

    private FileSearchStatus fileSearchStatus;

    public TreeFile(String pathname) {
        super(pathname);
        fileSearchStatus = FileSearchStatus.DEFAULT_COLOR;
    }

    public FileSearchStatus getBackgroundColorStatus() {
        return fileSearchStatus;
    }

    public void setSearchStatus(FileSearchStatus fileSearchStatus) {
        this.fileSearchStatus = fileSearchStatus;
    }

    @Override
    public String toString() {
        return "TreeFile{name=" +getName()+
                ", fileSearchStatus=" + fileSearchStatus +
                '}';
    }
}
