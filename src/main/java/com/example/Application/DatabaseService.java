package com.example.Application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {

    public static String getTeacherEmailById(int id) throws SQLException {
        String email = null;
        String sql = "SELECT email FROM teachers WHERE id = " + id;

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                email = rs.getString(1);
            }
        }

        return email;
    }

    public static String searchTeachers(String searchQuery) throws SQLException {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT first_name, last_name, email FROM teachers WHERE " +
                "first_name ILIKE '%" + searchQuery + "%' OR " +
                "last_name ILIKE '%" + searchQuery + "%' OR " +
                "email ILIKE '%" + searchQuery + "%'";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                result.append(rs.getString("first_name")).append(" ")
                        .append(rs.getString("last_name")).append(" (")
                        .append(rs.getString("email")).append(")\n");
            }
        }

        return result.toString();
    }

    public static String checkRoomStatus(int roomNumber) throws SQLException {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT rooms.id AS room_id, rooms.isavailable, schedule.teacher_id " +
                "FROM rooms " +
                "LEFT JOIN schedule ON rooms.id = schedule.room_id " +
                "WHERE rooms.room_number = '" + roomNumber + "'";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                boolean isAvailable = rs.getBoolean("isavailable");
                int teacherId = rs.getInt("teacher_id");

                if (isAvailable) {
                    result.append("Аудитория ").append(roomNumber).append(" свободна.");
                } else {
                    result.append("Аудитория ").append(roomNumber).append(" занята. Teacher ID: ").append(teacherId);
                }
            } else {
                result.append("Аудитория с номером ").append(roomNumber).append(" не найдена.");
            }
        }

        return result.toString();
    }
}
