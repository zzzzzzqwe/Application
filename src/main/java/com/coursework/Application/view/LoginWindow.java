package com.coursework.Application.view;

import com.coursework.Application.service.AuthService;
import com.coursework.Application.util.UserSession;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow {

    public static boolean showLoginDialog() {
        Stage stage = new Stage();
        stage.setTitle("Вход");

        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Войти");
        Label status = new Label();

        final boolean[] success = {false};

        loginBtn.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (AuthService.authenticate(user, pass)) {
                UserSession.login(user);
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
}
