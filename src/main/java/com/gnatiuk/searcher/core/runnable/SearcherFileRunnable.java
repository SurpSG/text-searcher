package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;
import com.gnatiuk.searcher.core.utils.TaskStartedEvent;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergiy on 6/3/2015.
 */
public class SearcherFileRunnable extends SearchRunnable {

    private File fileToRead;

    public SearcherFileRunnable(List<String> textsToFind, File fileToRead) {
        super(textsToFind);
        this.fileToRead = fileToRead;
    }

    @Override
    protected List<String> getProcessedFiles() {
        return Arrays.asList(fileToRead.getAbsolutePath());
    }

    @Override
    protected void performSearch() {
        getFilesWithText(fileToRead);
    }

    @Override
    protected TaskStartedEvent createTaskStartedEvent() {
//        return new TaskStartedEvent(getId(), Arrays.asList(fileToRead.getAbsolutePath()));
        return new TaskStartedEvent(getId(), Arrays.asList(fileToRead.getAbsolutePath()));//TODO it seems that is should be a Path
    }

    private void getFilesWithText(File file) {

        if (isEmpty(textsToFind)) {
            return;
        }

        if (file.isFile()) {
            String line = getFirstLineContainsKeyword(file);
            if (line != null) {
                alertFileFound(file.getAbsolutePath(), line);
            }
        }
    }

    private boolean fileContainsText(File file) {
        return getFirstLineContainsKeyword(file) != null;
    }

    private String getFirstLineContainsKeyword(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (isLineContainsKeywords(line)) {
                    System.out.println("\t[FOUND]: " + file.getAbsolutePath());
                    System.out.println("\t\t\t[LINE] "+line);
                    return line;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean isLineContainsKeywords(String line) {
        for (String textToFind : textsToFind) {
            if (isLineContainsKeyword(line, textToFind)) {
                return true;
            }
        }
        return false;
    }


    protected boolean isLineContainsKeyword(String line, String keyword) {
        return line.contains(keyword);
    }

    private boolean isEmpty(List<String> list) {
        for (String element : list) {
            if (!element.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
