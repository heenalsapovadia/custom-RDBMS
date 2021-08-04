package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogGenerator {

    String pathToLogFile = "src/main/java/logger/logs.txt";

    public void log (String message) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToLogFile, true));
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch (IOException io) {
            System.out.println("Failed to write to Logs : "+io.getMessage());
        }
    }
}
