package com.coursework.Application.view;

import com.coursework.Application.service.TeacherService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class TeacherTab {
    public static Tab createTeachersTab(TextArea outputArea) {
        Tab tab = new Tab("Преподаватели");

        Button allTeachersBtn = new Button("Показать всех");
        allTeachersBtn.setOnAction(e -> outputArea.setText(TeacherService.getAllTeachers()));

        VBox content = new VBox(10, allTeachersBtn);
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
