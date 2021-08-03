package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.Query;

import java.io.*;

public class CreateProcessor {
    public void create(Query queryObj, DatabaseStructures databaseStructures) {
        //TODO: CREATE TABLE
        // ADD DATA TO  METADATA FILE
        // ADD NEW FILE WITH TABLENAME.TXT AND CLOUMNNAME

        String tableName = queryObj.getTableName();
        String columns = queryObj.getColumns();
        String[] columnsList = columns.split(",");
        String pathtodbFiles = "src/main/java/databaseFiles/databases.txt";
        String pathtometaFile = "src/main/java/databaseFiles/";
        final String newLine = System.getProperty("line.separator");


//
//        File file = new File(pathtodbFiles);
//        try {
//            FileWriter fileWriter = new FileWriter(file);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            bufferedWriter.newLine();
//            bufferedWriter.write(tableName);
//            bufferedWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        // Add Data to Meta Data
//        // Make
//
//


        //TODO: CREATE DB
        //  append db name to databases.txt
        //	create directory
        //	create empty metadata.txt in directory

        // Append db name to database.tx
        PrintWriter printWriter = null;
        File file1 = new File(pathtodbFiles);
        try {
            if (!file1.exists()) file1.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(pathtodbFiles, true));
            printWriter.write(newLine + tableName);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }

        // Create empty directory
        String makedirectory = "src/main/java/databaseFiles/";
        String finaldirectoryName = makedirectory + tableName;
        File directory = new File(finaldirectoryName);
        directory.mkdir();
        System.out.println("Directory " +tableName+ " created");
        
        // Create empty metadata file
        String pathtometadata = finaldirectoryName + "/";
        String finalmetadataPath = pathtometadata + "METADATA.txt";
        File file2 = new File(finalmetadataPath);
        try {
            file2.createNewFile();
            System.out.println("Empty file created named " + "METADATA.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
