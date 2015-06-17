package com.gnatiuk.searcher.core.filters.internal_file_filter;

import com.gnatiuk.searcher.core.filters.IFilter;

import java.io.*;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public abstract class FilterFileReader implements IFilter {

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

            int n = 0;
            while ((line = reader.readLine()) != null) {
                n++;
                if (isLineContainsKeywords(line)) {
                    System.out.println("\t\t[LINE]: "+n+") "+line);
                    return line;
                }

            }
        } catch (FileNotFoundException e) {
//            System.err.println(e.getMessage());
        } catch (IOException e) {
//            System.err.println(e.getMessage());
        }
        return null;
    }

    protected abstract boolean isLineContainsKeywords(String line);
}
