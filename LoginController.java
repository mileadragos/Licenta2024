package com.example.crmjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";

    @FXML
    private Button signInButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusMessageLogin;



    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText();

        String password = passwordField.getText();

        String query = "SELECT * FROM utilizatori WHERE username = ?;";

        try(Connection postgresqlCon = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement pst = postgresqlCon.prepareStatement(query)) {


            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                if (BCrypt.checkpw(password, hashedPassword)) {

                    mainPage();

                }else{
                    statusMessageLogin.setText("Parolă incorectă! Te rog să încerci din nou.");
                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            } else {
              statusMessageLogin.setText("Utilizatorul nu a fost găsit! Te rog să încerci din nou.");
              usernameField.setText("");
              passwordField.setText("");

            }

        }catch ( SQLException ex) {
            Logger lgr = Logger.getLogger(LoginController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage() ,ex);
        }
    }

    @FXML
    void mainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("main-page.fxml")));

            Parent root = loader.load();
            MainPageController mainPageController = loader.getController();
            String username = usernameField.getText();
            Username currentUser = new Username(username);

            mainPageController.setNavigationMessage(currentUser);
            mainPageController.setLoggedInMessage(currentUser);
            mainPageController.getUser(currentUser);
            mainPageController.handleShowHome();

            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setTitle("Pagina Principală");
            primaryStage.setScene( new Scene( root, 1000, 800));
            primaryStage.getIcons().add(new Image("CRM-Icon.png"));
            primaryStage.setMaximized(true);

            primaryStage.show();

            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void back() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome-view.fxml")));
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.DECORATED);

            primaryStage.setScene( new Scene( root, 667, 496));
            primaryStage.setTitle("Bun venit!");
            primaryStage.getIcons().add(new Image("CRM-Icon.png"));
            primaryStage.setResizable(false);
            primaryStage.show();

            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //to be extended
    }
}

