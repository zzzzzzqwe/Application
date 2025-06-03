//package com.coursework.Application.view;
//
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//import javafx.geometry.Insets;
//import javafx.scene.control.Tab;
//import javafx.scene.control.TabPane;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//
//public class AdminWindow {
//
//    public static void showAdminWindow() {
//        Stage stage = new Stage();
//        stage.setTitle("Админ-панель");
//
//        TabPane tabPane = new TabPane();
//        tabPane.getStyleClass().add("tab-pane");
//
//        Tab teacherTab = new Tab("Преподаватели", new VBox());
//        Tab roomTab = new Tab("Аудитории", new VBox());
//        Tab scheduleTab = new Tab("Расписание", new VBox());
//        Tab searchTab = new Tab("Поиск", new VBox());
//
//        tabPane.getTabs().addAll(teacherTab, roomTab, scheduleTab, searchTab);
//
//        VBox container = new VBox();
//        container.setPadding(new Insets(16));
//        container.getChildren().addAll(new Label("Здесь может быть любая админ-логика"));
//
//        BorderPane root = new BorderPane();
//        root.getStyleClass().add("root-pane");
//        root.setCenter(tabPane);
//        root.setBottom(container);
//
//        Scene scene = new Scene(root, 800, 600);
//        scene.getStylesheets().add(AdminWindow.class.getResource("/style.css").toExternalForm());
//
//        stage.setScene(scene);
//        stage.show();
//    }
//}
