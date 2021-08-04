package erdGenerator;

import databaseFiles.DatabaseStructures;

import java.io.*;
import java.util.*;

public class ERDGenerator {
    // list of db table pairs with cardinality
    public List<Map<String, String>> cardinalityList = new ArrayList<>();

    public String pathToERD = "src/main/java/erdGenerator";
    String pathToDatabases = "src/main/java/databaseFiles";

    public void generateERD (String database, DatabaseStructures databaseStructures) {

        // generate cardinality list
        generateCardinality(database, databaseStructures);

        String pathToErdOfDatabase = pathToERD+"/"+database+".txt";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToErdOfDatabase));
            for (Map<String, String> cardinalityMap : cardinalityList) {
                Set<String> tableSet = cardinalityMap.keySet();
                List<String> tableList = new ArrayList<>();
                tableList.addAll(tableSet);

                String table1 = printTable(tableList.get(0), databaseStructures);
                bufferedWriter.write(table1);
                System.out.println(table1);

                String str = "Cardinality between "+tableList.get(0)+" and "+tableList.get(1)+" - ";
                String cardinality = cardinalityMap.get(tableList.get(0))+" : "+cardinalityMap.get(tableList.get(1));
                bufferedWriter.write(str+cardinality);
                bufferedWriter.newLine();
                System.out.println(str+cardinality);

                String table2 = printTable(tableList.get(1), databaseStructures);
                bufferedWriter.write(table2);
                System.out.println(table2);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException io) {
            System.out.println("Exception in writing ERD file : "+io.getMessage());
        }
    }

    /*
    Generate the list of cardinalities between tables containing data

     */
    public void generateCardinality (String database, DatabaseStructures databaseStructures) {
        cardinalityList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> map : databaseStructures.foreignKeyMap.entrySet()) {
            String tableName = map.getKey();
            if (!isTableEmpty(database, tableName)) {
                for (Map<String, String> foreignKey : map.getValue()) {
                    Map<String, String> cardinalityMap = new HashMap<>();
                    String refTableName = foreignKey.get("refTable");
                    if (!isTableEmpty(database, refTableName)) {
                        cardinalityMap.put(refTableName, "1");
                        if (databaseStructures.primaryKeyMap.containsKey(tableName)) {
                            cardinalityMap.put(tableName, "m");
                        } else {
                            cardinalityMap.put(tableName, "1");
                        }
                        cardinalityList.add(cardinalityMap);
                    }
                }
            }
        }
    }

    private boolean isTableEmpty (String database, String table) {
        String pathToTable = pathToDatabases+"/"+database+"/"+table+".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToTable));
            String columnHeader = bufferedReader.readLine();
            if (bufferedReader.readLine()!=null)
                return false;
            else
                return true;
        }
        catch (IOException io) {
            System.out.println("Exception in reading table file : "+io.getMessage());
        }
        return true;
    }

    /*
    Print the table structure and KEYS
     */
    private String printTable (String table, DatabaseStructures databaseStructures) {
        Map<String, Map<String, String>> tableStructures = databaseStructures.tableStructures;
        Map<String, String> primaryKeyMap = databaseStructures.primaryKeyMap;
        Map<String, List<Map<String, String>>> foreignKeyMap = databaseStructures.foreignKeyMap;
        StringBuilder printer = new StringBuilder();

        printer.append("------------- TABLE : "+table+" ------------- \n");
        for (Map.Entry<String, String> map : tableStructures.get(table).entrySet()) {
            printer.append(map.getKey()+" : "+map.getValue()+"\n");
        }
        if (primaryKeyMap.containsKey(table)) {
            printer.append("PRIMARY KEY : "+primaryKeyMap.get(table)+"\n");
        }
        if (foreignKeyMap.containsKey(table)) {
            List<Map<String, String>> fkList = foreignKeyMap.get(table);
            for (Map<String, String> fk : fkList) {
                printer.append("FOREGIN KEY : "+fk.get("column")+" REFERENCES "+fk.get("refTable")+"("+fk.get("refTableColumn")+")"+"\n");
            }
        }
        return printer.toString();
    }
}
