package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import com.coursework.Application.service.TeacherService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

import java.util.List;

public class SearchTab {

    public static Tab createSearchTab(TextArea outputArea) {
        Tab tab = new Tab("Поиск");

        TextField roomInput = new TextField();
        roomInput.setPromptText("Введите номер аудитории");
        Button searchRoomBtn = new Button("Найти аудиторию");
        searchRoomBtn.setOnAction(e -> {
            String input = roomInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(RoomService.searchRoomByNumber(input));
            }
        });

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Введите имя или фамилию преподавателя");
        List<String> teacherNames = TeacherService.getAllTeacherNames();
        TextFields.bindAutoCompletion(teacherInput, teacherNames);

        Button searchTeacherBtn = new Button("Найти преподавателя");
        searchTeacherBtn.setOnAction(e -> {
            String input = teacherInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(TeacherService.searchTeachersByName(input));
            }
        });

        VBox content = new VBox(10,
                new Label("Поиск аудитории:"), roomInput, searchRoomBtn,
                new Separator(),
                new Label("Поиск преподавателя:"), teacherInput, searchTeacherBtn
        );
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
