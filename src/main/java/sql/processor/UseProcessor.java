package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.Query;

import java.util.*;

public class UseProcessor {
    private String database = null;

    public String getDatabase() {
        return database;
    }
    public DatabaseStructures process (Query queryObj, DatabaseStructures databaseStructures) {

        //this.username = username;
        this.database = database;
        String newDatabase = (String) queryObj.getDatabaseName();
        System.out.println(newDatabase);

        databaseStructures.databaseName = newDatabase;
        System.out.println(databaseStructures.databaseName);
        return databaseStructures;

        /*String tableName = queryObj.getTableName();
        String columns = queryObj.getColumns();
        String[] columnsList = columns.split(",");
        // set the columnsList to all columns if it is *
        if (columnsList.length == 1) {
            Set<String> columnsSet = databaseStructures.tableStructures.get(tableName).keySet();
            List<String> colsList = new ArrayList<>();
            colsList.addAll(columnsSet);

            columnsList = new String[colsList.size()];
            for (int i=0; i<columnsList.length; i++) {
                columnsList[i] = colsList.get(i);
            }
        }

        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        List<Map<String, String>> selectedData = new ArrayList<>();

        for (Map<String, String> row : tableData) {
            Map<String, String> selectedRow = new HashMap<>();
            if (conditionMap!=null) {
                boolean conditionPass = true;
                for (Map.Entry<String, String> map : conditionMap.entrySet()) {
                    if (!row.get(map.getKey()).equals(map.getValue())) {
                        conditionPass = false;
                        break;
                    }
                }
                if (!conditionPass)
                    continue;
            }
            for (String column : columnsList) {
                selectedRow.put(column, row.get(column));
            }
            if (!selectedRow.isEmpty()) {
                selectedData.add(selectedRow);
            }
        }
*/

        //return "database selected : "+databaseStructures.databaseName;
    }
}
