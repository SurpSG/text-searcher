package com.gnatiuk.searcher.core.runnable;

import com.gnatiuk.searcher.core.runnable.SearchRunnable;

import java.io.*;
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
    protected void performSearch() {
        getFilesWithText(fileToRead);
    }

    private void getFilesWithText(File file) {

        if (isEmpty(textsToFind)) {
            return;
        }

        if (file.isFile()) {
            if (fileContainsText(file)) {
                System.out.println("\t[FOUND]: " + file.getAbsolutePath());
            }
        }
    }

    private boolean fileContainsText(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (isLineContainsKeywords(line)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
