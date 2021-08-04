package SQLDump;

import java.io.*;

public class SQLDumpGenerator {

    public String pathToSQLDumps = "src/main/java/SQLDump";
    String pathToDatabases = "src/main/java/databaseFiles";

    public void generateDumps (String database) {
        String pathToDbDir = pathToDatabases+"/"+database;
        String pathToMetadata = pathToDbDir+"/METADATA.txt";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToSQLDumps+"/"+database+"SQLDump.txt"));
            bufferedWriter.write("create database "+database);
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToMetadata));
            String line = bufferedReader.readLine();
            while (line!=null) {
                // create table query
                bufferedWriter.write(createTableQuery(line));
                bufferedWriter.newLine();
                line = bufferedReader.readLine();
            }
            // Calling insert queries generator
            bufferedWriter.write(insertQuery(database));
            bufferedWriter.close();
            System.out.println("SQL DUMP has been generated - You can access it at 'src/main/java/SQLDump'");
        }
        catch (IOException io) {
            System.out.println("IO Exception in reading file for SQL DUmp generation : "+io.getMessage());
        }
    }

    /*
    Generate Create table query
     */
    public String createTableQuery (String metadata) {
        StringBuilder queryBuilder = new StringBuilder("create table ");
        String[] parts = metadata.replaceAll("\\s+", "").split("=");
        String tableName = parts[0];
        queryBuilder.append(tableName + " ( ");
        String[] colsAndKeys = parts[1].replaceAll("\\{", "")
                .replaceAll("\\}", "")
                .split("\\|");
        String[] columns = colsAndKeys[0].split(",");
        for (int i=0; i<columns.length; i++) {
            String[] columnAndDatatype = columns[i].split(":");
            queryBuilder.append(columnAndDatatype[0]+" "+columnAndDatatype[1]);
            if (i!=columns.length-1)
                queryBuilder.append(", ");
        }

        if (colsAndKeys.length>1) {
            String[] pkAndFk = colsAndKeys[1].split("-");
            String[] part1 = pkAndFk[0].split(":");
            if (part1[0].equals("PK")) {
                queryBuilder.append(", ");
                queryBuilder.append("primary key (");
                queryBuilder.append(part1[1]+")");
            }
            if (part1[0].equals("FK") || (pkAndFk.length>1 && pkAndFk[1].split(":")[0].equals("FK"))) {
                String FKSet = "";
                if (part1[0].equals("FK")){
                    FKSet = part1[1].replaceAll("\\(", "").replaceAll("\\)", "");
                }
                else {
                    FKSet = pkAndFk[1].split(":")[1].replaceAll("\\(", "").replaceAll("\\)", "");
                }
                String[] fkPairs = FKSet.split(",");
                for (int i=0; i<fkPairs.length; i++) {
                    queryBuilder.append(", ");
                    queryBuilder.append("foreign key (");
                    String[] fkCols = fkPairs[i].split("\\?");
                    queryBuilder.append(fkCols[0]+") ");
                    queryBuilder.append("references "+fkCols[1]+"("+fkCols[2]+")");
                }
            }
        }
        queryBuilder.append(" ) ");
        return queryBuilder.toString();
    }

    /*
    Generate Insert query
     */
    public String insertQuery (String database) {
        StringBuilder queryBuilder = new StringBuilder();
        File dir = new File(pathToDatabases+"/"+database);
        for (File table : dir.listFiles()) {
            String tableName = table.getName().split("\\.")[0];
            if (tableName.equals("METADATA"))
                continue;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(table));
                String[] columns = bufferedReader.readLine().split("\\|");
                String line = bufferedReader.readLine();
                while (line!=null) {
                    String[] rowValues = line.split("\\|");
                    StringBuilder insert = new StringBuilder();
                    insert.append("insert into ");
                    insert.append(tableName+" (");
                    for (int i=0; i<columns.length; i++) {
                        insert.append(columns[i]);
                        if (i!=columns.length-1) insert.append(", ");
                    }
                    insert.append(") values (");
                    for (int i=0; i<rowValues.length; i++) {
                        insert.append(rowValues[i]);
                        if (i!=rowValues.length-1) insert.append(", ");
                    }
                    insert.append(")\n");
                    queryBuilder.append(insert);
                    line = bufferedReader.readLine();
                }
            }
            catch (IOException io) {
                System.out.println("Exception in reading table file : "+io.getMessage());
            }
        }
        return queryBuilder.toString();
    }
}
