package app.server.utils;

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DBUtils {
    // Declare JDBC Driver
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // Connection
    private static Connection conn = null;

    // Login data
    private static String username = "root";
    private static String pass = "";
    private static String ip = "localhost";
//    private static String port = "3306";
    private static String sid = "BazaAnkieta_marcin_zielinski";


    // Flag if DB exists
    private static boolean dbExists = false;

    //
    private static final String DATABASE_STRUCTURE_FILE_PATH = "bazaAnkiety.sql";
    private static final String QUESTIONS_FILE_PATH = "bazapytan.txt";


    public static void dbConnect() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj ip bazy: ");
        setIp(sc.nextLine());

//        System.out.println("Podaj port bazy: ");
//        setPort(sc.nextLine());

        System.out.println("Podaj nazwe bazy: ");
        setSid(sc.nextLine());

        System.out.println("Podaj login do bazy: ");
        setUsername(sc.nextLine());

        System.out.println("Podaj haslo do bazy: ");
        setPass(sc.nextLine());

        // setting Oracle driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.printf("Cannot find JDBC driver!");
            e.printStackTrace();
        }
        System.out.println("JDBC Driver Registered!");

        // Establish the MySQL Connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+ ip +"/", username, pass);
            System.out.println("Connected to localhost/");
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }

        // Check if DB exists
        ResultSet ifDBExistResultSet;
        try {
            ifDBExistResultSet = dbExecuteQuery(String.format("SHOW DATABASES LIKE '%s' ",sid));
            if (ifDBExistResultSet.next()) {
                dbExists = true;
            } else {
                dbExists = false;
            }
        } catch (SQLException e) {
            System.err.println("Error while checking if database exists");
            e.printStackTrace();
        }

        // Create DB if doesn't exists
        if (!dbExists) {
            try {
                dbExecuteUpdate(String.format("CREATE DATABASE %s", sid));
                dbExecuteUpdate(String.format("USE %s", sid));
                createDBStructure();
            } catch (SQLException e) {
                System.err.println("Error while creating database");
                e.printStackTrace();
            }
        }

        // choose proper DB
        try {
            dbExecuteUpdate(String.format("USE %s", sid));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDBStructure() throws SQLException {
        // Import file with db structure
        File file = new File(DATABASE_STRUCTURE_FILE_PATH);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find file with db structure");
            e.printStackTrace();
        }

        String dbStructureSQL;

        // execute sql statements one-by-one
       while (sc.hasNext()) {
           dbStructureSQL = sc.useDelimiter(";").next();
         //  System.out.println(dbStructureSQL);
           if (!dbStructureSQL.isEmpty()) dbExecuteUpdate(dbStructureSQL);
       }
    }

    // Close connection
    public static void dbDisconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Disconnected!");
            }
        } catch (SQLException e) {
            System.err.println("Error while closing the connection!");
            e.printStackTrace();
        }
    }

    // DB Execute Query Operation (select)
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet cachedRowSet = null;

        try {
            // Connect to DB
          //  System.out.println("Select statement: " + queryStmt);

            // Create statement
            stmt = conn.createStatement();

            // Execute operation
            resultSet = stmt.executeQuery(queryStmt);

            // Cached Row Set Implementation
            // In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next error
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.populate(resultSet);


        } catch (SQLException e) {
            System.err.println("Error at executing query");
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }
        return cachedRowSet;
    }


    public static void dbExecuteUpdate(String sqlStmt) throws SQLException {
        Statement stmt = null;

        try {
          //  System.out.println(sqlStmt);
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.err.println("Error while executing update statement");
            e.printStackTrace();
        } finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    public static void fillDatabase() {
        // Set file with questions
        File file = new File(QUESTIONS_FILE_PATH);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file with questions");
            e.printStackTrace();
        }

        int numberOfQuestions = sc.nextInt();
        sc.nextLine(); //end line ?

        for (int i = 0; i < numberOfQuestions; i++) {
            String question = sc.nextLine();                // read question
            try {
                String stmt = String.format("INSERT INTO pytania (id_pytania, tresc_pytania) VALUES (%s, \"%s\")" +
                        "ON DUPLICATE KEY UPDATE id_pytania=%s, tresc_pytania=\"%s\";",i+1, question, i+1, question);
                dbExecuteUpdate(stmt);
            } catch (SQLException e) {
                System.err.println("Error while inserting question to db");
                e.printStackTrace();
            }

            int numberOfAnswers = sc.nextInt();             // read answers
            sc.nextLine();
            for (int j = 0; j < numberOfAnswers; j++) {
                String answer = sc.nextLine();
                try {
                    String stmt2 = String.format("INSERT INTO odpowiedzi (id_pytania, id_odpowiedzi, tresc_odpowiedzi) " +
                            "VALUES (%s, %s, \"%s\") ON DUPLICATE KEY UPDATE id_pytania=%s, " +
                            "id_odpowiedzi=%s, tresc_odpowiedzi= \"%s\";",i+1, j+1, answer,i+1, j+1, answer);
               // System.out.println(stmt2);
                    dbExecuteUpdate(stmt2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
              //  System.out.println("Odpowiedz: " + answer);
            }

        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DBUtils.username = username;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        DBUtils.pass = pass;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        DBUtils.ip = ip;
    }

//    public static String getPort() {
//        return port;
//    }
//
//    public static void setPort(String port) {
//        DBUtils.port = port;
//    }

    public static String getSid() {
        return sid;
    }

    public static void setSid(String sid) {
        DBUtils.sid = sid;
    }
}
