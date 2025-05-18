package com.example.Application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {


//    // ВЫВОД ВСЕХ АУДИТОРИЙ
//    public static String getAllRoomsWithCurrentTeachers() {
//        StringBuilder result = new StringBuilder();
//
//        String sql = "SELECT \n" +
//                "            r.room_number,\n" +
//                "            COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher\n" +
//                "        FROM rooms r\n" +
//                "        LEFT JOIN teachers t ON r.room_number = t.room_number\n" +
//                "        ORDER BY r.room_number;";
//
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                String room = rs.getString("room_number");
//                String teacher = rs.getString("teacher");
//                result.append("Кабинет ").append(room)
//                        .append(" — ").append(teacher).append("\n");
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при получении данных: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }


//    // ПОИСК АУДИТОРИИ ПО НОМЕРУ
//    public static String searchRoomByNumber(String roomInput) {
//        StringBuilder result = new StringBuilder();
//
//        // prepared statement with placeholders to avoid sql-injections
//        String sql = """
//                    SELECT
//                        r.room_number,
//                        COALESCE(CONCAT(t.last_name, ' ', t.first_name), '-') AS teacher
//                    FROM rooms r
//                    LEFT JOIN teachers t ON r.room_number = t.room_number
//                    WHERE r.room_number = ?
//                    ORDER BY r.room_number;
//                """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             var ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, roomInput);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String room = rs.getString("room_number");
//                    String teacher = rs.getString("teacher");
//                    result.append("Кабинет ").append(room)
//                            .append(" — ").append(teacher).append("\n");
//                } else {
//                    result.append("Кабинет ").append(roomInput).append(" не найден.");
//                }
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при поиске кабинета: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }

//    // ПОЛУЧЕНИЕ ТОЛЬКО ЗАНЯТЫХ КАБИНЕТОВ
//    public static String getOccupiedRooms() {
//        StringBuilder result = new StringBuilder();
//
//        String sql = """
//                    SELECT
//                        r.room_number,
//                        CONCAT(t.last_name, ' ', t.first_name) AS teacher
//                    FROM rooms r
//                    JOIN teachers t ON r.room_number = t.room_number
//                    ORDER BY r.room_number;
//                """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                String room = rs.getString("room_number");
//                String teacher = rs.getString("teacher");
//                result.append("Кабинет ").append(room)
//                        .append(" — ").append(teacher).append("\n");
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при получении занятых кабинетов: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }

//    // ПОЛУЧЕНИЕ ТОЛЬКО СВОБОДНЫХ
//    public static String getFreeRooms() {
//        StringBuilder result = new StringBuilder();
//
//        String sql = """
//                    SELECT
//                        r.room_number,
//                        'Свободно' AS status
//                    FROM rooms r
//                    WHERE r.room_number NOT IN (
//                        SELECT room_number FROM teachers WHERE room_number IS NOT NULL
//                    )
//                    ORDER BY r.room_number;
//                """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                String room = rs.getString("room_number");
//                result.append("Кабинет ").append(room).append(" — Свободно\n");
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при получении свободных кабинетов: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }


//    // ВЫВОД ВСЕХ ПРЕПОДАВАТЕЛЕЙ
//    public static String getAllTeachers() {
//        StringBuilder result = new StringBuilder();
//
//        String sql = """
//                    SELECT
//                        CONCAT(t.last_name, ' ', t.first_name) AS teacher,
//                        t.email,
//                        t.phone,
//                        COALESCE(t.room_number, '-') AS room_number
//                    FROM teachers t
//                    ORDER BY t.last_name, t.first_name;
//                """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                String teacher = rs.getString("teacher");
//                String email = rs.getString("email");
//                String phone = rs.getString("phone");
//                String room = rs.getString("room_number");
//
//                result.append(teacher)
//                        .append(" | ").append(email)
//                        .append(" | ").append(phone)
//                        .append(" | Кабинет: ").append(room)
//                        .append("\n");
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при получении преподавателей: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }

//    // ПОИСК ПРЕПОДАВАТЕЛЯ ПО ИМЕНИ
//    public static String searchTeachersByName(String namePart) {
//        StringBuilder result = new StringBuilder();
//
//        // prepared statement with placeholders to avoid sql-injections
//        String sql = """
//                    SELECT
//                        CONCAT(t.last_name, ' ', t.first_name) AS teacher,
//                        t.email,
//                        t.phone,
//                        COALESCE(t.room_number, '-') AS room_number
//                    FROM teachers t
//                    WHERE t.first_name ILIKE ? OR t.last_name ILIKE ? OR CONCAT(t.last_name, ' ', t.first_name) ILIKE ?
//                    ORDER BY t.last_name, t.first_name;
//                """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             var ps = con.prepareStatement(sql)) {
//
//            String likePattern = "%" + namePart + "%";
//            ps.setString(1, likePattern);
//            ps.setString(2, likePattern);
//            ps.setString(3, likePattern);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    String teacher = rs.getString("teacher");
//                    String email = rs.getString("email");
//                    String phone = rs.getString("phone");
//                    String room = rs.getString("room_number");
//
//                    result.append(teacher)
//                            .append(" | ").append(email)
//                            .append(" | ").append(phone)
//                            .append(" | Кабинет: ").append(room)
//                            .append("\n");
//                }
//
//                if (result.length() == 0) {
//                    result.append("Преподаватели не найдены.");
//                }
//
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при поиске преподавателя: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }

