package com.gnatiuk.searcher.core;

import com.gnatiuk.searcher.core.utils.ThreadCompleteEvent;
import com.gnatiuk.searcher.core.utils.IThreadCompleteListener;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sgnatiuk on 6/2/15.
 */
public class SearchRunnable implements Runnable{


    private static int ID_SOURCE = 0;

    private final int id;
    private IThreadCompleteListener IThreadCompleteListener;

    private List<String> textsToFind;
    private List<String> filePaths;
    private List<Pattern> fileFilterPatterns;

    public SearchRunnable(List<String> textsToFind, List<String> filePaths, List<Pattern> fileFilters){
        this.textsToFind = textsToFind;
        this.filePaths = filePaths;
        fileFilterPatterns = fileFilters;
        id = ID_SOURCE++;
        this.filePaths = filePaths;
    }

    @Override
    public void run() {

        try{
            performSearch();
        }finally {
            IThreadCompleteListener.actionPerformed(new ThreadCompleteEvent(id));
        }
    }

    private void performSearch(){
        for (String filePath : filePaths) {
            File file = new File(filePath);

            if(file.isDirectory() && !Files.isSymbolicLink(file.toPath())){
                String[] files = file.list();
                if(files == null){
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < files.length; j++) {

                    stringBuilder.append(file.getAbsolutePath())
                            .append("/")
                            .append(files[j]);

                    files[j] = stringBuilder.toString();
                    stringBuilder.setLength(0);
                }

                ThreadController.getInstance().registerThread(new SearchRunnable(textsToFind, Arrays.asList(files), fileFilterPatterns));
            }else{
                checkFileWithSearchParams(file);
            }
        }
    }

    private void checkFileWithSearchParams(File file){
        if(fileFilterPatterns.isEmpty()){
            getFilesWithText(file);
        }else{
            for (Pattern fileFilterPattern : fileFilterPatterns) {
                if(fileFilterPattern.matcher(file.getName()).find()){
                    getFilesWithText(file);
                    break;
                }
            }
        }
    }

    private void getFilesWithText(File file) {

        if(isEmpty(textsToFind)){
            return;
        }

        if (file.isFile()) {
            if (fileContainsText(file)) {
                System.out.println("\t[FOUND]: "+file.getAbsolutePath());
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

    private boolean isLineContainsKeywords(String line){
        for (String textToFind : textsToFind) {
            if(isLineContainsKeyword(line, textToFind)){
                return true;
            }
        }
        return false;
    }


    protected boolean isLineContainsKeyword(String line, String keyword){
        return line.contains(keyword);
    }

    private boolean isEmpty(List<String> list){
        for (String element : list) {
            if(!element.isEmpty()){
                return false;
            }
        }
        return true;
    }

    public void addThreadCompleteListener(IThreadCompleteListener IThreadCompleteListener){
        this.IThreadCompleteListener = IThreadCompleteListener;
    }

    public int getId() {
        return id;
    }
}