package sql.processor;

import databaseFiles.DatabaseStructures;
import org.apache.commons.lang3.StringUtils;
import sql.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateProcessor {
    public String createtable(Query queryObj, DatabaseStructures databaseStructures) {
        //TODO: CREATE TABLE
        // ADD DATA TO  METADATA FILE
        // ADD NEW FILE WITH TABLENAME.TXT AND CLOUMNNAME


        String tableName = queryObj.getTableName();
        String databaseName = queryObj.getDatabaseName();
        Map<String, String> columns = queryObj.getOptionMap();
        String[] columnsList = null;
        String pathtodbFiles = "src/main/java/databaseFiles/databases.txt";
        String makedirectory = "src/main/java/databaseFiles/";
        String finaldirectoryName = makedirectory + databaseName;
        String pathtometadata = finaldirectoryName + "/";
        String finalmetadataPath = pathtometadata + "METADATA.txt";
        final String newLine = System.getProperty("line.separator");
        String appendingTxt = "";

        // TODO append data into METADATA.txt
        databaseStructures.primaryKeyMap.put(tableName, queryObj.getPrimarykey());
        List<Map<String, String>> fkList = new ArrayList<>();
        fkList.add(queryObj.getFKMap());
        databaseStructures.foreignKeyMap.put(tableName, fkList);
        databaseStructures.tableStructures.put(tableName, queryObj.getOptionMap());
        databaseStructures.pushKeys(tableName);


        // TODO: create newfile with tablename.txt and add coloumname
        String newTablePath = makedirectory+databaseStructures.databaseName+"/"+tableName+".txt";
        Set<String> colSet = queryObj.getOptionMap().keySet();
        StringBuilder columnHeader = new StringBuilder();
        for (String str : colSet) {
            columnHeader.append(str+"|");
        }
        columnHeader.deleteCharAt(columnHeader.length()-1);

//        appendingTxt = StringUtils.join(columnsList, "|");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newTablePath));
            bufferedWriter.write(columnHeader.toString());
            bufferedWriter.close();
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