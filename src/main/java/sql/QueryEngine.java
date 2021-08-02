package sql;

import databaseFiles.DatabaseStructures;
import sql.processor.CreateProcessor;
import sql.processor.DeleteProcessor;
import sql.processor.SelectProcessor;
import sql.processor.UpdateProcessor;

import java.util.Scanner;

public class QueryEngine {
    String database;
    String userName;

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
            queryType = queryValidator.getQueryType(query);
            if (queryType.equals("INVALID")) {
                System.out.println("INVALID SQL QUERY ENTERED...try again");
                continue;
            }
            else if (queryType.equals("exit")){
                return;
            }
            else if (!queryType.equals("use") && databaseStructures.databaseName.isEmpty()) {
                System.out.println("Please select database - 'use <database_name>'"); // can add 'show databases'
                continue;
            }
            else {
                //call sql parsers
                QueryParser queryParser = new QueryParser();
                Query queryObj = new Query();
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
//                        databaseStructures.loadDatabase("company"); // only for testing
                        queryObj = queryParser.selectParser(query);
                        SelectProcessor selectProcessor = new SelectProcessor();
                        selectProcessor.process(queryObj, databaseStructures);
                        break;

                    case "insert" :

                    case "update" :
                        queryObj = queryParser.updateParser(query);
                        UpdateProcessor updateProcessor = new UpdateProcessor();
                        updateProcessor.process(queryObj, databaseStructures);
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
