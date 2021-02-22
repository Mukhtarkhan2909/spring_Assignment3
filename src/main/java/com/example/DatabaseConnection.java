package com.example;

import org.springframework.stereotype.Component;
import java.sql.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Customer";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "qweasz11";
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String sql = "SELECT * FROM \"Users\"";

    @PostConstruct
    public void myInit() throws SQLException {
        System.out.println("Doing my initialization");
        System.out.println("Connecting to database");
        System.out.println("Creating statement");
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultSet = statement.executeQuery(sql);
    }
    @PreDestroy
    public void myDestroy() throws SQLException {
        System.out.println("Doing my destruction");
        System.out.println("Closing statement");
        System.out.println("Closing database");
        resultSet.close();
        statement.close();
        connection.close();
    }
    public void updateBalance(int balance, String name) throws SQLException {
        String sql = "UPDATE \"Users\"" +
                "SET user_balance = " + balance +
                "WHERE user_name = '" + name + "';";
        int r = statement.executeUpdate(sql);
        if (r > 0) {
            System.out.println("Balance updated");
        } else {
            System.out.println("Balance is not updated");
        }
    }
    public void beforeFirst() throws SQLException {
        resultSet.beforeFirst();
    }
    public ResultSet getResultSet() {
        return resultSet;
    }
}
