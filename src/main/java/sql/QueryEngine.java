package sql;

import databaseFiles.DatabaseStructures;
import sql.processor.*;
import logger.LogGenerator;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class QueryEngine {
    String database;
    String userName;
    LogGenerator logGenerator = new LogGenerator();
    Duration timeElapsed;
    Instant end;
    Instant start;

    public QueryEngine() {}

    public void run(String database, String userName) {
        this.userName = userName;
        this.database = database;
        DatabaseStructures databaseStructures = new DatabaseStructures();

        QueryValidator queryValidator = new QueryValidator();
        String queryType;

        while (true) {
            String query = inputQuery();
            start = Instant.now();
            logGenerator.log("Query Submission TimeStamp : "+start);
            queryType = queryValidator.getQueryType(query);

            if (queryType.equals("INVALID")) {
                System.out.println("INVALID SQL QUERY ENTERED...try again");
                end = Instant.now();
                logGenerator.log("** INVALID SQL QUERY **");
                logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                continue;
            }
            else if (queryType.equals("exit")){
                // log - time elapsed
                end = Instant.now();
                logGenerator.log("EXIT");
                logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                return;
            }
            else if (!queryType.equals("use") && databaseStructures.databaseName.isEmpty()) {
                System.out.println("Please select database - 'use <database_name>'"); // can add 'show databases'
                end = Instant.now();
                logGenerator.log("** DATABASE NOT SELECTED **");
                logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                continue;
            }
            else {
                //call sql parsers
                QueryParser queryParser = new QueryParser();
                Query queryObj = new Query();
                String message="";
                switch (queryType) {
                    case "use" :
                        // load tableData
                        queryObj = queryParser.useParser(query);
                        UseProcessor useProcessor = new UseProcessor();
                        String databaseName = useProcessor.process(queryObj, databaseStructures);
                        databaseStructures.loadDatabase(databaseName);
                        message = "Database set : "+databaseName;
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(message);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "create" :
                        queryObj = queryParser.createParser(query);
                        CreateProcessor createProcessor = new CreateProcessor();
                        if(queryObj.getType().equals("database")){
                            message = createProcessor.createdb(queryObj, databaseStructures);
                            logGenerator.log(queryType);
                            end = Instant.now();
                            logGenerator.log(message);
                        }
                        if(queryObj.getType().equals("table")){
                            message = createProcessor.createtable(queryObj,databaseStructures);
                            System.out.println("Table Created");
                            logGenerator.log(queryType);
                            end = Instant.now();
                            logGenerator.log(message);
                        }

                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "select" :
                        queryObj = queryParser.selectParser(query);
                        SelectProcessor selectProcessor = new SelectProcessor();
                        message = selectProcessor.process(queryObj, databaseStructures);
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(message);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "insert" :
                        queryObj = queryParser.insertParser(query);
                        InsertProcessor insertProcessor = new InsertProcessor();
                        DatabaseStructures insertedStructures = insertProcessor.process(queryObj,databaseStructures);
                        if (insertedStructures!=null) {
                            insertedStructures.pushDatabaseData(queryObj.getTableName());
                        }
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(insertProcessor.logMessage);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "update" :
                        queryObj = queryParser.updateParser(query);
                        UpdateProcessor updateProcessor = new UpdateProcessor();
                        DatabaseStructures updatedStructures = updateProcessor.process(queryObj, databaseStructures);
                        if (updatedStructures!=null) {
                            updatedStructures.pushDatabaseData(queryObj.getTableName());
                        }
                        System.out.println("Insider logs : "+updateProcessor.logMessage);
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(updateProcessor.logMessage);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "delete" :
                        queryObj = queryParser.deleteParser(query);
                        DeleteProcessor deleteProcessor = new DeleteProcessor();
                        message = deleteProcessor.process(queryObj, databaseStructures);
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(message);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "start" :
                        logGenerator.log("START TRANSACTION");
                        List<String> queries = queryParser.transactionParser(query);
                        if (queries == null) {
                            // handle error cases
                            break;
                        }
                        TransactionProcessor transactionProcessor = new TransactionProcessor();
                        transactionProcessor.process(queries, databaseStructures);
                        logGenerator.log("END OF TRANSACTION");
                        end = Instant.now();
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;
                }
                System.out.println(message+"\n");
            }

        }
    }

    public String inputQuery() {
        System.out.println("Enter your SQL query");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        return query;
    }
}
