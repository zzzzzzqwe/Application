package com.coursework.Application.view;

import com.coursework.Application.service.RoomService;
import com.coursework.Application.service.TeacherService;
import com.coursework.Application.util.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TeacherTab {

    public static Tab createTeachersTab(TextArea outputArea) {
        Tab tab = new Tab("Преподаватели");
        tab.getStyleClass().add("tab-label");

        VBox container = new VBox(15);
        container.getStyleClass().add("tab-content-area");
        container.setPadding(new Insets(15));

        Button allTeachersBtn = new Button("Все преподаватели");
        allTeachersBtn.getStyleClass().add("action-button");

        Separator separator = new Separator();

        Label headerAdd = new Label("Добавить нового преподавателя:");

        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        Label fnLabel = new Label("Имя:");
        TextField fnField = new TextField();
        fnField.setPromptText("Имя");
        fnField.setPrefWidth(250);
        fnField.getStyleClass().add("text-field");

        Label lnLabel = new Label("Фамилия:");
        TextField lnField = new TextField();
        lnField.setPromptText("Фамилия");
        lnField.setPrefWidth(250);
        lnField.getStyleClass().add("text-field");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(250);
        emailField.getStyleClass().add("text-field");

        Label phoneLabel = new Label("Телефон:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Телефон");
        phoneField.setPrefWidth(250);
        phoneField.getStyleClass().add("text-field");

        Label roomLabel = new Label("Кабинет:");
        ComboBox<String> roomCombo = new ComboBox<>();
        roomCombo.setPrefWidth(250);
        roomCombo.getStyleClass().add("combo-field");
        roomCombo.getItems().addAll(RoomService.getAllRoomNumbers());
        roomCombo.getItems().add(0, "Выберите кабинет");
        roomCombo.getSelectionModel().select(0);

        Button addTeacherBtn = new Button("Добавить");
        addTeacherBtn.getStyleClass().add("action-button");
        addTeacherBtn.setDisable(true);

        boolean isAdmin = "admin".equals(UserSession.getUsername());
        if (isAdmin) {
            addTeacherBtn.setDisable(false);
            fnField.setDisable(false);
            lnField.setDisable(false);
            emailField.setDisable(false);
            phoneField.setDisable(false);
            roomCombo.setDisable(false);
        } else {
            fnField.setDisable(true);
            lnField.setDisable(true);
            emailField.setDisable(true);
            phoneField.setDisable(true);
            roomCombo.setDisable(true);
        }

        addForm.add(fnLabel,        0, 0);
        addForm.add(fnField,        1, 0);
        addForm.add(lnLabel,        0, 1);
        addForm.add(lnField,        1, 1);
        addForm.add(emailLabel,     0, 2);
        addForm.add(emailField,     1, 2);
        addForm.add(phoneLabel,     0, 3);
        addForm.add(phoneField,     1, 3);
        addForm.add(roomLabel,      0, 4);
        addForm.add(roomCombo,      1, 4);
        addForm.add(addTeacherBtn,  1, 5);

        container.getChildren().addAll(
                allTeachersBtn,
                separator,
                headerAdd,
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

        allTeachersBtn.setOnAction(e ->
                outputArea.setText(TeacherService.getAllTeachers())
        );

        addTeacherBtn.setOnAction(e -> {
            String first      = fnField.getText().trim();
            String last       = lnField.getText().trim();
            String email      = emailField.getText().trim();
            String phone      = phoneField.getText().trim();
            String roomNumber = roomCombo.getValue();
            if (roomNumber == null || roomNumber.isBlank() || roomNumber.equals("Выберите кабинет")) {
                outputArea.setText("Ошибка: выберите кабинет для преподавателя.");
                return;
            }
            outputArea.setText(TeacherService.addTeacher(first, last, email, phone, roomNumber));
        });

        return tab;
    }
}
