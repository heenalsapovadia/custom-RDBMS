package sql;

import sql.processor.SelectProcessor;

import java.util.Scanner;

public class QueryEngine {
    String database;
    String userName;

    public QueryEngine() {}

    public void run(String database, String userName) {
        this.userName = userName;
        this.database = database;
        QueryValidator queryValidator = new QueryValidator();
        String queryType;

        while (true) {
            String query = inputQuery();
            queryType = queryValidator.getQueryType(query);
            if (queryType.equals("INVALID")) {
                System.out.println("INVALID SQL QUERY ENTERED...try again");
                continue;
            }
            else if (queryType.equals("exit")){
                break;
            }
            else if (!queryType.equals("use")) {
                System.out.println("Please select database - 'use <database_name>'"); // can add 'show databases'
                continue;
            }
            else {
                //call sql parsers
//                queryValidator.parseQuery(queryType, query);
                QueryParser queryParser = new QueryParser();
                Query queryObj = new Query();
                // call sql query execution
                switch (query) {
                    case "create" :
//                queryParser.createParser(query);

                    case "select" :
                        queryObj = queryParser.selectParser(query);
                        SelectProcessor selectProcessor = new SelectProcessor();
//                        selectProcessor.process();

                    case "insert" :
                    case "update" :
                    case "delete" :
                }

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
