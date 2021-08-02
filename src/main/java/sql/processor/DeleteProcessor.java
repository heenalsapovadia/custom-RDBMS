package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.LockManager;
import sql.Query;

import java.util.List;
import java.util.Map;

public class DeleteProcessor {
    LockManager lockManager = new LockManager();
    public void delete(Query queryObj, DatabaseStructures databaseStructures) {
        String databaseName = databaseStructures.databaseName;
        String tableName = queryObj.getTableName();

        // Checking for the lock manager flag
        if (!lockManager.checkAndApplyLock(databaseName, tableName)) {
            return;
        }
        //TODO: Get option and condition map
        Map<String, String> optionsMap = queryObj.getOptionMap();
        Map<String, String> conditionMap = queryObj.getConditionMap();

        //TODO: Get table data and Primary key
        List<Map<String, String>> tableData = databaseStructures.databaseData.get(tableName);
        String primaryKeyColumn = databaseStructures.primaryKeyMap.get(tableName);


        //TODO: DELETE LOGIC -- FOR TABLE
        //  PUT A LOCK
        //  CHECK FOR FOREIGN KEY CONSTRAINTS
        //  DELETE FROM METADATA
        //  DELETE FROM THE TXT FILE
        //  REVOKE THE LOCK



        //TODO: DELETE LOGIC -- FOR COLUMN
        // PUT A LOCK
        // CHECK FOR PRIMARY KEY
        // UPDATE THE METADATA
        // REWRITE THE TXT FILE
        // REVOKE THE LOCK


        //TODO: GLOBAL LOCK CLOSE

    }
}
