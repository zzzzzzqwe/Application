package com.example.Application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ДЛЯ ПРОМЕЖУТОЧНОГО ТЕСТА МЕТОДОВ
public class Test {

    public static void main(String[] args) {
        String output = getAllRoomsWithCurrentTeachers();
        System.out.println(output);
    }

    public static String getAllRoomsWithCurrentTeachers() {
        StringBuilder result = new StringBuilder();

        String sql = "SELECT \n" +
                "            r.room_number,\n" +
                "            COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher\n" +
                "        FROM rooms r\n" +
                "        LEFT JOIN teachers t ON r.room_number = t.room_number\n" +
                "        ORDER BY r.room_number;";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String room = rs.getString("room_number");
                String teacher = rs.getString("teacher");
                result.append("Кабинет ").append(room)
                        .append(" — ").append(teacher).append("\n");
            }

        } catch (SQLException e) {
            result.append("Ошибка при получении данных: ").append(e.getMessage());
        }

        return result.toString();
    }
}
