package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;
import sql.QueryValidator;

import java.util.*;

public class InsertProcessor {

    LockManager lockManager = new LockManager();
    QueryValidator queryValidator = new QueryValidator();
    public String logMessage = "";

    public DatabaseStructures process(Query queryObj, DatabaseStructures databaseStructures) {

        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            logMessage = "** LOCK CONSTRAINT : "+tableName;
            return null;
        }

        Map<String, String> optionsMap = queryObj.getOptionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        String primaryKeyColumn = databaseStructures.primaryKeyMap.get(tableName);
        Set<String> uniqueKeyValues = new HashSet<>();
        for (Map<String, String> record : tableData) {
            uniqueKeyValues.add(record.get(primaryKeyColumn));
        }

        Map<String, String> row = new HashMap<>();
        for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
            if (entry.getKey().equals(primaryKeyColumn)) {
                if (uniqueKeyValues.contains(entry.getValue())) {
                    // release locks
                    lockManager.releaseLock(databaseName, tableName);
                    logMessage = "** PRIMARY KEY CONSTRAINT VIOLATED **";
                    return null;
                }
            }
            row.put(entry.getKey(), entry.getValue());
        }
        tableData.add(row);
        databaseStructures.databaseData.put(tableName, tableData);
        //write the updated value in db
//        databaseStructures.storeDatabase("insert", tableName);

        // release the lock
        lockManager.releaseLock(databaseName, tableName);
        logMessage = "Successfully Inserted 1 row";
        return databaseStructures;
    }
}
