package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.Query;

import java.util.*;

public class SelectProcessor {

    public void process (Query queryObj, DatabaseStructures databaseStructures) {
        String tableName = queryObj.getTableName();
        String columns = queryObj.getColumns();
        String[] columnsList = columns.split(",");
        // set the columnsList to all columns if it is * ---------------- TBD
        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);

        List<Map<String, String>> selectedData = new ArrayList<>();

        for (Map<String, String> row : tableData) {
            Map<String, String> selectedRow = new HashMap<>();
            for (String column : columnsList) {
                //condition check
                if (conditionMap.containsKey(column)) {
                    if (conditionMap.get(column).equals(row.get(column)))
                        selectedRow.put(column, row.get(column));
                }
                else
                    selectedRow.put(column, row.get(column));
            }
            if (!selectedRow.isEmpty())
                selectedData.add(selectedRow);
        }
        displaySelectedData(selectedData);
    }

    public void displaySelectedData (List<Map<String, String>> selectedData) {
        Set<String> columns = selectedData.get(0).keySet();
        List<String> columnsList = new ArrayList<>();
        columnsList.addAll(columns);
        for (String column : columnsList) {
            System.out.print(column + " | ");
        }
        for (Map<String, String> row : selectedData) {
            for (String column : columnsList) {
                System.out.println(row.get(column) + " | ");
            }
        }
    }
}
