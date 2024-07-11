package com.by.chaplygin.demo.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor

public class JdbcRequest {
    @Value("spring.datasource.username")
    private  String DB_USERNAME;
    @Value("spring.datasource.password")
    private  String DB_PASSWORD;

    private Connection getConn(){
        Connection connection;
        try {
             connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/to-do", "postgres", "postgres");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public int getCountOfPartiesInCityAndDateJDBC(String city) throws SQLException {
        try (Connection connection = getConn();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Party WHERE city = ? AND date_ofCreate = ?")) {

            preparedStatement.setString(1, city);
            preparedStatement.setObject(2, Timestamp.valueOf(LocalDateTime.now()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
}
