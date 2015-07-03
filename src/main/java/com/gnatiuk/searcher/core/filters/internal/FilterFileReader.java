package com.gnatiuk.searcher.core.filters.internal;

import com.gnatiuk.searcher.core.filters.ATextFilter;
import com.gnatiuk.searcher.core.filters.ITextPreprocessor;

import java.io.*;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public abstract class FilterFileReader extends ATextFilter {

    public FilterFileReader(List<String> keywords) {
        super(keywords);
    }

    public FilterFileReader(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
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

            int n = 0;
            String b = null;
            while ((line = reader.readLine()) != null) {
                n++;
                if (isLineContainsKeywords(textPreprocessor.process(line))) {
                    System.out.println("\t\t[LINE]: "+n+") "+line);
//                    return line;
                    b = "";
                }

            }

            return b;
        } catch (FileNotFoundException e) {
//            System.err.println(e.getMessage());
        } catch (IOException e) {
//            System.err.println(e.getMessage());
        }
        return null;
    }

    protected abstract boolean isLineContainsKeywords(String line);
}
