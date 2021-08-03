package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;
import sql.QueryValidator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProcessor {

    LockManager lockManager = new LockManager();
    QueryValidator queryValidator = new QueryValidator();
    public String logMessage = "";

    public DatabaseStructures process (Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            logMessage = "** LOCK CONSTRAINT : "+tableName;
            return null;
        }

        Map<String, String> optionsMap = queryObj.getOptionMap();
        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        String primaryKeyColumn = databaseStructures.primaryKeyMap.get(tableName);
        Map<String, String> tbStructure = databaseStructures.tableStructures.get(tableName);
        Set<String> uniqueKeyValues = new HashSet<>();
        for (Map<String, String> record : tableData) {
            uniqueKeyValues.add(record.get(primaryKeyColumn));
        }
        int rowsModified = 0;
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
            for (Map.Entry<String, String> entry : optionsMap.entrySet()) {
                if (entry.getKey().equals(primaryKeyColumn)) {
                    if (uniqueKeyValues.contains(entry.getValue())) {
                        // release locks
                        lockManager.releaseLock(databaseName,tableName);
                        logMessage = "** PRIMARY KEY CONSTRAINT VIOLATED **";
                        return null;
                    }
                }
                if (tbStructure.get(entry.getKey()).equals("int")) {
                    if (queryValidator.isInteger(entry.getValue())) {
                        row.put(entry.getKey(), entry.getValue());
                    }
                    else {
                        lockManager.releaseLock(databaseName,tableName);
                        logMessage = "** DATATYPE CONSTRAINT VIOLATED - Expected INT **";
                        return null;
                    }
                }
                else if (tbStructure.get(entry.getKey()).equals("varchar")) {
                    if (queryValidator.isVarchar(entry.getValue())) {
                        row.put(entry.getKey(), entry.getValue().replaceAll("\\'", ""));
                    }
                    else {
                        // release locks
                        lockManager.releaseLock(databaseName,tableName);
                        logMessage = "** DATATYPE CONSTRAINT VIOLATED - Expected VARCHAR [''] **";
                        return null;
                    }
                }

            }
            tableData.set(i, row);
            rowsModified++;
        }
        databaseStructures.databaseData.put(tableName, tableData);
        //write the updated value in db
//        databaseStructures.storeDatabase("update", tableName);

        // release the lock
        lockManager.releaseLock(databaseName, tableName);
        logMessage = "Successfully Updated "+rowsModified+" rows";
        return databaseStructures;
    }
}
