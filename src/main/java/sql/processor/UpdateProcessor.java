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

    public String process (Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            // return appropriate message for log
            return "** LOCK CONSTRAINT : "+tableName;
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
//                        System.out.println("** PRIMARY KEY CONSTRAINT VIOLATED **");
                        return "** PRIMARY KEY CONSTRAINT VIOLATED **";
                    }
                }
                if (tbStructure.get(entry.getKey()).equals("int")) {
                    if (queryValidator.isInteger(entry.getValue())) {
                        row.put(entry.getKey(), entry.getValue());
                    }
                    else {
                        lockManager.releaseLock(databaseName,tableName);
                        return "** DATATYPE CONSTRAINT VIOLATED - Expected INT **";
                    }
                }
                else if (tbStructure.get(entry.getKey()).equals("varchar")) {
                    if (queryValidator.isVarchar(entry.getValue())) {
                        row.put(entry.getKey(), entry.getValue().replaceAll("\\'", ""));
                    }
                    else {
                        System.out.println("inside else");
                        // release locks
                        lockManager.releaseLock(databaseName,tableName);
                        return "** DATATYPE CONSTRAINT VIOLATED - Expected VARCHAR [''] **";
                    }
                }

            }
            tableData.set(i, row);
        }
        databaseStructures.databaseData.put(tableName, tableData);
        //write the updated value in db
        databaseStructures.storeDatabase("update", tableName);

        // release the lock
        lockManager.releaseLock(databaseName, tableName);

        return "Successfully Updated "+3+" rows";
    }
}
