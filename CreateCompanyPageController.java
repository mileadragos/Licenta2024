package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateCompanyPageController implements Initializable {


    @FXML
    private Label statusUpdate;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField companyAddressField;
    @FXML
    private TextField companyPhoneField;
    @FXML
    private TextField companyEmailField;
    @FXML
    private CheckBox companyActiveCheckBox;
    @FXML
    private ComboBox<Users> companyAssigneeComboBox;
    @FXML
    private DatePicker companyCreatedDate;
    @FXML
    private ComboBox<Users> companyCreatedByComboBox;
    @FXML
    private Button backButton;



    //Declararea variabilelor ajutatoare pentru conexiunea la baza de date
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";



    private Companies company;

    @FXML
    public void createCompany() {

        String createdName = companyNameField.getText();
        String createdAddress = companyAddressField.getText();
        String createdPhone = companyPhoneField.getText();
        String createdEmail = companyEmailField.getText();
        LocalDate localDateToday = LocalDate.now();
        Date today = Date.valueOf(localDateToday);
        Boolean activ = companyActiveCheckBox.isSelected();
        Users createdAssigneeUsername = companyAssigneeComboBox.getValue();
        Users createdCreatedByUser = companyCreatedByComboBox.getValue();


        Companies createdCompany = new Companies(
                createdName,
                createdAddress,
                createdPhone,
                createdEmail,
                activ,
                createdAssigneeUsername.getId(),
                today,
                createdCreatedByUser.getId()
        );

        createCompanyInDatabase(createdCompany);
    }

    private void createCompanyInDatabase(Companies createdCompany) {
        String insertQuery = "INSERT INTO companie(nume_companie, adresa_companie, telefon, email, activ, responsabil, data_creare, creat_de)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, createdCompany.getNume_companie());
            pstmt.setString(2, createdCompany.getAdresa_companie());
            pstmt.setString(3, createdCompany.getTelefon());
            pstmt.setString(4, createdCompany.getEmail());

            pstmt.setBoolean(5, createdCompany.getActiv());
            pstmt.setInt(6, createdCompany.getResponsabil());
            pstmt.setDate(7, createdCompany.getData_creare());
            pstmt.setInt(8, createdCompany.getCreat_de());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Compania a fost creata cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(LoginController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }


    }


    private ObservableList<Users> getUsersFromDB() {
        ObservableList<Users> usersData = FXCollections.observableArrayList();
        String query = "SELECT id, username, password, rol FROM public.utilizatori";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String rol = rs.getString("rol");

                usersData.add(new Users(id, username, password, rol));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DetailsLeadPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return usersData;
    }



    public static class UserListCell extends ListCell<Users> {
        @Override
        protected void updateItem(Users user, boolean empty) {
            super.updateItem(user, empty);
            if (user != null) {
                setText(user.getUsername());
            } else {
                setText(null);
            }
        }
    }


    @FXML
    private void back() {
        showChangeWarning();
    }


    private void showChangeWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Creare Companie");
        alert.setHeaderText("Mesaj de confirmare");
        alert.setContentText("Ești sigur că vrei să părăsești această pagină?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        ObservableList<Users> assignedUserList = getUsersFromDB();
        companyAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        companyCreatedByComboBox.setItems(createdByUserList);

        companyAssigneeComboBox.setCellFactory(lv -> new DetailsCompanyPageController.UserListCell());
        companyAssigneeComboBox.setButtonCell(new DetailsCompanyPageController.UserListCell());

        companyCreatedByComboBox.setCellFactory(lv -> new DetailsCompanyPageController.UserListCell());
        companyCreatedByComboBox.setButtonCell(new DetailsCompanyPageController.UserListCell());


    }
}
