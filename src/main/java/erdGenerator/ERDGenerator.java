package erdGenerator;

import databaseFiles.DatabaseStructures;

import java.io.*;
import java.util.*;

public class ERDGenerator {
    public List<Map<String, String>> cardinalityList = new ArrayList<>();
    public String pathToERD = "src/main/java/erdGenerator";
    String pathToDatabases = "src/main/java/databaseFiles";

    public void generateERD (String database, DatabaseStructures databaseStructures) {
        generateCardinality(database, databaseStructures);
        String pathToErdOfDatabase = pathToERD+"/"+database+".txt";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToErdOfDatabase));
            for (Map<String, String> cardinalityMap : cardinalityList) {
                Set<String> tableSet = cardinalityMap.keySet();
                List<String> tableList = new ArrayList<>();
                tableList.addAll(tableSet);
                String str = "Cardinality between "+tableList.get(0)+" and "+tableList.get(1)+" - ";
                String cardinality = cardinalityMap.get(tableList.get(0))+" : "+cardinalityMap.get(tableList.get(1));
                bufferedWriter.write(str+cardinality);
                System.out.println(str+cardinality);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException io) {
            System.out.println("Exception in writing ERD file : "+io.getMessage());
        }
    }

    public void generateCardinality (String database, DatabaseStructures databaseStructures) {
        cardinalityList = new ArrayList<>();
        System.out.println(databaseStructures.foreignKeyMap.size());
        for (Map.Entry<String, List<Map<String, String>>> map : databaseStructures.foreignKeyMap.entrySet()) {
            System.out.println(map.getKey());
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
}
