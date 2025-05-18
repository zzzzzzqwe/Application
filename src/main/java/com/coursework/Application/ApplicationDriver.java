package com.coursework.Application;
import com.coursework.Application.view.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.example.Application.view.*;
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
        outputArea.setWrapText(true);
        // outputArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        outputArea.setPrefRowCount(10);

        Button loginBtn = new Button("Войти");

        loginBtn.setOnAction(e -> {
            if (LoginWindow.showLoginDialog()) {
                outputArea.appendText("Вход выполнен\n");
                loginBtn.setDisable(true); // отключаем после входа
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(spacer, loginBtn);
        topBar.setPadding(new Insets(10, 10, 5, 10));

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                RoomTab.createRoomsTab(outputArea),
                TeacherTab.createTeachersTab(outputArea),
                SearchTab.createSearchTab(outputArea),
                ScheduleTab.createScheduleTab(outputArea)
        );

        VBox layout = new VBox(topBar, tabPane, outputArea);
        VBox.setVgrow(outputArea, Priority.ALWAYS);
        layout.setPadding(new Insets(5));
        layout.setSpacing(5);

        Scene scene = new Scene(layout, 780, 540);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Университетская информационная система");
        primaryStage.show();
    }
}
