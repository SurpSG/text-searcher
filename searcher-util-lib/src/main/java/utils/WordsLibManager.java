package utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sgnatiuk on 10/8/15.
 */
public final class WordsLibManager {

    private static final String DATA_SEPARATOR = " ";
    private static final int MAX_WORDS_TO_SAVE = 3;

    private static final Object keywordsSynchronizer = new Object();

    private Set<PriorityItem<String>> keywordsLib;
    private File wordsLibFile;

    private WordsLibManager(){
        wordsLibFile = new File(AppOptions.KEY_ASSIST_FILE_PATH);
    }

    static class InstanceContainer{
        public static final WordsLibManager instance = new WordsLibManager();
    }

    public Collection<String> getKeywords(){
        synchronized (keywordsSynchronizer){
            if(keywordsLib == null){//Lib did not loaded
                keywordsLib = new TreeSet<PriorityItem<String>>(new PriorityItem.PriorityItemComparator<>());
                keywordsLib.addAll(loadWords());
            }
            return Collections.unmodifiableCollection(
                    keywordsLib.stream()
                            .map(priorityItem -> priorityItem.getData())
                            .collect(Collectors.toList())
            );
        }
    }

    public void saveWords(Collection<String> words){
        synchronized (keywordsSynchronizer){
            for (String word : words) {
                keywordsLib.addAll(
                        Arrays.asList(word.split(DATA_SEPARATOR))
                                .stream()
                                .map(s -> new PriorityItem<>(1, s))
                                .collect(Collectors.toList()));
            }
            saveWordsLib();
        }
    }

    public void saveWord(String word){
        saveWords(Arrays.asList(word));
    }

    public void clearBuffer(){
        clearBuffer(false);
    }

    public void clearBuffer(boolean saveChanges){
        synchronized (keywordsSynchronizer){
            if(saveChanges){
                saveWordsLib();
            }
            keywordsLib.clear();
            keywordsLib = null;
        }
    }

    public static WordsLibManager getInstance(){
        return InstanceContainer.instance;
    }

    private void saveWordsLib(){
        synchronized (wordsLibFile){
            try {
                FileUtils.writeToFile(wordsLibFile,
                        keywordsLib.stream()
                                .limit(MAX_WORDS_TO_SAVE)
                                .map(
                                        priorityItem -> new String(priorityItem.getData() + DATA_SEPARATOR + priorityItem.getPriority())
                                ).collect(Collectors.toList()),
                        true
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<PriorityItem<String>> loadWords(){
        synchronized (wordsLibFile){
            try {

                return FileUtils.readFile(new File(AppOptions.KEY_ASSIST_FILE_PATH), true).stream()
                        .map(s -> parseRowToPriorityItem(s))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    private PriorityItem<String> parseRowToPriorityItem(String row){
        StringTokenizer stringTokenizer = new StringTokenizer(row, DATA_SEPARATOR);
        try{
            String data = stringTokenizer.nextToken();
            String priority = stringTokenizer.nextToken();
            return new PriorityItem<>(Integer.parseInt(priority), data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PriorityItem<>(Integer.MIN_VALUE, "");
    }
}
