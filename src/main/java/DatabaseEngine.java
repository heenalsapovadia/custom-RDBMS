import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEngine {

    public DatabaseEngine() {}

    public List<String> fetchDatabaseList() {
        String path = "/home/heenal/DAL/DW/Project/csci-5408-dp-4/src/main/java/databaseFiles/databases.txt";
        List<String> databaseList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            while(line != null){
                databaseList.add(line);
                line = bufferedReader.readLine();
            }
        }
        catch (Exception fe){
            System.out.println("Databases FILE READ ERROR : "+fe.getMessage());
        }
        return databaseList;
    }

    public String selectDatabase() {
        List<String> databaseList = fetchDatabaseList();
        if (databaseList.isEmpty()) {
            System.out.println("No Databases available...");
            return null;
        }
        int databaseOption = Main.printSelection(databaseList);
        return databaseList.get(databaseOption-1);
    }
}
