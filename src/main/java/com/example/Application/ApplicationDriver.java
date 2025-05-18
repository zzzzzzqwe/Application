package com.example.Application;
import com.example.Application.service.AuthService;
import com.example.Application.service.ScheduleService;
import com.example.Application.service.TeacherService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import com.example.Application.service.RoomService;
import java.util.List;
import java.util.Objects;

public class ApplicationDriver extends Application {

    private TextArea outputArea;
    private static boolean isLoggedIn = false;
    private static String currentUsername = null;

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
        outputArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        outputArea.setPrefRowCount(10);

        // Вход
        Button loginBtn = new Button("Войти");
        loginBtn.setStyle("-fx-background-color: #4285f4; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> {
            if (LoginWindow.showLoginDialog()) {
                isLoggedIn = true;
                currentUsername = LoginWindow.getCurrentUsername();
                outputArea.appendText("Вход выполнен: " + currentUsername + "\n");
                loginBtn.setDisable(true);
            } else {
                outputArea.appendText("Вход не выполнен\n");
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(spacer, loginBtn);
        topBar.setPadding(new Insets(10, 10, 5, 10));

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createRoomsTab(),
                createTeachersTab(),
                createSearchTab(),
                createScheduleTab()
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

    private Tab createRoomsTab() {
        Tab tab = new Tab("Аудитории");

        Button allRoomsBtn = new Button("Все");
        Button busyRoomsBtn = new Button("Занятые");
        Button freeRoomsBtn = new Button("Свободные");

        allRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getAllRoomsWithCurrentTeachers()));
        busyRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getOccupiedRooms()));
        freeRoomsBtn.setOnAction(e -> outputArea.setText(RoomService.getFreeRooms()));

        HBox buttons = new HBox(10, allRoomsBtn, busyRoomsBtn, freeRoomsBtn);
        buttons.setPadding(new Insets(10));

        VBox content = new VBox(buttons);
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }

    private Tab createTeachersTab() {
        Tab tab = new Tab("Преподаватели");

        Button allTeachersBtn = new Button("Показать всех");
        allTeachersBtn.setOnAction(e -> outputArea.setText(TeacherService.getAllTeachers()));

        VBox content = new VBox(10, allTeachersBtn);
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }

    private Tab createSearchTab() {
        Tab tab = new Tab("Поиск");

        TextField roomInput = new TextField();
        roomInput.setPromptText("Введите номер аудитории");
        Button searchRoomBtn = new Button("Найти аудиторию");
        searchRoomBtn.setOnAction(e -> {
            String input = roomInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(RoomService.searchRoomByNumber(input));
            }
        });

        TextField teacherInput = new TextField();
        teacherInput.setPromptText("Введите имя или фамилию преподавателя");
        List<String> teacherNames = TeacherService.getAllTeacherNames();
        TextFields.bindAutoCompletion(teacherInput, teacherNames);

        Button searchTeacherBtn = new Button("Найти преподавателя");
        searchTeacherBtn.setOnAction(e -> {
            String input = teacherInput.getText().trim();
            if (!input.isEmpty()) {
                outputArea.setText(TeacherService.searchTeachersByName(input));
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

    private Tab createScheduleTab() {
        Tab tab = new Tab("Расписание");

        Button showScheduleBtn = new Button("Показать расписание");
        showScheduleBtn.setOnAction(e -> outputArea.setText(ScheduleService.getSchedule()));

        VBox content = new VBox(10, showScheduleBtn);
        content.setPadding(new Insets(10));
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }

    public static class LoginWindow {

        private static String currentUsername = null;

        public static boolean showLoginDialog() {
            Stage stage = new Stage();
            stage.setTitle("Вход");

            TextField userField = new TextField();
            PasswordField passField = new PasswordField();
            Button loginBtn = new Button("Войти");
            Label status = new Label();

            final boolean[] success = {false};

            loginBtn.setOnAction(e -> {
                String user = userField.getText();
                String pass = passField.getText();
                if (AuthService.authenticate(user, pass)) {
                    currentUsername = user;
                    success[0] = true;
                    stage.close();
                } else {
                    status.setText("Неверный логин или пароль");
                }
            });

            VBox layout = new VBox(10,
                    new Label("Имя пользователя:"), userField,
                    new Label("Пароль:"), passField,
                    loginBtn,
                    status
            );
            layout.setPadding(new Insets(15));

            stage.setScene(new Scene(layout));
            stage.showAndWait();

            return success[0];
        }

        public static String getCurrentUsername() {
            return currentUsername;
        }
    }
}
