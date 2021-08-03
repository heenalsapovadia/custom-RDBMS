package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;
import sql.QueryValidator;

import java.util.*;

public class DeleteProcessor {
    LockManager lockManager = new LockManager();
    QueryValidator queryValidator = new QueryValidator();
    public String logMessage = "";

    public DatabaseStructures process (Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            logMessage = "** LOCK CONSTRAINT : "+tableName;
            return null;
        }

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
            System.out.println("index :"+indexToRemove.get(i));
            System.out.println(tableData.remove(tableData.get(indexToRemove.get(i))));
            System.out.println(tableData.size());
        }
        databaseStructures.databaseData.put(tableName, tableData);
//        databaseStructures.storeDatabase("update", tableName);

        // release the lock
        lockManager.releaseLock(databaseName, tableName);
        logMessage = "Deleted "+indexToRemove.size()+" rows";
        return databaseStructures;
    }
}
