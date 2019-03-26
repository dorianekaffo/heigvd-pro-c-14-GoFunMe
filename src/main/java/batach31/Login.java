package batach31;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    //connecting to database
    //password: root
    //port: 5432
    //french: switzerland

    private final String url = "jdbc:postgresql://localhost:5432/example"; //database name here is example
    private final String user = "postgres"; //superuser
    private final String password = "root";

    private Connection connect() {
        /*try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    private int hash(String passwordToHash){
        int hash = 7;
        for (int i = 0; i < passwordToHash.length(); i++) {
            hash = hash*31 + passwordToHash.charAt(i);
        }
        return hash;
    }

    public void register(String username, String userPassword){
        Connection co = connect();
        try {
            Statement statement = co.createStatement();
            statement.executeQuery("INSERT INTO USEREXAMPLE (UserName, PasswordHash) VALUES ('" + username + "'," + hash(userPassword) + ");");
            System.out.println("Inserted " + username + " " + hash(userPassword));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //will open the user's page and connect to the server later on
    public void log(String username, String userPassword){
        Connection co = connect();

        ResultSet registeredPassword;
        int pwToTest = 0;
        int hash = hash(userPassword);
        try{
            Statement statement = co.createStatement();
            registeredPassword = statement.executeQuery("SELECT PasswordHash FROM USEREXAMPLE WHERE UserName = '" + username + "';");
            registeredPassword.next();
            pwToTest = registeredPassword.getInt("PasswordHash");
        }catch (SQLException e){
            e.printStackTrace();
        }

        if(pwToTest == hash){
            System.out.println("Connecting...");
        }else{
            System.out.println("Connection failed...");
        }

    }


    public static void main(String[] args) {
        Login log = new Login();
        //log.register("blah", "bloup");
        log.log("blah", "bloup");


    }

}
