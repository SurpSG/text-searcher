package com.gnatiuk.searcher.filters.internal;

import com.gnatiuk.searcher.filters.ATextFilter;
import com.gnatiuk.searcher.filters.text_processors.ITextPreprocessor;
import com.gnatiuk.searcher.filters.util.FileSearchEvent;
import com.gnatiuk.searcher.filters.util.FoundOption;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 6/15/15.
 */
public abstract class FilterFileReader extends ATextFilter implements InternalFilterMarker{

    public FilterFileReader(){
        this(new ArrayList<>());
    }

    public FilterFileReader(List<String> keywords) {
        super(keywords);
    }

    public FilterFileReader(List<String> keywords, ITextPreprocessor textPreprocessor) {
        super(keywords, textPreprocessor);
    }

    @Override
    public FileSearchEvent doFilter(File file) {
        return scanFileForKeyword(file);
    }

    private FileSearchEvent scanFileForKeyword(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            FileSearchEvent fileSearchEvent = new FileSearchEvent(file);
            String line;
            int rowNumber = 0;
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (isLineContainsKeywords(textPreprocessor.process(line))) {
                    fileSearchEvent.addFoundOption(FoundOption.FOUND_ROW, rowNumber + ": " + line);
                }

            }

            return fileSearchEvent.getOptionsCount() > 0 ? fileSearchEvent : FileSearchEvent.NOT_FOUND;
        } catch (FileNotFoundException e) {
//            System.err.println(e.getMessage());
        } catch (IOException e) {
//            System.err.println(e.getMessage());
        }
        return FileSearchEvent.NOT_FOUND;
    }

    protected abstract boolean isLineContainsKeywords(String line);
}
