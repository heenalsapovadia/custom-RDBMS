package sql;

import databaseFiles.DatabaseStructures;
import sql.processor.CreateProcessor;
import sql.processor.DeleteProcessor;
import logger.LogGenerator;
import sql.processor.SelectProcessor;
import sql.processor.UpdateProcessor;

import java.time.Duration;
import java.time.Instant;
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
        //temp code
//        databaseStructures.databaseName = "company"; // only for testing

        while (true) {
            String query = inputQuery();
            // log - query submission time
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
                String message;
                // call sql query execution
                switch (queryType) {
                    case "use" :
                        // load tableData
                    case "create" :
                        queryObj = queryParser.createParser(query);
                        CreateProcessor createProcessor = new CreateProcessor();
                        createProcessor.create(queryObj, databaseStructures);
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

                    case "update" :
                        queryObj = queryParser.updateParser(query);
                        UpdateProcessor updateProcessor = new UpdateProcessor();
                        message = updateProcessor.process(queryObj, databaseStructures);
                        logGenerator.log(queryType);
                        end = Instant.now();
                        logGenerator.log(message);
                        logGenerator.log("Time Elapsed : "+Duration.between(start, end)+"\n");
                        break;

                    case "delete" :
                        queryObj = queryParser.deleteParser(query);
                        DeleteProcessor deleteProcessor = new DeleteProcessor();
                        deleteProcessor.delete(queryObj, databaseStructures);
                        break;
                }

            }
            System.out.println("--------------END OF QUERY------------");
        }
    }

    public String inputQuery() {
        System.out.println("Enter your SQL query");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        return query;
    }
}
