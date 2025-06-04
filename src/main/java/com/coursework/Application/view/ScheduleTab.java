package com.coursework.Application.view;

import com.coursework.Application.service.ScheduleService;
import com.coursework.Application.service.TeacherService;
import com.coursework.Application.service.RoomService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

public class ScheduleTab {


    public static Tab createScheduleTab(TextArea outputArea) {
        Tab tab = new Tab("Расписание");
        tab.getStyleClass().add("tab-label");

        VBox container = new VBox(15);
        container.getStyleClass().add("tab-content-area");
        container.setPadding(new Insets(15));

        Button showScheduleBtn = new Button("Показать расписание");
        showScheduleBtn.getStyleClass().add("action-button");

        Separator separator = new Separator();

        Label addLabel = new Label("Добавить новую пару:");

        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        Label dayLabel = new Label("День недели:");
        TextField dayField = new TextField();
        dayField.setPromptText("Понедельник");
        dayField.setPrefWidth(250);
        dayField.getStyleClass().add("text-field");

        Label startLabel = new Label("Начало (HH:MM):");
        TextField startField = new TextField();
        startField.setPromptText("09:00");
        startField.setPrefWidth(250);
        startField.getStyleClass().add("text-field");

        Label endLabel = new Label("Конец (HH:MM):");
        TextField endField = new TextField();
        endField.setPromptText("10:00");
        endField.setPrefWidth(250);
        endField.getStyleClass().add("text-field");

        Label teacherLabel = new Label("Преподаватель:");
        ComboBox<String> teacherCombo = new ComboBox<>();
        teacherCombo.setPrefWidth(250);
        teacherCombo.getStyleClass().add("combo-field");
        teacherCombo.getItems().addAll(TeacherService.getAllTeacherDisplay());

        Label roomNumberLabel = new Label("Номер аудитории:");
        ComboBox<String> roomCombo = new ComboBox<>();
        roomCombo.setPrefWidth(250);
        roomCombo.getStyleClass().add("combo-field");
        roomCombo.getItems().addAll(RoomService.getAllRoomNumbers());

        Button addLessonBtn = new Button("Добавить");
        addLessonBtn.getStyleClass().add("action-button");

        addForm.add(dayLabel,          0, 0);
        addForm.add(dayField,          1, 0);
        addForm.add(startLabel,        0, 1);
        addForm.add(startField,        1, 1);
        addForm.add(endLabel,          0, 2);
        addForm.add(endField,          1, 2);
        addForm.add(teacherLabel,      0, 3);
        addForm.add(teacherCombo,      1, 3);
        addForm.add(roomNumberLabel,   0, 4);
        addForm.add(roomCombo,         1, 4);
        addForm.add(addLessonBtn,      1, 5);

        Label deleteLabel = new Label("Удалить пару:");

        GridPane deleteForm = new GridPane();
        deleteForm.setHgap(10);
        deleteForm.setVgap(10);

        Label dayDelLabel = new Label("День недели:");
        TextField dayDelField = new TextField();
        dayDelField.setPromptText("Понедельник");
        dayDelField.setPrefWidth(250);
        dayDelField.getStyleClass().add("text-field");

        Label startDelLabel = new Label("Начало (HH:MM):");
        TextField startDelField = new TextField();
        startDelField.setPromptText("09:00");
        startDelField.setPrefWidth(250);
        startDelField.getStyleClass().add("text-field");

        Label roomDelLabel = new Label("Номер аудитории:");
        ComboBox<String> roomDelCombo = new ComboBox<>();
        roomDelCombo.setPrefWidth(250);
        roomDelCombo.getStyleClass().add("combo-field");
        roomDelCombo.getItems().addAll(RoomService.getAllRoomNumbers());

        Button deleteLessonBtn = new Button("Удалить");
        deleteLessonBtn.getStyleClass().add("action-button");

        deleteForm.add(dayDelLabel,     0, 0);
        deleteForm.add(dayDelField,     1, 0);
        deleteForm.add(startDelLabel,   0, 1);
        deleteForm.add(startDelField,   1, 1);
        deleteForm.add(roomDelLabel,    0, 2);
        deleteForm.add(roomDelCombo,    1, 2);
        deleteForm.add(deleteLessonBtn, 1, 3);

        container.getChildren().addAll(
                showScheduleBtn,
                separator,
                addLabel,
                addForm,
                deleteLabel,
                deleteForm
        );

        showScheduleBtn.setOnAction(e ->
                outputArea.setText(ScheduleService.getSchedule())
        );

        addLessonBtn.setOnAction(e -> {
            String day = dayField.getText().trim();
            String start = startField.getText().trim();
            String end   = endField.getText().trim();
            String teacherItem = teacherCombo.getValue();
            if (teacherItem == null || teacherItem.isBlank()) {
                outputArea.setText("Ошибка: выберите преподавателя.");
                return;
            }
            int teacherId = Integer.parseInt(teacherItem.split("\\s+")[0]);
            String room = roomCombo.getValue();
            if (room == null || room.isBlank()) {
                outputArea.setText("Ошибка: выберите аудиторию.");
                return;
            }
            outputArea.setText(
                    ScheduleService.addLesson(day, start, end, teacherId, room)
            );
        });

        deleteLessonBtn.setOnAction(e -> {
            String dayDel   = dayDelField.getText().trim();
            String startDel = startDelField.getText().trim();
            String roomDel  = roomDelCombo.getValue();
            if (roomDel == null || roomDel.isBlank()) {
                outputArea.setText("Ошибка: выберите аудиторию.");
                return;
            }
            outputArea.setText(
                    ScheduleService.deleteLesson(dayDel, startDel, roomDel)
            );
        });

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, e ->
                container.setTranslateX(container.getTranslateX() - e.getDeltaX())
        );


        double scrollMultiplier = 0.020;
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY() * scrollMultiplier;
            scrollPane.setVvalue(scrollPane.getVvalue() - delta);
            event.consume();
        });

        tab.setContent(scrollPane);
        return tab;
    }
}
