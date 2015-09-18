package com.gnatiuk.searcher.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgnatiuk on 9/18/15.
 */
public class FileUtils {

    public static List<String> readFile(File file) throws IOException {
        List<String> fileRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileRows.add(line);
            }
        }

        return fileRows;
    }

    public static void writeToFile(File file, List<String> rows) throws IOException {

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            for (String row : rows) {
                bw.write(row);
                bw.newLine();
            }
        }
    }
}
