package com.gnatiuk.searcher.core; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class Finder {

    private static volatile int currentSearchThreads = 0;

    private final int processors;
    private static int filesNumber = 0;

    private static Set<String> files = Collections.synchronizedSet(new TreeSet<String>());


    private static synchronized void increaseSearchThreads(){
        currentSearchThreads++;
    }

    private static synchronized void increaseFilesNumber(){
        filesNumber++;
    }

    private static synchronized void decreaseHierarchyThreads(){
        currentSearchThreads--;
    }

    private static synchronized int getCurrentSearchThreads(){
        return currentSearchThreads;
    }


    private List<String> textsToFind;
    private List<String> filePaths;
    private List<Pattern> fileFilterPatterns;

    public static void main(String[] args) {

        String textToFind = "new PurchaseProblems\\(.*,.*,.*\\)";
        textToFind = "public PurchaseProblems\\(";
        textToFind = "new PurchaseProblems(";
        textToFind = "Print.out(Print.FATAL, \"surop caStatus=\"+caStatus);";
        textToFind = "createPurchaseProblems";
        textToFind = "segn";

        String[] filePaths = {
                    "/cryptfs/builds/shr26-700/root_pac/build_mips",
//                    "/cryptfs/logs",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/druid",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/middleware_core",
//                "/cryptfs/builds/shr26-700/root_pac/build_mips/centrals",
        };

        List<String> fileFilters = Arrays.asList(/*"\\.java$"/*,"\\.c$"*/);



        Finder finder = new Finder(textToFind, Arrays.asList(filePaths), fileFilters);
//        finder = new FinderPatternBased(textToFind, filePaths, fileFilters);
        finder.start();

    }

    public Finder(String textToFind, List<String> filePaths, List<String> fileFilters){
        this(Arrays.asList(textToFind), filePaths, fileFilters);
    }

    public Finder(List<String> textsToFind, List<String> filePaths, List<String> fileFilters){
        processors = Runtime.getRuntime().availableProcessors();
        this.textsToFind = textsToFind;
        this.filePaths = filePaths;
        fileFilterPatterns = createPatterns(fileFilters);
    }

    public void start(){
        final long t1 = System.currentTimeMillis();


            new Thread(new Core(filePaths)).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentSearchThreads;

                    while( (currentSearchThreads = getCurrentSearchThreads()) > 0){

                        System.err.println("threads: " + currentSearchThreads);
                        System.err.println("FilesNumber="+filesNumber);
                        if(currentSearchThreads > 100){
                            System.exit(1);
                        }

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    long t2 = System.currentTimeMillis() - t1;

                    int i = 1;
                    for (String file : files) {
                        System.out.println("["+i+"] "+file);
                        i++;
                    }
                    System.out.println("time: "+(t2));
                    System.out.println("FilesNumber="+filesNumber);
                }
            }).start();
    }

    protected List<Pattern> createPatterns(List<String> stringPatterns){
        List<Pattern> patterns = new ArrayList<>();

        for (String filter : stringPatterns) {
            patterns.add(Pattern.compile(filter));
        }

        return patterns;
    }

    protected Pattern createPattern(String stringPatterns){
        return Pattern.compile(stringPatterns);
    }


    private class Core implements Runnable{

        private List<String> filesPaths;

        public Core(List<String> filesPaths){
            this.filesPaths = filesPaths;
        }

        @Override
        public void run() {
            sleepThreadIfToMuchOtherThreads();
            increaseSearchThreads();

            for (String filePath : filesPaths) {
                increaseFilesNumber();
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

                    new Thread(new Core(Arrays.asList(files))).start();
                }else{
                    checkFileWithSearchParams(file);

                }
            }
            decreaseHierarchyThreads();
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

    private void sleepThreadIfToMuchOtherThreads(){
        while ((processors - getCurrentSearchThreads()) <= 0){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void getFilesWithText(File file) {

        if(isEmpty(textsToFind)){
            return;
        }

        if (file.isFile()) {
            if (fileContainsText(file)) {
                files.add(file.getAbsolutePath());
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

    public static Finder build(FinderType finderType, String textToFind, List<String> filePaths, List<String> fileFilters){
        switch (finderType){
            case PATTERN_BASED:
                return new FinderPatternBased(textToFind, filePaths, fileFilters);
            default:
                return new Finder(textToFind, filePaths, fileFilters);
        }
    }


}
