package com.example.Application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ApplicationDriver extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField idField = new TextField();
        idField.setPromptText("Введите ID преподавателя");
        Button getEmailButton = new Button("Получить email");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        TextField searchField = new TextField();
        searchField.setPromptText("Поиск преподавателя");
        Button searchButton = new Button("Искать");

        TextField roomField = new TextField();
        roomField.setPromptText("Номер аудитории");
        Button checkRoomButton = new Button("Проверить аудиторию");

        VBox layout = new VBox(10, idField, getEmailButton, searchField, searchButton, roomField, checkRoomButton, resultArea);
        layout.setPadding(new Insets(15));

        getEmailButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String email = DatabaseService.getTeacherEmailById(id);
                resultArea.setText("Email преподавателя: " + email);
            } catch (Exception ex) {
                resultArea.setText("Ошибка: " + ex.getMessage());
            }
        });

        searchButton.setOnAction(e -> {
            try {
                String query = searchField.getText();
                String result = DatabaseService.searchTeachers(query);
                resultArea.setText(result);
            } catch (Exception ex) {
                resultArea.setText("Ошибка: " + ex.getMessage());
            }
        });

        checkRoomButton.setOnAction(e -> {
            try {
                int room = Integer.parseInt(roomField.getText());
                String result = DatabaseService.checkRoomStatus(room);
                resultArea.setText(result);
            } catch (Exception ex) {
                resultArea.setText("Ошибка: " + ex.getMessage());
            }
        });

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Приложение преподавателей");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
