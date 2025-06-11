package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import com.coursework.Application.service.TeacherService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
        ComboBox<String> roomCombo = new ComboBox<>();
        roomCombo.setPrefWidth(250);
        roomCombo.getStyleClass().add("combo-field");
        roomCombo.getItems().add("Выберите аудиторию");
        roomCombo.getItems().addAll(RoomService.getAllRoomNumbers());
        roomCombo.getSelectionModel().selectFirst();

        Button searchRoomBtn = new Button("Найти кабинет");
        searchRoomBtn.getStyleClass().add("action-button");
        roomSearchBox.getChildren().addAll(roomCombo, searchRoomBtn);

        Label teacherLabel = new Label("Поиск преподавателя (имя или фамилия):");
        HBox teacherSearchBox = new HBox(10);
        ComboBox<String> teacherCombo = new ComboBox<>();
        teacherCombo.setPrefWidth(250);
        teacherCombo.getStyleClass().add("combo-field");
        teacherCombo.getItems().add("Выберите преподавателя");
        teacherCombo.getItems().addAll(TeacherService.getAllTeacherDisplay());
        teacherCombo.getSelectionModel().selectFirst();

        Button searchTeacherBtn = new Button("Найти преподавателя");
        searchTeacherBtn.getStyleClass().add("action-button");
        teacherSearchBox.getChildren().addAll(teacherCombo, searchTeacherBtn);

        container.getChildren().addAll(
                roomLabel, roomSearchBox,
                new Separator(),
                teacherLabel, teacherSearchBox
        );
        tab.setContent(container);
        tab.setClosable(false);

        searchRoomBtn.setOnAction(e -> {
            String input = roomCombo.getValue().trim();
            outputArea.setText(RoomService.searchRoomByNumber(input));
        });

        searchTeacherBtn.setOnAction(e -> {
            String input = teacherCombo.getValue().trim();
            outputArea.setText(TeacherService.searchTeachersByName(input));
        });

        return tab;
    }
}
