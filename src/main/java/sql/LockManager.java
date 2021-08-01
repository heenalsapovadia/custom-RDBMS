package sql;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockManager {
    public Map<String, List<String>> locks = new HashMap<>();
    String pathToLocks = "src/main/java/databaseFiles/locks.txt";

    public void updateLocksToFile () {
        try {
            if (locks.isEmpty()) {
                System.out.println("lock empty");
                FileWriter fileWriter = new FileWriter(pathToLocks, false);
                fileWriter.close();
            }
            else {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToLocks));

                for (Map.Entry<String, List<String>> lock : locks.entrySet()) {
                    String database = lock.getKey();
                    List<String> tables = lock.getValue();
                    StringBuilder line;
                    for (String table : tables) {
                        line = new StringBuilder();
                        line.append(database);
                        line.append(":");
                        line.append(table);
                        bufferedWriter.write(line.toString());
                        bufferedWriter.newLine();
                    }
                }
                bufferedWriter.close();
            }
        }
        catch (IOException io) {
            System.out.println("Exception in writing Locks file : "+io.getMessage());
        }
    }

    public void getLocksFromFile () {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToLocks));
            String line = bufferedReader.readLine();
            locks = new HashMap<>();
            while (line!=null) {
                String[] parts = line.split(":");
                List<String> keys = locks.getOrDefault(parts[0], new ArrayList<>());
                keys.add(parts[1]);
                locks.put(parts[0], keys);

                line = bufferedReader.readLine();
            }
        }
        catch (IOException io) {
            System.out.println("Exception in reading Locks file : "+io.getMessage());
        }
    }
}
