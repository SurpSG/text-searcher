package utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sgnatiuk on 10/8/15.
 */
public final class WordsLibManager {

    private static final Object keywordsSynchronizer = new Object();

    private Set<String> keywordsLib;
    private Collection<String> immutableLib;

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
                keywordsLib = new TreeSet<>(loadWords());
                immutableLib = Collections.unmodifiableCollection(keywordsLib);
            }
        }
        return immutableLib;
    }

    public void saveWords(Collection<String> words){
        synchronized (keywordsSynchronizer){
            for (String word : words) {
                keywordsLib.addAll(Arrays.asList(word.split(" ")));
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
            immutableLib = null;
        }
    }

    public static WordsLibManager getInstance(){
        return InstanceContainer.instance;
    }

    private void saveWordsLib(){
        synchronized (wordsLibFile){
            try {
                FileUtils.writeToFile(wordsLibFile, keywordsLib, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> loadWords(){
        synchronized (wordsLibFile){
            try {
                return FileUtils.readFile(new File(AppOptions.KEY_ASSIST_FILE_PATH), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }
}
