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

    private List<PriorityDateItem<String>> keywordsLib;
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
                keywordsLib = new ArrayList<>();
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
        System.out.println("word="+word);
//                keywordsLib.addAll(
//                        Arrays.asList(word.split(DATA_SEPARATOR))
//                                .stream()
//                                .map(s -> new PriorityItem<>(1, s))
//                                .collect(Collectors.toList()));
                for (String s : word.split(DATA_SEPARATOR)) {
                    PriorityDateItem<String> newPriorityItem = new PriorityDateItem<>(1, System.currentTimeMillis(), s);
                    int index = keywordsLib.indexOf(newPriorityItem);
                    if (index >= 0){
                        PriorityDateItem<String> priorityItemInLib = keywordsLib.get(index);
                        priorityItemInLib.setPriority(priorityItemInLib.getPriority() + 1);
                        priorityItemInLib.setDate(System.currentTimeMillis());
                    }else {
                        keywordsLib.add(newPriorityItem);
                    }
                }
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
                keywordsLib.sort(new PriorityDateItem.PriorityDateItemComparator<>());
                FileUtils.writeToFile(wordsLibFile,
                        keywordsLib.stream()
                                .limit(MAX_WORDS_TO_SAVE)
                                .map(
                                        priorityItem ->
                                                new String(
                                                        priorityItem.getData() +
                                                                DATA_SEPARATOR +
                                                                priorityItem.getPriority() +
                                                                DATA_SEPARATOR +
                                                                priorityItem.getDate()
                                                )
                                ).collect(Collectors.toList()),
                        true
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<PriorityDateItem<String>> loadWords(){
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

    private PriorityDateItem<String> parseRowToPriorityItem(String row){
        StringTokenizer stringTokenizer = new StringTokenizer(row, DATA_SEPARATOR);
        try{
            String data = stringTokenizer.nextToken();
            String priority = stringTokenizer.nextToken();
            String date = stringTokenizer.nextToken();
            return new PriorityDateItem<>(Integer.parseInt(priority), Long.parseLong(date), data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PriorityDateItem<>(Integer.MIN_VALUE, 0, "");
    }
}
