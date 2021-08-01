package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.Query;

import java.util.List;
import java.util.Map;

public class UpdateProcessor {
    public void process (Query queryObj, DatabaseStructures databaseStructures) {
        String tableName = queryObj.getTableName();
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
    }
}
