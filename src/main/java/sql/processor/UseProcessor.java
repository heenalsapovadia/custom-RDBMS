package sql.processor;

import databaseFiles.DatabaseStructures;
import sql.Query;

import java.util.*;

public class UseProcessor {
    private String database = null;

    public String getDatabase() {
        return database;
    }
    public String process (Query queryObj, DatabaseStructures databaseStructures) {

        //this.username = username;
        this.database = database;
        String newDatabase = (String) queryObj.getDatabaseName();


        databaseStructures.databaseName = newDatabase;
        System.out.println("Using Database: "+databaseStructures.databaseName);
        return databaseStructures.databaseName;

    }
}