//    // ВЫВОД ВСЕГО РАСПИСАНИЯ
//    public static String getSchedule() {
//        StringBuilder result = new StringBuilder();
//
//        String sql = """
//            SELECT s.day_of_week,
//                   TO_CHAR(s.start_time, 'HH24:MI') || '–' || TO_CHAR(s.end_time, 'HH24:MI') AS time,
//                   CONCAT(t.last_name, ' ', t.first_name) AS teacher,
//                   s.room_number
//            FROM schedule s
//            JOIN teachers t ON s.teacher_id = t.id
//            ORDER BY s.day_of_week, s.start_time;
//        """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                String day = rs.getString("day_of_week");
//                String time = rs.getString("time");
//                String teacher = rs.getString("teacher");
//                String room = rs.getString("room_number");
//
//                result.append(day)
//                        .append(" | ").append(time)
//                        .append(" | ").append(teacher)
//                        .append(" | Кабинет: ").append(room)
//                        .append("\n");
//            }
//
//        } catch (SQLException e) {
//            result.append("Ошибка при получении расписания: ").append(e.getMessage());
//        }
//
//        return result.toString();
//    }

//    // ДЛЯ АВТОЗАПОЛНЕНИЯ
//    public static List<String> getAllTeacherNames() {
//        List<String> names = new ArrayList<>();
//
//        String sql = "SELECT CONCAT(last_name, ' ', first_name) AS full_name FROM teachers";
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                names.add(rs.getString("full_name"));
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Ошибка при получении имён преподавателей: " + e.getMessage());
//        }
//
//        return names;
//    }

//    public static boolean authenticate(String username, String password) {
//        String sql = "SELECT password FROM users WHERE username = ?";
//        try (Connection con = DatabaseConnection.getConnection();
//             var ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, username);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String realPassword = rs.getString("password");
//                    return password.equals(realPassword); // позже заменить на хеш-сравнение
//                }
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Ошибка при аутентификации: " + e.getMessage());
//        }
//        return false;
//    }

}




    // old test functions, to delete
//    public static String getTeacherEmailById(int id) throws SQLException {
//        String email = null;
//        String sql = "SELECT email FROM teachers WHERE id = " + id;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            if (rs.next()) {
//                email = rs.getString(1);
//            }
//        }
//
//        return email;
//    }
//
//    public static String searchTeachers(String searchQuery) throws SQLException {
//        StringBuilder result = new StringBuilder();
//        String sql = "SELECT first_name, last_name, email FROM teachers WHERE " +
//                "first_name ILIKE '%" + searchQuery + "%' OR " +
//                "last_name ILIKE '%" + searchQuery + "%' OR " +
//                "email ILIKE '%" + searchQuery + "%'";
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                result.append(rs.getString("first_name")).append(" ")
//                        .append(rs.getString("last_name")).append(" (")
//                        .append(rs.getString("email")).append(")\n");
//            }
//        }
//
//        return result.toString();
//    }
//
//    public static String checkRoomStatus(int roomNumber) throws SQLException {
//        StringBuilder result = new StringBuilder();
//        String sql = "SELECT rooms.id AS room_id, rooms.isavailable, schedule.teacher_id " +
//                "FROM rooms " +
//                "LEFT JOIN schedule ON rooms.id = schedule.room_id " +
//                "WHERE rooms.room_number = '" + roomNumber + "'";
//
//        try (Connection con = DatabaseConnection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            if (rs.next()) {
//                boolean isAvailable = rs.getBoolean("isavailable");
//                int teacherId = rs.getInt("teacher_id");
//
//                if (isAvailable) {
//                    result.append("Аудитория ").append(roomNumber).append(" свободна.");
//                } else {
//                    result.append("Аудитория ").append(roomNumber).append(" занята. Teacher ID: ").append(teacherId);
//                }
//            } else {
//                result.append("Аудитория с номером ").append(roomNumber).append(" не найдена.");
//            }
//        }
//
//        return result.toString();
//    }
//}
