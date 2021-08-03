package sql.processor;

import databaseFiles.DatabaseStructures;
import org.apache.commons.lang3.StringUtils;
import sql.Query;

import java.io.*;

public class CreateProcessor {
    public String createtable(Query queryObj, DatabaseStructures databaseStructures) {
        //TODO: CREATE TABLE
        // ADD DATA TO  METADATA FILE
        // ADD NEW FILE WITH TABLENAME.TXT AND CLOUMNNAME

        String tableName = queryObj.getTableName();
        String columns = queryObj.getColumns();
        String[] columnsList = columns.split(",");
        String pathtodbFiles = "src/main/java/databaseFiles/databases.txt";
        String makedirectory = "src/main/java/databaseFiles/";
        String finaldirectoryName = makedirectory + tableName;
        String pathtometadata = finaldirectoryName + "/";
        String finalmetadataPath = pathtometadata + "METADATA.txt";
        final String newLine = System.getProperty("line.separator");
        String appendingTxt = "";

        // TODO append data into METADATA.txt
        queryObj.getColumns();

        // TODO: create newfile with tablename.txt and add coloumname
        appendingTxt = StringUtils.join(columnsList, "|");
        try {
            FileWriter fw = new FileWriter(finalmetadataPath,true);
            fw.write(appendingTxt);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Operation done";
    }

    public String createdb(Query queryObj, DatabaseStructures databaseStructures) {
        //TODO: CREATE DB
        //  append db name to databases.txt
        //	create directory
        //	create empty metadata.txt in directory

        String dbName = queryObj.getDatabaseName();
        String pathtodbFiles = "src/main/java/databaseFiles/databases.txt";
        String makedirectory = "src/main/java/databaseFiles/";
        String finaldirectoryName = makedirectory + dbName;
//        System.out.println(finaldirectoryName);
        String pathtometadata = finaldirectoryName + "/";
        String finalmetadataPath = pathtometadata + "METADATA.txt";
        final String newLine = System.getProperty("line.separator");

        // Append db name to database.tx
        PrintWriter printWriter = null;
        File file1 = new File(pathtodbFiles);
        try {
            if (!file1.exists()) file1.createNewFile();
            printWriter = new PrintWriter(new FileOutputStream(pathtodbFiles, true));
            printWriter.write(newLine + dbName);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }

        // Create empty directory
        File directory = new File(finaldirectoryName);
        directory.mkdir();
//        System.out.println("Directory " + dbName + " created");

        // Create empty metadata file
        File file2 = new File(finalmetadataPath);
        try {
            file2.createNewFile();
//            System.out.println("Empty file created named " + "METADATA.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Operation done";
    }
}