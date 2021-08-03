package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;

import java.util.*;

public class InsertProcessor {

    LockManager lockManager = new LockManager();

    public String process(Query queryObj, DatabaseStructures databaseStructures) {

        //String databaseName = databaseStructures.databaseName;
        //String tableName = queryObj.getTableName();

        // check for locks and apply lock
        //if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
        // return appropriate message for log
        //   return "** LOCK CONSTRAINT : "+tableName;
        // }


        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            return "** LOCK CONSTRAINT : " + tableName;
        }

        Map<String, String> optionsMap = queryObj.getOptionMap();
        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        String primaryKeyColumn = databaseStructures.primaryKeyMap.get(tableName);
        Set<String> uniqueKeyValues = new HashSet<>();
        for (Map<String, String> record : tableData) {
            uniqueKeyValues.add(record.get(primaryKeyColumn));
        }


        for (int i = 0; i < tableData.size(); i++) {
            Map<String, String> row = tableData.get(i);

            for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
                if (entry.getKey().equals(primaryKeyColumn)) {
                    if (uniqueKeyValues.contains(entry.getValue())) {
                        // release locks
                        lockManager.releaseLock(databaseName, tableName);
                        System.out.println("** PRIMARY KEY CONSTRAINT VIOLATED **");
                        return "** PRIMARY KEY CONSTRAINT VIOLATED **";
                    }
                }
                row.put(entry.getKey(), entry.getValue());
            }
            //tableDat.set(i, row);n "Successfully added "+3+" rows";
        }
        return "Successfully added";
    }
}
