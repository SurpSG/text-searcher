package com.pack; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {

    private static final List<String> fileFilter = Arrays.asList("java","sql", "xml");

    private static int currentSearchThreads = 0;

    private static int filesNumber = 0;

    private static List<String> files = Collections.synchronizedList(new ArrayList<String>());


    private static synchronized void increase(){
        currentSearchThreads++;
    }

    private static synchronized void increaseFilesNumber(){
        filesNumber++;
    }

    private static synchronized void decrease(){
        currentSearchThreads--;
    }

    private static synchronized int get(){
       return currentSearchThreads;
    }



    public static void main(String[] args) {



        if(args.length >= 0){
//            String filePath = args[0];
//            String textToFind = args[1];
            String filePath = "C:\\netcracker\\residential-order-entry";
//            String filePath = "C:\\netcracker\\sql_logs";
            String textToFind = "Exist in Location";

           new Thread(new Core(new String[]{filePath}, textToFind)).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                     while(get() > 0){
                         try {
                             Thread.sleep(10000);
                             System.err.println("threads: "+get());
                         } catch (InterruptedException e) {
                             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                         }
                     }

                    int i = 1;
                    for (String file : files) {
                        System.out.println("["+i+"] "+file);
                        i++;
                    }
                    System.out.println("FilesNumber="+filesNumber);
                }
            }).start();
        }
    }


    public static class Core implements Runnable{

        private String[] files;
        private String textToFind;

        public Core(String[] files, String textToFind){
            this.textToFind = textToFind;
            this.files = files;
        }

        @Override
        public void run() {
            increase();
            for (int i = 0; i < files.length; i++) {
                increaseFilesNumber();
                File file = new File(files[i]);

                if(file.isDirectory()){
                    String[] files = file.list();
                    for (int j = 0; j < files.length; j++) {
                        files[j] = file.getAbsolutePath()+"\\"+files[j];
                    }
                    new Thread(new Core(files, textToFind)).start();
//                    new Thread(new Core(Arrays.copyOfRange(files, 0, files.length / 2), textToFind)).start();
//                    new Thread(new Core(Arrays.copyOfRange(files, files.length/2, files.length), textToFind)).start();

                }else{
//                    System.out.println(file);
                    getFilesWithText(textToFind, file);
                }
            }
            decrease();
        }
    }

    public static void getFilesWithText(String text, File file) {

        if (file.isDirectory()) {
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getAbsolutePath() + "/" + files[i]);
                getFilesWithText(text, file1);
            }
        } else {
            for (String filter : fileFilter) {
                Pattern r = Pattern.compile(filter);
                Matcher m = r.matcher(file.getName());
                if (m.find() && fileContainsText(text, file)) {
                    files.add(file.getAbsolutePath());
                    System.out.println("\t[FOUND]: "+file.getAbsolutePath());
                }
            }
        }
    }

    public static boolean fileContainsText(String text, File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(text.toLowerCase())) {
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


}
