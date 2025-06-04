
package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleService {

    public static String getSchedule() {
        StringBuilder result = new StringBuilder();
        String sql = """
                    SELECT 
                        s.day_of_week,
                        TO_CHAR(s.start_time, 'HH24:MI') || '–' || TO_CHAR(s.end_time, 'HH24:MI') AS time,
                        CONCAT(t.last_name, ' ', t.first_name) AS teacher,
                        r.room_number,
                        r.subject
                    FROM schedule s
                    JOIN teachers t ON s.teacher_id = t.id
                    JOIN rooms r ON s.room_number = r.room_number
                    ORDER BY CASE s.day_of_week
                                         WHEN 'Понедельник' THEN 1
                                         WHEN 'Вторник'     THEN 2
                                         WHEN 'Среда'       THEN 3
                                         WHEN 'Четверг'     THEN 4
                                         WHEN 'Пятница'     THEN 5
                                         WHEN 'Суббота'     THEN 6
                                         WHEN 'Воскресенье' THEN 7
                                         ELSE 8
                                     END
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                String time = rs.getString("time");
                String teacher = rs.getString("teacher");
                String room = rs.getString("room_number");
                String subject = rs.getString("subject");

                result.append(day)
                        .append(" | ").append(time)
                        .append(" | Преподаватель: ").append(teacher)
                        .append(" | Аудитория: ").append(room)
                        .append(" | Предмет: ").append(subject == null ? "-" : subject)
                        .append("\n");
            }

        } catch (SQLException e) {
            return "Ошибка при получении расписания: " + e.getMessage();
        }
        return result.toString();
    }

    public static String addLesson(
            String dayOfWeek,
            String startTime,
            String endTime,
            String teacherName,
            String roomNumber
    ) {
        if (dayOfWeek == null || dayOfWeek.isBlank()) {
            return "Ошибка: день недели обязателен.";
        }
        if (startTime == null || !startTime.matches("^[0-2][0-9]:[0-5][0-9]$")) {
            return "Ошибка: неверный формат времени начала (должно быть HH:MM).";
        }
        if (endTime == null || !endTime.matches("^[0-2][0-9]:[0-5][0-9]$")) {
            return "Ошибка: неверный формат времени конца (должно быть HH:MM).";
        }
        if (teacherName == null || teacherName.isBlank() || !teacherName.contains(" ")) {
            return "Ошибка: выберите преподавателя из списка.";
        }
        if (roomNumber == null || !roomNumber.matches("^[0-9]{3}[A-Z]?$")) {
            return "Ошибка: неверный формат номера аудитории.";
        }

        String[] nameParts = teacherName.trim().split("\\s+", 2);
        if (nameParts.length < 2) {
            return "Ошибка: неверный формат имени преподавателя.";
        }
        String firstName = nameParts[0];
        String lastName  = nameParts[1];

        int teacherId;
        String findTeacherSql = """
                    SELECT id 
                    FROM teachers 
                    WHERE last_name = ? AND first_name = ?;
                """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement psFind = con.prepareStatement(findTeacherSql)) {
            psFind.setString(1, lastName);
            psFind.setString(2, firstName);
            try (ResultSet rs = psFind.executeQuery()) {
                if (rs.next()) {
                    teacherId = rs.getInt("id");
                } else {
                    return "Ошибка: преподаватель \"" + teacherName + "\" не найден.";
                }
            }
        } catch (SQLException e) {
            return "Ошибка при поиске преподавателя: " + e.getMessage();
        }

        String checkSql = """
                    SELECT COUNT(*) AS cnt
                    FROM schedule
                    WHERE day_of_week = ?
                      AND room_number = ?
                      AND NOT (end_time <= ? OR start_time >= ?)
                """;

        String insertSql = """
                    INSERT INTO schedule (day_of_week, start_time, end_time, teacher_id, room_number)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection()) {
            java.sql.Time startTimeSql = java.sql.Time.valueOf(startTime + ":00");
            java.sql.Time endTimeSql = java.sql.Time.valueOf(endTime + ":00");

            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setString(1, dayOfWeek.trim());
                psCheck.setString(2, roomNumber);
                psCheck.setTime(3, startTimeSql);
                psCheck.setTime(4, endTimeSql);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    if (rsCheck.next() && rsCheck.getInt("cnt") > 0) {
                        return "Ошибка: аудитория " + roomNumber + " уже занята в указанный промежуток.";
                    }
                }
            }

            try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {
                psInsert.setString(1, dayOfWeek.trim());
                psInsert.setTime(2, startTimeSql);
                psInsert.setTime(3, endTimeSql);
                psInsert.setInt(4, teacherId);
                psInsert.setString(5, roomNumber);
                psInsert.executeUpdate();
                return "Пара успешно добавлена.";
            }

        } catch (SQLException e) {
            return "Ошибка при добавлении пары: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Неверный формат времени. Используйте HH:MM.";
        }
    }

    public static String deleteLesson(String dayOfWeek, String startTime, String roomNumber) {
        if (dayOfWeek == null || dayOfWeek.isBlank()) {
            return "Ошибка: день недели обязателен.";
        }
        if (startTime == null || !startTime.matches("^[0-2][0-9]:[0-5][0-9]$")) {
            return "Ошибка: неверный формат времени начала (должно быть HH:MM).";
        }
        if (roomNumber.equals("Выберите аудиторию"))
        {
            return "Выберите аудиторию";
        }
        if (roomNumber == null || !roomNumber.matches("^[0-9]{3}[A-Z]?$")) {
            return "Ошибка: неверный формат номера аудитории.";
        }


        String deleteSql = """
            DELETE FROM schedule
            WHERE day_of_week = ? 
              AND start_time = ? 
              AND room_number = ?
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(deleteSql)) {

            ps.setString(1, dayOfWeek.trim());
            ps.setTime(2, java.sql.Time.valueOf(startTime + ":00"));
            ps.setString(3, roomNumber);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return "Пара успешно удалена.";
            } else {
                return "Пара с указанными параметрами не найдена.";
            }
        } catch (SQLException e) {
            return "Ошибка при удалении пары: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Неверный формат времени. Используйте HH:MM.";
        }
    }
}
