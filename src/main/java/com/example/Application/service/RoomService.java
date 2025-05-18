package com.example.Application.service;

import com.example.Application.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomService {
    // ВЫВОД ВСЕХ АУДИТОРИЙ
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

    // ПОИСК АУДИТОРИИ ПО НОМЕРУ
    public static String searchRoomByNumber(String roomInput) {
        StringBuilder result = new StringBuilder();

        // prepared statement with placeholders to avoid sql-injections
        String sql = """
                    SELECT 
                        r.room_number,
                        COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher
                    FROM rooms r
                    LEFT JOIN teachers t ON r.room_number = t.room_number
                    WHERE r.room_number = ?
                    ORDER BY r.room_number;
                """;

        try (Connection con = DatabaseConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, roomInput);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String room = rs.getString("room_number");
                    String teacher = rs.getString("teacher");
                    result.append("Кабинет ").append(room)
                            .append(" — ").append(teacher).append("\n");
                } else {
                    result.append("Кабинет ").append(roomInput).append(" не найден.");
                }
            }

        } catch (SQLException e) {
            result.append("Ошибка при поиске кабинета: ").append(e.getMessage());
        }

        return result.toString();
    }

    // ПОЛУЧЕНИЕ ТОЛЬКО ЗАНЯТЫХ КАБИНЕТОВ
    public static String getOccupiedRooms() {
        StringBuilder result = new StringBuilder();

        String sql = """
                    SELECT 
                        r.room_number,
                        CONCAT(t.last_name, ' ', t.first_name) AS teacher
                    FROM rooms r
                    JOIN teachers t ON r.room_number = t.room_number
                    ORDER BY r.room_number;
                """;

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
            result.append("Ошибка при получении занятых кабинетов: ").append(e.getMessage());
        }

        return result.toString();
    }

    // ПОЛУЧЕНИЕ ТОЛЬКО СВОБОДНЫХ
    public static String getFreeRooms() {
        StringBuilder result = new StringBuilder();

        String sql = """
                    SELECT 
                        r.room_number,
                        'Свободно' AS status
                    FROM rooms r
                    WHERE r.room_number NOT IN (
                        SELECT room_number FROM teachers WHERE room_number IS NOT NULL
                    )
                    ORDER BY r.room_number;
                """;

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String room = rs.getString("room_number");
                result.append("Кабинет ").append(room).append(" — Свободно\n");
            }

        } catch (SQLException e) {
            result.append("Ошибка при получении свободных кабинетов: ").append(e.getMessage());
        }

        return result.toString();
    }

}
