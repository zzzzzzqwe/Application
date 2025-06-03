package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {

    public static String getAllTeachers() {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT 
                CONCAT(t.last_name, ' ', t.first_name) AS teacher,
                t.email,
                t.phone,
                COALESCE(t.room_number, '-') AS room_number
            FROM teachers t
            ORDER BY t.last_name, t.first_name;
        """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String teacher = rs.getString("teacher");
                String email   = rs.getString("email");
                String phone   = rs.getString("phone");
                String room    = rs.getString("room_number");
                result.append(teacher)
                        .append(" | ").append(email)
                        .append(" | ").append(phone)
                        .append(" | Кабинет: ").append(room)
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Ошибка при получении преподавателей: " + e.getMessage();
        }
        return result.toString();
    }

    public static List<String> getAllTeacherDisplay() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id, last_name, first_name FROM teachers ORDER BY last_name, first_name";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
              //  int    id        = rs.getInt("id");
                String lastName  = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                list.add(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            // В случае ошибки возвращаем пустой список
        }
        return list;
    }

    public static String searchTeachersByName(String name) {
        if (name == null || name.isBlank()) {
            return "Пожалуйста, введите имя или фамилию преподавателя для поиска.";
        }
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT 
                CONCAT(t.last_name, ' ', t.first_name) AS teacher,
                t.email,
                t.phone,
                COALESCE(t.room_number, '-') AS room_number
            FROM teachers t
            WHERE LOWER(t.last_name) LIKE LOWER(?)
               OR LOWER(t.first_name) LIKE LOWER(?)
            ORDER BY t.last_name, t.first_name;
        """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String wildcard = "%" + name.trim() + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String teacher = rs.getString("teacher");
                    String email   = rs.getString("email");
                    String phone   = rs.getString("phone");
                    String room    = rs.getString("room_number");
                    result.append(teacher)
                            .append(" | ").append(email)
                            .append(" | ").append(phone)
                            .append(" | Кабинет: ").append(room)
                            .append("\n");
                }
                if (!found) {
                    return "Преподаватели по запросу «" + name + "» не найдены.";
                }
            }
        } catch (SQLException e) {
            return "Ошибка при поиске преподавателей: " + e.getMessage();
        }
        return result.toString();
    }

    public static String addTeacher(String first, String last, String email, String phone, String roomNumber) {
        if (first == null || first.isBlank()) {
            return "Имя преподавателя обязательно.";
        }
        if (last == null || last.isBlank()) {
            return "Фамилия преподавателя обязательна.";
        }
        if (email == null || email.isBlank() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            return "Ошибка: неверный формат email.";
        }
        if (phone == null || !phone.matches("^\\d{5,20}$")) {
            return "Ошибка: телефон должен содержать только цифры (5–20 символов).";
        }
        if (roomNumber == null || roomNumber.isBlank()) {
            return "Ошибка: необходимо указать кабинет.";
        }
        String sql = "INSERT INTO teachers (first_name, last_name, email, phone, room_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, roomNumber);
            ps.executeUpdate();
            return "Преподаватель успешно добавлен.";
        } catch (SQLException e) {
            return "Ошибка при добавлении преподавателя: " + e.getMessage();
        }
    }
}
