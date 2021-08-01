package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateProcessor {
    LockManager lockManager = new LockManager();

    public void process (Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // check for locks and apply lock
        lockManager.getLocksFromFile();
        List<String> tbLocks = new ArrayList<>();
        if (lockManager.locks.containsKey(databaseName)) {
            tbLocks = lockManager.locks.get(databaseName);
            if(tbLocks.contains(tableName)) {
                System.out.println("Table : "+tableName+" is locked! Please try again after some time");
                return;
            }
        }
        tbLocks.add(tableName);
        lockManager.locks.put(databaseName, tbLocks);
        lockManager.updateLocksToFile();

        Map<String, String> optionsMap = queryObj.getOptionMap();
        Map<String, String> conditionMap = queryObj.getConditionMap();

        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);

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
                row.put(entry.getKey(), entry.getValue());
            }
            tableData.set(i, row);
        }
        databaseStructures.databaseData.put(tableName, tableData);
        //write the updated value in db
        databaseStructures.storeDatabase("update", "employee");

        // release the lock
        lockManager.getLocksFromFile();
        tbLocks = lockManager.locks.get(databaseName);
        if (tbLocks.isEmpty()) {
            lockManager.locks.remove(databaseName);
        }
        else {
            lockManager.locks.put(databaseName, tbLocks);
        }
        lockManager.updateLocksToFile();
    }
}
