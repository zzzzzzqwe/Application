package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    // ВЫВОД ВСЕХ ПРЕПОДАВАТЕЛЕЙ
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
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String teacher = rs.getString("teacher");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String room = rs.getString("room_number");

                result.append(teacher)
                        .append(" | ").append(email)
                        .append(" | ").append(phone)
                        .append(" | Кабинет: ").append(room)
                        .append("\n");
            }

        } catch (SQLException e) {
            result.append("Ошибка при получении преподавателей: ").append(e.getMessage());
        }

        return result.toString();
    }

    // ПОИСК ПРЕПОДАВАТЕЛЯ ПО ИМЕНИ
    public static String searchTeachersByName(String namePart) {
        StringBuilder result = new StringBuilder();

        // prepared statement with placeholders to avoid sql-injections
        String sql = """
                    SELECT 
                        CONCAT(t.last_name, ' ', t.first_name) AS teacher,
                        t.email,
                        t.phone,
                        COALESCE(t.room_number, '-') AS room_number
                    FROM teachers t
                    WHERE t.first_name ILIKE ? OR t.last_name ILIKE ? OR CONCAT(t.last_name, ' ', t.first_name) ILIKE ?
                    ORDER BY t.last_name, t.first_name;
                """;

        try (Connection con = DatabaseConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            String likePattern = "%" + namePart + "%";
            ps.setString(1, likePattern);
            ps.setString(2, likePattern);
            ps.setString(3, likePattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String teacher = rs.getString("teacher");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String room = rs.getString("room_number");

                    result.append(teacher)
                            .append(" | ").append(email)
                            .append(" | ").append(phone)
                            .append(" | Кабинет: ").append(room)
                            .append("\n");
                }

                if (result.length() == 0) {
                    result.append("Преподаватели не найдены.");
                }

            }

        } catch (SQLException e) {
            result.append("Ошибка при поиске преподавателя: ").append(e.getMessage());
        }

        return result.toString();
    }

    // ДЛЯ АВТОЗАПОЛНЕНИЯ
    public static List<String> getAllTeacherNames() {
        List<String> names = new ArrayList<>();

        String sql = "SELECT CONCAT(last_name, ' ', first_name) AS full_name FROM teachers";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                names.add(rs.getString("full_name"));
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении имён преподавателей: " + e.getMessage());
        }

        return names;
    }
}
