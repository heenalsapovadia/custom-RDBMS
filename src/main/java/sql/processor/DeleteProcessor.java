package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;

import java.util.*;

public class DeleteProcessor {
    LockManager lockManager = new LockManager();

    public String process (Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            return "** LOCK CONSTRAINT : "+tableName;
        }

        Map<String, String> optionsMap = queryObj.getOptionMap();
        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        String primaryKeyColumn = databaseStructures.primaryKeyMap.get(tableName);
        Set<String> uniqueKeyValues = new HashSet<>();
        for (Map<String, String> record : tableData) {
            uniqueKeyValues.add(record.get(primaryKeyColumn));
        }
        List<Integer> indexToRemove = new ArrayList<>();
        // for loop over tableData - update the ds
        for (int i=0; i<tableData.size(); i++) {
            Map<String, String> row = tableData.get(i);
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
            indexToRemove.add(i);
        }
        for (int i= indexToRemove.size()-1; i>=0; i--) {
            tableData.remove(indexToRemove.get(i));
            System.out.println(tableData.size());
        }
        databaseStructures.databaseData.put(tableName, tableData);
//        databaseStructures.databaseData.remove(tableName, tableData);
        //write the updated value in db
        databaseStructures.storeDatabase("update", tableName);

        // release the lock
        lockManager.releaseLock(databaseName, tableName);

        return "Successfully Deleted";
    }
}
