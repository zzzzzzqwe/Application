package com.coursework.Application.view;

import com.coursework.Application.service.ScheduleService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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

        Label startLabel = new Label("Начало:");
        TextField startField = new TextField();
        startField.setPromptText("08:30");
        startField.setPrefWidth(250);
        startField.getStyleClass().add("text-field");

        Label endLabel = new Label("Конец:");
        TextField endField = new TextField();
        endField.setPromptText("10:00");
        endField.setPrefWidth(250);
        endField.getStyleClass().add("text-field");

        Label teacherIdLabel = new Label("ID преподавателя:");
        TextField teacherIdField = new TextField();
        teacherIdField.setPromptText("ID");
        teacherIdField.setPrefWidth(250);
        teacherIdField.getStyleClass().add("text-field");

        Label roomNumberLabel = new Label("Номер аудитории:");
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("101");
        roomNumberField.setPrefWidth(250);
        roomNumberField.getStyleClass().add("text-field");

        Button addLessonBtn = new Button("Добавить пару");
        addLessonBtn.getStyleClass().add("action-button");

        addForm.add(dayLabel, 0, 0);
        addForm.add(dayField, 1, 0);
        addForm.add(startLabel, 0, 1);
        addForm.add(startField, 1, 1);
        addForm.add(endLabel, 0, 2);
        addForm.add(endField, 1, 2);
        addForm.add(teacherIdLabel, 0, 3);
        addForm.add(teacherIdField, 1, 3);
        addForm.add(roomNumberLabel, 0, 4);
        addForm.add(roomNumberField, 1, 4);
        addForm.add(addLessonBtn, 1, 5);

        container.getChildren().addAll(
                showScheduleBtn,
                separator,
                addLabel,
                addForm
        );

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        double scrollMultiplier = 0.020;
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY() * scrollMultiplier;
            scrollPane.setVvalue(scrollPane.getVvalue() - delta);
            event.consume();
        });

        tab.setContent(scrollPane);
        tab.setClosable(false);

        showScheduleBtn.setOnAction(e ->
                outputArea.setText(ScheduleService.getSchedule())
        );

        addLessonBtn.setOnAction(e -> {
            String day       = dayField.getText().trim();
            String start     = startField.getText().trim();
            String end       = endField.getText().trim();
            String teacherId = teacherIdField.getText().trim();
            String room      = roomNumberField.getText().trim();
            outputArea.setText(
                    ScheduleService.addLesson(day, start, end, teacherId, room)
            );
        });

        return tab;
    }
}
