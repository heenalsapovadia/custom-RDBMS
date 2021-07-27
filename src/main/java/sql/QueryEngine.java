package sql;

import java.util.Scanner;

public class QueryEngine {
    String database;
    String userName;

    public QueryEngine() {}

    public void run(String database, String userName) {
        this.userName = userName;
        this.database = database;

        String query = inputQuery();


    }

    public String inputQuery() {
        System.out.println("Enter your SQL query");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        return query;
    }
}
