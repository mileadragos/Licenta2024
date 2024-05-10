package com.example.crmjavafx;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {


    @FXML
    public Button logInButton;

    @FXML
    public Button registerWelcomeButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logInButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login-page.fxml"));

            try {
                Parent root = loader.load();
                Stage stage = (Stage) logInButton.getScene().getWindow();
                stage.setScene(new Scene(root, 667, 496));
                stage.setResizable(false);
                stage.getIcons().add(new Image("CRM-Icon.png"));
                stage.setTitle("Pagina Login");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        registerWelcomeButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(RegisterController.class.getResource("register-page.fxml"));

            try {
                Parent root = loader.load();
                Stage stage = (Stage) registerWelcomeButton.getScene().getWindow();
                stage.setScene((new Scene(root, 667, 496)));
                stage.setResizable(false);
                stage.getIcons().add(new Image("CRM-Icon.png"));
                stage.setTitle("Pagina Inregistrare");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}