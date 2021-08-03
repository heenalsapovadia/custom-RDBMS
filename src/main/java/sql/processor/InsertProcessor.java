package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertProcessor {

    LockManager lockManager = new LockManager();

    public String process (Query queryObj, DatabaseStructures databaseStructures) {

        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            return "** LOCK CONSTRAINT : "+tableName;
        }

        Map<String, String> optionsMap = queryObj.getOptionMap();
        //Map<String, String> conditionMap = queryObj.getConditionMap();





        /*
        String tableName = queryObj.getTableName();
        String[] columns = queryObj.getColumn();
        //String[] columns = columns.split(",");
        // set the columnsList to all columns if it is * ---------------- TBD
        Map<String, String> valueMap = queryObj.getValueMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);

        List<Map<String, String>> selectedData = new ArrayList<>();

        for (Map<String, String> row : tableData) {
            Map<String, String> selectedRow = new HashMap<>();
            for (String column : columns) {
                //condition check
                if (valueMap.containsKey(column)) {
                    if (valueMap.get(column).equals(row.get(column)))
                        selectedRow.put(column, row.get(column));
                }
                else
                    selectedRow.put(column, row.get(column));
            }
            if (!selectedRow.isEmpty())
                selectedData.add(selectedRow);
        }*/
        //displaySelectedData(selectedData);
        return "Successfully added "+3+" rows";
    }
}
