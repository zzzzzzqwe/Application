package com.coursework.Application;

import com.coursework.Application.util.UserSession;
import com.coursework.Application.view.RoomTab;
import com.coursework.Application.view.TeacherTab;
import com.coursework.Application.view.SearchTab;
import com.coursework.Application.view.ScheduleTab;
import com.coursework.Application.view.LoginWindow;
import com.coursework.Application.service.AuthService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ApplicationDriver extends Application {

    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // automatic admin for testing, remove later todo
        UserSession.login("admin");

        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        Label titleLabel = new Label("Университетская информационная система");
        titleLabel.getStyleClass().add("section-title");

        Button loginBtn = new Button("Войти");
        loginBtn.getStyleClass().add("action-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, titleLabel, spacer, loginBtn);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("root-pane");

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("tab-pane");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefRowCount(10);
        outputArea.getStyleClass().add("status-bar");

        rebuildTabs(tabPane);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");
        root.setTop(topBar);
        root.setCenter(tabPane);
        root.setBottom(outputArea);

        Scene scene = new Scene(root, 800, 650);
        scene.getStylesheets().add(
                ApplicationDriver.class.getResource("/style.css").toExternalForm()
        );

        primaryStage.setTitle("Университетская информационная система");
        primaryStage.setScene(scene);
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            if (UserSession.getUsername() == null) {
                if (LoginWindow.showLoginDialog()) {
                    outputArea.setText("Вход выполнен: " + UserSession.getUsername());
                    loginBtn.setText("Выйти");
                    rebuildTabs(tabPane);
                } else {
                    outputArea.setText("Вход не выполнен");
                }
            } else {
                UserSession.clear();
                outputArea.setText("Выход выполнен");
                loginBtn.setText("Войти");
                rebuildTabs(tabPane);
            }
        });
    }

    private void rebuildTabs(TabPane tabPane) {
        tabPane.getTabs().clear();
        Tab roomsTab    = RoomTab.createRoomsTab(outputArea);
        Tab teachersTab = TeacherTab.createTeachersTab(outputArea);
        Tab searchTab   = SearchTab.createSearchTab(outputArea);
        Tab scheduleTab = ScheduleTab.createScheduleTab(outputArea);
        tabPane.getTabs().addAll(roomsTab, teachersTab, searchTab, scheduleTab);
    }
}
