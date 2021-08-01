import java.io.BufferedReader;
import java.io.FileReader;

public class User {
    private String userName;
    private String password;

    User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public static User login(String userName, String password){
        String path = "/home/heenal/DAL/DW/Project/csci-5408-dp-4/credentials.txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            while(line != null){
                String[] creds = line.split("\\|");
                if(creds[0].equals(userName) && creds[1].equals(password)) {
                    System.out.println("LOGIN Successful");
                    return new User(userName, password);
                }
                line = bufferedReader.readLine();
            }
        }
        catch (Exception fe){
            System.out.println("CREDENTIALS FILE READ ERROR : "+fe.getMessage());
        }
        return null;
    }
}
