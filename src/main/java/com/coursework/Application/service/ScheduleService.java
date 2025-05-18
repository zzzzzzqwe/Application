package com.coursework.Application.service;

import com.coursework.Application.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScheduleService {

    // ВЫВОД ВСЕГО РАСПИСАНИЯ
    public static String getSchedule() {
        StringBuilder result = new StringBuilder();

        String sql = """
            SELECT s.day_of_week,
                   TO_CHAR(s.start_time, 'HH24:MI') || '–' || TO_CHAR(s.end_time, 'HH24:MI') AS time,
                   CONCAT(t.last_name, ' ', t.first_name) AS teacher,
                   s.room_number
            FROM schedule s
            JOIN teachers t ON s.teacher_id = t.id
            ORDER BY s.day_of_week, s.start_time;
        """;

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                String time = rs.getString("time");
                String teacher = rs.getString("teacher");
                String room = rs.getString("room_number");

                result.append(day)
                        .append(" | ").append(time)
                        .append(" | ").append(teacher)
                        .append(" | Кабинет: ").append(room)
                        .append("\n");
            }

        } catch (SQLException e) {
            result.append("Ошибка при получении расписания: ").append(e.getMessage());
        }

        return result.toString();
    }
}
