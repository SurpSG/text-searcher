package com.gnatiuk.searcher.core.filters;

import java.io.*;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public class FilterKeyword implements IFilter {

    private List<String> textsToFind;

    public FilterKeyword(List<String> textsToFind) {
        this.textsToFind = textsToFind;
    }

    @Override
    public boolean doFilter(File file) {
        return fileContainsText(file);
    }

    private boolean fileContainsText(File file) {
        return getFirstLineContainsKeyword(file) != null;
    }

    private String getFirstLineContainsKeyword(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (isLineContainsKeywords(line)) {
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
}
