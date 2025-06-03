package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String realPassword = rs.getString("password");
                    return password.equals(realPassword);
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при аутентификации: " + e.getMessage());
        }
        return false;
    }
}
