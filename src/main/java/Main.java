import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        System.out.println("Welcome to RDBMS 5408 DP4");

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
        System.out.print("USERNAME : ");
        userName = sc.next();
        System.out.print("PASSWORD : ");
        password = sc.next();

        User user = User.login(userName, password);
        if(user!=null) System.out.println("User loaded");
        /*

         */

    }
}
