package sql.processor;

import databaseFiles.DatabaseStructures;
import logger.LogGenerator;
import sql.Query;
import sql.QueryParser;
import sql.QueryValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionProcessor {

    QueryValidator queryValidator = new QueryValidator();
    QueryParser queryParser = new QueryParser();
    StringBuilder transactionLog = new StringBuilder();
    LogGenerator logGenerator = new LogGenerator();
    String errorQuery;

    public void process (List<String> queries, DatabaseStructures databaseStructures) {
        String queryType = "";
        DatabaseStructures transactionStructures = new DatabaseStructures();
        transactionStructures.databaseName = databaseStructures.databaseName;
        transactionStructures.tableStructures.putAll(databaseStructures.tableStructures);
        transactionStructures.primaryKeyMap.putAll(databaseStructures.primaryKeyMap);
        transactionStructures.foreignKeyMap.putAll(databaseStructures.foreignKeyMap);
        transactionStructures.databaseData.putAll(databaseStructures.databaseData);

        Set<String> tablesUpdated = new HashSet<>();
        for (String query : queries) {
            if (transactionStructures == null) {
                // found error in one of the queries
                logGenerator.log("** ERROR in "+errorQuery);
                return;
            }
            Query queryObj = new Query();
            queryType = queryValidator.getQueryType(query);
            switch (queryType) {
                case "INVALID":
                    break;
                case "insert":
                    queryObj = queryParser.insertParser(query);
                    InsertProcessor insertProcessor = new InsertProcessor();
                    transactionStructures = insertProcessor.process(queryObj,databaseStructures);
                    if (transactionStructures==null) {
                        errorQuery = "INSERT";
                        break;
                    }
                    transactionLog.append("INSERT : "+insertProcessor.logMessage+"\n");
                    tablesUpdated.add(queryObj.getTableName());
                    break;
                case "update":
                    queryObj = queryParser.updateParser(query);
                    UpdateProcessor updateProcessor = new UpdateProcessor();
                    transactionStructures = updateProcessor.process(queryObj, databaseStructures);
                    if (transactionStructures==null) {
                        errorQuery = "UPDATE";
                        break;
                    }
                    transactionLog.append("UPDATE : "+updateProcessor.logMessage+"\n");
                    tablesUpdated.add(queryObj.getTableName());
                    break;

                case "delete":
                    queryObj = queryParser.deleteParser(query);
                    DeleteProcessor deleteProcessor = new DeleteProcessor();
                    DatabaseStructures deletedStructures = deleteProcessor.process(queryObj, databaseStructures);
                    if (deletedStructures==null) {
                        errorQuery = "DELETE";
                        break;
                    }
                    transactionLog.append("DELETE : "+deleteProcessor.logMessage+"\n");
                    tablesUpdated.add(queryObj.getTableName());
                    break;

                case "rollback":
                    transactionStructures = new DatabaseStructures();
                    logGenerator.log("** TRANSACTION ROLLBACK **");
                    return;
                case "commit":
                    for (String table : tablesUpdated) {
                        transactionStructures.pushDatabaseData(table);
                    }
                    transactionLog.append("TRANSACTION COMMIT");
                    logGenerator.log(transactionLog.toString());
            }
        }
    }














}
