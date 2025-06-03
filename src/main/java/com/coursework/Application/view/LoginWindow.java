package com.coursework.Application.view;

import com.coursework.Application.service.AuthService;
import com.coursework.Application.util.UserSession;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow {

    public static boolean showLoginDialog() {
        Stage stage = new Stage();
        stage.setTitle("Вход");
        stage.setResizable(false);

        Label userLabel = new Label("Имя пользователя:");
        TextField userField = new TextField();
        userField.getStyleClass().add("text-field");

        Label passLabel = new Label("Пароль:");
        PasswordField passField = new PasswordField();
        passField.getStyleClass().add("password-field");

        Button loginBtn = new Button("Войти");
        loginBtn.getStyleClass().add("action-button");

        Label status = new Label();
        status.getStyleClass().add("status-bar");

        VBox layout = new VBox(10,
                userLabel, userField,
                passLabel, passField,
                loginBtn,
                status
        );
        layout.getStyleClass().add("tab-content-area");
        layout.setPadding(new Insets(20));
        loginBtn.setDefaultButton(true);
        loginBtn.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (AuthService.authenticate(user, pass)) {
                UserSession.login(user);
                stage.close();
            } else {
                status.setText("Неверный логин или пароль");
            }
        });

        stage.setScene(new Scene(layout));
        stage.showAndWait();

        return UserSession.getUsername() != null;
    }
}
