import SQLDump.SQLDumpGenerator;
import databaseFiles.DatabaseStructures;
import erdGenerator.ERDGenerator;
import sql.QueryEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        displayHeader();

        /*
        Load Database and check its connection
        connectionToDb()
        handle database loading error
         */

        /*
        Take user credentials and authenticate
        userLogin()
        handle invalid credentials error
         */
        String userName;
        String password;
        Scanner sc = new Scanner(System.in);
        User user;
        while(true) {
            System.out.print("USERNAME : ");
            userName = sc.next();
            System.out.print("PASSWORD : ");
            password = sc.next();

            user = User.login(userName, password);

            if (user == null) {
                System.out.println("Invalid credentials");
                System.out.println("Enter 1 to Retry or Any key to Exit");
                if(sc.hasNextInt()) {
                    int option = sc.nextInt();
                    if(option == 1 ) continue;
                }
                else {
                    System.out.println("Exiting the system...");
                    System.exit(0);
                }
            }
            else {
                break;
            }
        }

        List<String> userMenuOptions = Arrays.asList("Execute SQL Query",
                "Generate SQL Dump",
                "Generate ERD",
                "Generate Data Dictionary",
                "Logout",
                "Exit");
        while (true) {
            DatabaseEngine databaseEngine = new DatabaseEngine();
            String database;
            int option = printSelection(userMenuOptions);

            switch (option) {
                case 1:
                    // sql query input
                    //                DatabaseEngine databaseEngine = new DatabaseEngine();
                    //                String database = databaseEngine.selectDatabase();
                    QueryEngine queryEngine = new QueryEngine();
                    queryEngine.run("", user.getUserName());
                case 2:
                    // sql dump code
                    database = databaseEngine.selectDatabase();
                    SQLDumpGenerator sqlDumpGenerator = new SQLDumpGenerator();
                    sqlDumpGenerator.generateDumps(database);
                case 3:
                    // erd generation
                    database = databaseEngine.selectDatabase();
                    DatabaseStructures databaseStructures = new DatabaseStructures();
                    databaseStructures.loadDatabase(database);
                    ERDGenerator erdGenerator = new ERDGenerator();
                    erdGenerator.generateERD(database, databaseStructures);
                case 4:
                    // data dictionary
                case 5:
                    //logout code
                case 6:
                    //exit
                    System.out.println("Exiting System...");
                    return;
            }
        }
    }

    public static void displayHeader(){
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to RDBMS 5408 DP4");
        System.out.println("-----------------------------------------------------");
    }

    public static int printSelection(List<String> selectionOptions) {
        int selected = -1;
        Scanner scanner = new Scanner(System.in);
        while (true) {

            for (int option = 0; option < selectionOptions.size(); option++) {
                System.out.println((option + 1) + "." + " "
                        + selectionOptions.get(option));
            }
            System.out.print("Enter your option : ");

            if (scanner.hasNextInt()) {
                selected = scanner.nextInt();
                if (selected >= selectionOptions.size() || selected < 0) {
                    System.out.println("INVALID SELECTION");
                    continue;
                }
                else return selected;
            } else {
                System.out.println("INVALID SELECTION");
                continue;
            }

        }
    }
}
