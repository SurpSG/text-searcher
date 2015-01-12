package com.pack; /**
 * Created with IntelliJ IDEA.
 * User: segn1014
 * Date: 1/12/15
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {

    private static final String fileFilter = "\\.";
    private static long currentSearchThreads = 0;

    private static synchronized void increase(){
        currentSearchThreads++;
        System.out.println("currentSearchThreads="+currentSearchThreads);
    }

    private static synchronized void decrease(){
        currentSearchThreads--;
        System.out.println("currentSearchThreads="+currentSearchThreads);
    }

    public static void main(String[] args) {


        if(args.length >= 0){
//            String filePath = args[0];
//            String textToFind = args[1];
            String filePath = "C:\\netcracker\\0_9.3_BE_PartnerPortal_build107";
//            String filePath = "C:\\netcracker\\sql_logs";
            String textToFind = "4063055154013004343";

           new Thread(new Core(new String[]{filePath}, textToFind)).start();
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
//            increase();
            for (int i = 0; i < files.length; i++) {

                File file = new File(files[i]);

                if(file.isDirectory()){
                    String[] files = file.list();
                    for (int j = 0; j < files.length; j++) {
                        files[j] = file.getAbsolutePath()+"\\"+files[j];
                    }
                    new Thread(new Core(files, textToFind)).start();
//                    new Thread(new Core(Arrays.copyOfRange(files, 0, files.length/2), textToFind)).start();
//                    new Thread(new Core(Arrays.copyOfRange(files, files.length/2, files.length), textToFind)).start();

                }else{
                    getFilesWithText(textToFind, file);
                }
            }
//            decrease();
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
            Pattern r = Pattern.compile(fileFilter);
            Matcher m = r.matcher(file.getName());
            if (m.find() && fileContainsText(text, file)) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    public static boolean fileContainsText(String text, File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains(text)) {
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
