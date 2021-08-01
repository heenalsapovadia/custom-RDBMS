package databaseFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseStructures {
    // table : <colname, value>
    public Map<String, List<Map<String, String>>> databaseData = new HashMap<>();
    // table : <col, datatype>
    public Map<String, Map<String, String>> tableStructures = new HashMap<>();
    public Map<String, String> primaryKeyMap = new HashMap<>();
    public Map<String, String> foreignKeyMap = new HashMap<>(); // doubtful

    String pathToDatabases = "/src/main/java/databaseFiles";

    /*
    Loads the database structures from files
     */
    public void loadDatabase (String databaseName) {
        String resPath = DatabaseStructures.class.getClassLoader().getResource(databaseName).getPath();
//        File databaseDirectory = new File(getClass().getResource("databases.txt").getPath());
        File databaseDirectory = new File(databaseName);
        System.out.println(databaseDirectory.getAbsolutePath());
//        String path = databaseDirectory.getAbsolutePath();
//        File dir = new File(path);
//        System.out.println(dir.isDirectory());


        String path = "/home/heenal/DAL/DW/Project/csci-5408-dp-4";
        File dir = new File(path+pathToDatabases+"/"+databaseName);
        System.out.println(dir.isDirectory());

        for (File table : dir.listFiles()) {
            System.out.println("Table : "+table);
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(table));
                String line = bufferedReader.readLine();
                while (line!=null) {

                }
            }
            catch (IOException io) {
                System.out.println("Exception in reading table file : "+io);
            }
        }
    }

    /*
    Write the database structures back to files
     */
    public void storeDatabase (String databaseName) {

    }
}
