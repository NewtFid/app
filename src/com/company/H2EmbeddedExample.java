package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2EmbeddedExample {
    public static void main(String[] args) throws SQLException {
        String jdbcURL = "jdbc:h2:./test";
        String username = "sa";
        String password = "1234";

        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to H2 embedded database.");

        Statement statement = connection.createStatement();
        try {
            statement.execute("create Table cities(name varChar(32))");
            statement.execute("INSERT INTO cities values('London') ");
            statement.execute("INSERT INTO cities values('Saint-Petersburg') ");
            statement.execute("INSERT INTO cities values('Moscow') ");
            statement.execute("INSERT INTO cities values('Kemerovo') ");


        }
        catch (org.h2.jdbc.JdbcSQLSyntaxErrorException e){ }

        ResultSet resultSet = statement.executeQuery("Select * FROM cities");

        int count = 0;

        while (resultSet.next()) {
            count++;
            String name = resultSet.getString("name");
            System.out.println("City:  " + name);
        }

        connection.close();
    }
}
