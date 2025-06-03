package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    public static List<String> getAllRoomNumbers() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT room_number FROM rooms ORDER BY room_number";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("room_number"));
            }
        } catch (SQLException e) {
            // empty list if something goes wrong
        }
        return list;
    }

    public static String getAllRoomsWithCurrentTeachers() {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT 
                r.room_number,
                r.floor,
                r.block,
                r.subject,
                COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher
            FROM rooms r
            LEFT JOIN teachers t ON r.room_number = t.room_number
            ORDER BY r.room_number;
        """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String room    = rs.getString("room_number");
                int floor      = rs.getInt("floor");
                String block   = rs.getString("block");
                String subject = rs.getString("subject");
                String teacher = rs.getString("teacher");
                result.append("Аудитория: ").append(room)
                        .append(" | Этаж: ").append(floor)
                        .append(" | Блок: ").append(block)
                        .append(" | Предмет: ").append(subject == null ? "-" : subject)
                        .append(" | Преподаватель: ").append(teacher)
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Ошибка при получении аудиторий: " + e.getMessage();
        }
        return result.toString();
    }

    public static String searchRoomByNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isBlank()) {
            return "Пожалуйста, введите номер аудитории для поиска.";
        }

        StringBuilder result = new StringBuilder();
        String sql = """
        SELECT 
            r.room_number,
            r.floor,
            r.block,
            r.subject,
            COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher
        FROM rooms r
        LEFT JOIN teachers t ON r.room_number = t.room_number
        WHERE LOWER(r.room_number) LIKE LOWER(?)
        ORDER BY r.room_number;
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + roomNumber.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String room    = rs.getString("room_number");
                    int floor      = rs.getInt("floor");
                    String block   = rs.getString("block");
                    String subject = rs.getString("subject");
                    String teacher = rs.getString("teacher");

                    result.append("Аудитория: ").append(room)
                            .append(" | Этаж: ").append(floor)
                            .append(" | Блок: ").append(block)
                            .append(" | Предмет: ").append(subject == null ? "-" : subject)
                            .append(" | Преподаватель: ").append(teacher)
                            .append("\n");
                }
                if (!found) {
                    return "Аудитории по запросу " + roomNumber + " не найдены.";
                }
            }
        } catch (SQLException e) {
            return "Ошибка при поиске аудиторий: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getOccupiedRooms() {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT 
                r.room_number,
                r.floor,
                r.block,
                r.subject,
                CONCAT(t.last_name, ' ', t.first_name) AS teacher
            FROM rooms r
            JOIN teachers t ON r.room_number = t.room_number
            ORDER BY r.room_number;
        """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String room    = rs.getString("room_number");
                int floor      = rs.getInt("floor");
                String block   = rs.getString("block");
                String subject = rs.getString("subject");
                String teacher = rs.getString("teacher");
                result.append("Аудитория: ").append(room)
                        .append(" | Этаж: ").append(floor)
                        .append(" | Блок: ").append(block)
                        .append(" | Предмет: ").append(subject == null ? "-" : subject)
                        .append(" | Преподаватель: ").append(teacher)
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Ошибка при получении занятых аудиторий: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getFreeRooms() {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT 
                r.room_number,
                r.floor,
                r.block,
                r.subject
            FROM rooms r
            WHERE r.room_number NOT IN (
                SELECT room_number FROM teachers WHERE room_number IS NOT NULL
            )
            ORDER BY r.room_number;
        """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String room    = rs.getString("room_number");
                int floor      = rs.getInt("floor");
                String block   = rs.getString("block");
                String subject = rs.getString("subject");
                result.append("Аудитория: ").append(room)
                        .append(" | Этаж: ").append(floor)
                        .append(" | Блок: ").append(block)
                        .append(" | Предмет: ").append(subject == null ? "-" : subject)
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Ошибка при получении свободных аудиторий: " + e.getMessage();
        }
        return result.toString();
    }

    public static String addRoom(String roomNumber, String floorStr, String block, String subject) {
        if (roomNumber == null || !roomNumber.matches("^[0-9]{3}[A-Z]?$")) {
            return "Ошибка: неверный формат номера аудитории. Допустимо три цифры и опционально одна буква (например, 101 или 302A).";
        }
        int floor;
        try {
            floor = Integer.parseInt(floorStr);
        } catch (NumberFormatException e) {
            return "Ошибка: этаж должен быть числом от 1 до 4.";
        }
        if (floor < 1 || floor > 4) {
            return "Ошибка: этаж должен быть числом от 1 до 4.";
        }
        if (block == null || !block.matches("^[A-Z]$")) {
            return "Ошибка: блок должен быть одной заглавной буквой (например, A, B, C).";
        }
        if (subject == null || subject.isBlank()) {
            return "Ошибка: предмет не может быть пустым.";
        }
        String sql = "INSERT INTO rooms (room_number, floor, block, subject) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.setInt(2, floor);
            ps.setString(3, block);
            ps.setString(4, subject.trim());
            ps.executeUpdate();
            return "Аудитория добавлена успешно.";
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return "Ошибка: аудитория с номером " + roomNumber + " уже существует.";
            }
            return "Ошибка при добавлении аудитории: " + e.getMessage();
        }
    }
}
