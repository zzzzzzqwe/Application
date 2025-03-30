package com.example.Application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import java.util.List;
import java.util.Objects;

public class ApplicationDriver extends Application {

    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icon.png"))
        );
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createRoomsTab(),
                createTeachersTab(),
                createSearchTab()
        );

        VBox layout = new VBox(tabPane, outputArea);
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        Scene scene = new Scene(layout, 750, 500);

        // css styles
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Университетская информационная система");
        primaryStage.show();
    }

    private Tab createRoomsTab() {
        Tab tab = new Tab("Аудитории");

        Button allRoomsBtn = new Button("Все");
        Button busyRoomsBtn = new Button("Занятые");
        Button freeRoomsBtn = new Button("Свободные");

        allRoomsBtn.setOnAction(e -> outputArea.setText(DatabaseService.getAllRoomsWithCurrentTeachers()));
        busyRoomsBtn.setOnAction(e -> outputArea.setText(DatabaseService.getOccupiedRooms()));
        freeRoomsBtn.setOnAction(e -> outputArea.setText(DatabaseService.getFreeRooms()));

        HBox buttons = new HBox(10, allRoomsBtn, busyRoomsBtn, freeRoomsBtn);
        buttons.setPadding(new Insets(10));

        VBox content = new VBox(buttons);
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }


    private Tab createTeachersTab() {
        Tab tab = new Tab("Преподаватели");
        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Введите имя или фамилию преподавателя");



        Button allTeachersBtn = new Button("Показать всех");

        allTeachersBtn.setOnAction(e -> outputArea.setText(DatabaseService.getAllTeachers()));

        VBox content = new VBox(10, allTeachersBtn);
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }

    private Tab createSearchTab() {
        Tab tab = new Tab("Поиск");

        // Поиск кабинета
        TextField roomInput = new TextField();
        roomInput.setPromptText("Введите номер аудитории");
        Button searchRoomBtn = new Button("Найти аудиторию");
        searchRoomBtn.setOnAction(e -> {
            String input = roomInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(DatabaseService.searchRoomByNumber(input));
            }
        });

        // Поиск преподавателя
        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Введите имя или фамилию преподавателя");

        // Автозаполнение
        // List<String> teacherNames = DatabaseService.getAllTeacherNames();
        // TextFields.bindAutoCompletion(teacherInput, teacherNames);

        Button searchTeacherBtn = new Button("Найти преподавателя");
        searchTeacherBtn.setOnAction(e -> {
            String input = teacherInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(DatabaseService.searchTeachersByName(input));
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
