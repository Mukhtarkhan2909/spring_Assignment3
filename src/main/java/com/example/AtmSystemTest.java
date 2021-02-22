package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AtmSystemTest {
    public static void main(String[] args) throws SQLException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.example");
        context.refresh();
        DatabaseConnection connection = context.getBean("databaseConnection",
                DatabaseConnection.class);
        UserList list = context.getBean("userList", UserList.class);
        Scanner s = new Scanner(System.in);
        ResultSet resultSet = connection.getResultSet();
        String userName;
        int userCardNumber;
        int userPinCode;
        int userBalance;
        while (resultSet.next()) {
            Users users = context.getBean("users", Users.class);
            users.setUserName(resultSet.getString("user_name"));
            users.setUserCardNumber(resultSet.getInt("user_card_number"));
            users.setUserPinCode(resultSet.getInt("user_pin_code"));
            users.setUserBalance(resultSet.getInt("user_balance"));
            list.addUser(users);
        }
        loop: while (true) {
            connection.beforeFirst();
            System.out.println("Enter your username");
            String name = s.next();
            userName = null;
            userCardNumber = 0;
            userPinCode = 0;
            userBalance = 0;
            while (resultSet.next()) {
                if (resultSet.getString("user_name").equalsIgnoreCase(name)) {
                    userName = resultSet.getString("user_name");
                    userCardNumber = resultSet.getInt("user_card_number");
                    userPinCode = resultSet.getInt("user_pin_code");
                    userBalance = resultSet.getInt("user_balance");
                    break;
                }
            }
            if (userName != null) {
                System.out.println("Enter the card number");
                int cardNumber = s.nextInt();
                System.out.println("Enter the pin code");
                int pinCode = s.nextInt();
                if (userCardNumber == cardNumber && userPinCode == pinCode) {
                    System.out.println("\t***Choose operation***\n" +
                            "\n1. Check balance" +
                            "\n2. Withdraw" +
                            "\n3. Top up" +
                            "\n4. Exit");
                    int ch = s.nextInt();
                    switch (ch) {
                        case 1:
                            System.out.println("Your balance is: " + userBalance);
                            break;
                        case 2:
                            System.out.println("Enter withdraw amount");
                            int withdraw = s.nextInt();
                            if (withdraw <= userBalance) {
                                connection.updateBalance(userBalance - withdraw, userName);
                            } else {
                                System.out.println("Your balance is less than " + withdraw);
                            }
                            System.out.println("Now your balance is: " + userBalance);
                            break;
                        case 3:
                            System.out.println("Enter top up amount");
                            int topUp = s.nextInt();
                            connection.updateBalance(userBalance + topUp, userName);
                            System.out.println("Now your balance is: " + userBalance);
                            break;
                        case 4:
                            break loop;
                    }
                } else {
                    System.out.println("Pin code or password is incorrect");
                }
            } else {
                System.out.println("Account not found");
            }
        }
        for (Users user: list.getUsers()) {
            System.out.println(user);
        }
        context.close();
    }
}
