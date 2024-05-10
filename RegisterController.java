package com.example.crmjavafx;

import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.binding.IntegerExpression;


public class RegisterController implements Initializable {


    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button backButton;

    @FXML
    private Label statusMessageRegister;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label passwordRequirementsLabel;


    String passwordRegex = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){9,16}$";

    private static final String PASSWORD_REQUIREMENTS =
            "Criterii creare parola utilizator: \n" +
            "- Cel putin 8 caractere lungime\n" +
            "- Cel putin o litera mare\n" +
            "- Cel putin o litera mica\n" +
            "- Cel putin o cifra\n" +
            "- Cel putin un caracter special: @$!%*?&";

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            statusMessageRegister.setText("Parola nu indeplineste criteriile stabilite!");
            passwordRequirementsLabel.setText(PASSWORD_REQUIREMENTS);

            return false;
        } else {
            return true;
        }
    }


    @FXML
    void register(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (!validatePassword(password)) {
            return;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String query = "INSERT INTO utilizatori (username, password) VALUES(?,?);";

        try(Connection postgresqlCon = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement pst = postgresqlCon.prepareStatement(query)) {


            pst.setString(1, username);
            pst.setString(2, hashedPassword);
            Integer rowInserted = pst.executeUpdate();


            statusMessageRegister.setText("User-ul a fost inregistrat cu succes! Vei fi acum redirectionat catre Pagina de Login");
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();

            progressBar.setVisible(true);

            try {
                final Timeline[] timeline = {null};
                timeline[0] = new Timeline(new KeyFrame(Duration.seconds(0.1), evt -> {
                    double progressIncrement = 1.0/30;
                    double progress = progressBar.getProgress() + progressIncrement;
                    progressBar.setProgress(progress);
                    if (progress >= 0.99) {
                        timeline[0].stop();
                        logInPage();
                    }
                }));

                timeline[0].setCycleCount(30);
                timeline[0].play();
            }catch (Exception ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, "Error creating timeline", ex);
            }




        }catch ( SQLException ex) {
            Logger lgr = Logger.getLogger(LoginController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

//    @FXML
//    void register(ActionEvent event) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        String selectQuery = "SELECT COUNT(*) FROM utilizatori WHERE username = ?;";
//
//        try(Connection postgresqlCon = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
//            PreparedStatement selectPst = postgresqlCon.prepareStatement(selectQuery)) {
//
//
//            selectPst.setString(1, username);
//
//            try (ResultSet rs = selectPst.executeQuery()) {
//                if (rs.next() && rs.getInt(1) > 0) {
//                    // User exists
//                    statusMessageRegister.setText("Acest nume de utilizator exista deja. Te rog incearca un alt nume de utilizator.");
//                    usernameField.setText("");
//                    passwordField.setText("");
//                    usernameField.requestFocus();
//                    return;
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//
//
//        }catch ( SQLException ex) {
//            Logger lgr = Logger.getLogger(LoginController.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
//        }
//
//        if (!validatePassword(password)) {
//            return;
//        }
//
//
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//
//        String insertQuery = "INSERT INTO utilizatori (username, password) VALUES(?,?);";
//
//        try(Connection postgresqlCon = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
//            PreparedStatement pst = postgresqlCon.prepareStatement(insertQuery)) {
//
//
//            pst.setString(1, username);
//            pst.setString(2, hashedPassword);
//
//
//
//            statusMessageRegister.setText("User-ul a fost inregistrat cu succes! Vei fi acum redirectionat catre Pagina de Login");
//            usernameField.setText("");
//            passwordField.setText("");
//            usernameField.requestFocus();
//
//            progressBar.setVisible(true);
//
//            try {
//                final Timeline[] timeline = {null};
//                timeline[0] = new Timeline(new KeyFrame(Duration.seconds(0.1), evt -> {
//                    double progressIncrement = 1.0/30;
//                    double progress = progressBar.getProgress() + progressIncrement;
//                    progressBar.setProgress(progress);
//                    if (progress >= 0.99) {
//                        timeline[0].stop();
//                        logInPage();
//                    }
//                }));
//
//                timeline[0].setCycleCount(30);
//                timeline[0].play();
//            }catch (Exception ex) {
//                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, "Error creating timeline", ex);
//            }
//
//
//
//
//        }catch ( SQLException ex) {
//            Logger lgr = Logger.getLogger(LoginController.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
//        }
//    }

    @FXML
    void logInPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-page.fxml")));
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.getIcons().add(new Image("CRM-Icon.png"));
            primaryStage.setTitle("Pagina Login");
            primaryStage.setScene( new Scene( root, 667, 496));
            primaryStage.setResizable(false);
            primaryStage.show();


            Stage stage = (Stage) registerButton.getScene().getWindow();
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
            primaryStage.getIcons().add(new Image("CRM-Icon.png"));
            primaryStage.setTitle("Bun venit!");
            primaryStage.setScene( new Scene( root, 667, 496));
            primaryStage.setResizable(false);
            primaryStage.show();

            Stage stage = (Stage) backButton.getScene().getWindow();
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