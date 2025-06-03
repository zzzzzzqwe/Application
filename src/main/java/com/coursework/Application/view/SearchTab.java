package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import com.coursework.Application.service.TeacherService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchTab {

    public static Tab createSearchTab(TextArea outputArea) {
        Tab tab = new Tab("Поиск");
        tab.getStyleClass().add("tab-label");

        VBox container = new VBox(15);
        container.getStyleClass().add("tab-content-area");
        container.setPadding(new Insets(15));

        Label roomLabel = new Label("Поиск аудитории (номер):");
        HBox roomSearchBox = new HBox(10);
        TextField roomInput = new TextField();
        roomInput.setPromptText("Введите номер кабинета");
        roomInput.setPrefWidth(250);
        roomInput.getStyleClass().add("text-field");
        Button searchRoomBtn = new Button("Найти кабинет");
        searchRoomBtn.getStyleClass().add("action-button");
        roomSearchBox.getChildren().addAll(roomInput, searchRoomBtn);

        Label teacherLabel = new Label("Поиск преподавателя (имя или фамилия):");
        HBox teacherSearchBox = new HBox(10);
        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Введите имя/фамилию");
        teacherInput.setPrefWidth(250);
        teacherInput.getStyleClass().add("text-field");
        Button searchTeacherBtn = new Button("Найти преподавателя");
        searchTeacherBtn.getStyleClass().add("action-button");
        teacherSearchBox.getChildren().addAll(teacherInput, searchTeacherBtn);

        container.getChildren().addAll(
                roomLabel, roomSearchBox,
                new Separator(),
                teacherLabel, teacherSearchBox
        );
        tab.setContent(container);
        tab.setClosable(false);

        searchRoomBtn.setOnAction(e -> {
            String input = roomInput.getText().trim();
            outputArea.setText(RoomService.searchRoomByNumber(input));
        });

        searchTeacherBtn.setOnAction(e -> {
            String input = teacherInput.getText().trim();
            outputArea.setText(TeacherService.searchTeachersByName(input));
        });

        return tab;
    }
}
