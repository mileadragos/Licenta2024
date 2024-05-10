package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DetailsCompanyPageController implements Initializable {


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
    @FXML
    private Button createCompanyButton;


    //Declararea variabilelor ajutatoare pentru conexiunea la baza de date
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";

    private Companies company;


    public void setCurrentUserId(Integer currentUserId) {
    }

    public void setCompanyData(Companies company) {
        this.company = company;
        populateDetails();

    }

    private void populateDetails() {
        companyNameField.setText(company.getNume_companie());
        companyAddressField.setText(company.getAdresa_companie());
        companyPhoneField.setText(company.getTelefon());
        companyEmailField.setText(company.getEmail());
        companyActiveCheckBox.setSelected(company.getActiv());

        companyCreatedDate.setValue(company.getData_creare() != null ? company.getData_creare().toLocalDate() : null);


        ObservableList<Users> userList = getUsersFromDB();
        companyAssigneeComboBox.setItems(userList);
        for (Users user : userList) {
            if (user.getId().equals(company.getResponsabil())) {
                companyAssigneeComboBox.setValue(user);
                break;
            }
        }


        ObservableList<Users> createdByList = getUsersFromDB();
        companyCreatedByComboBox.setItems(createdByList);
        for (Users user : createdByList) {
            if (user.getId().equals(company.getCreat_de())) {
                companyCreatedByComboBox.setValue(user);
                break;
            }
        }

    }


    @FXML
    public void updateCompany() {

        String updatedName = companyNameField.getText();
        String updatedAddress = companyAddressField.getText();
        String updatedPhone = companyPhoneField.getText();
        String updatedEmail = companyEmailField.getText();
        Boolean updatedActive = companyActiveCheckBox.isSelected();
        Users updatedAssignee = companyAssigneeComboBox.getValue();
        Date updatedDate = Date.valueOf(companyCreatedDate.getValue());
        Users updatedCreatedBy = companyCreatedByComboBox.getValue();
        Users updatedAssigneeUsername = companyAssigneeComboBox.getValue();
        Users updateCreatedByUsername = companyCreatedByComboBox.getValue();


        Companies updatedCompany = new Companies(
                updatedName,
                updatedAddress,
                updatedPhone,
                updatedEmail,
                company.getId(),
                updatedActive,
                updatedAssignee.getId(),
                updatedDate,
                updatedCreatedBy.getId(),
                updatedAssigneeUsername.getUsername(),
                updateCreatedByUsername.getUsername()

        );
        
        updateCompanyInDatabase(updatedCompany);
    }

    private void updateCompanyInDatabase(Companies updatedCompany) {
        String updateQuery = "UPDATE companie SET nume_companie=?, adresa_companie=?, telefon=?, email=?, activ=?, responsabil=?, data_creare=?, creat_de=? WHERE id = ?"; // Assuming you have an 'id' field in your Contacts class
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, updatedCompany.getNume_companie());
            pstmt.setString(2, updatedCompany.getAdresa_companie());
            pstmt.setString(3, updatedCompany.getTelefon());
            pstmt.setString(4, updatedCompany.getEmail());

            pstmt.setBoolean(5, updatedCompany.getActiv());
            pstmt.setInt(6, updatedCompany.getResponsabil());
            pstmt.setDate(7, updatedCompany.getData_creare());
            pstmt.setInt(8, updatedCompany.getCreat_de());
            pstmt.setInt(9, updatedCompany.getId());
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Compania a fost actualizata cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(LoginController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }


    }

    @FXML
    public void archiveCompany () {
        Boolean activ = false;

        Companies archivedCompany = new Companies (activ, company.getId());


        archiveCompanyInDatabase(archivedCompany);

    }


    private void archiveCompanyInDatabase (Companies archivedCompany) {
        String updateArchiveQuery = "UPDATE companie SET activ=? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(updateArchiveQuery)) {

            pstmt.setBoolean(1, false);
            pstmt.setInt(2, archivedCompany.getId());
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Compania a fost arhivată cu succes!");

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
        alert.setTitle("Detalii Companie");
        alert.setHeaderText("Mesaj de confirmare");
        alert.setContentText("Ești sigur că vrei să părăsești această pagină?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private void createCompany() {
        FXMLLoader loader = new FXMLLoader(CreateCompanyPageController.class.getResource("create-company-page.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) createCompanyButton.getScene().getWindow();
            stage.setScene(new Scene(root, 667, 496));
            stage.setResizable(false);
            stage.getIcons().add(new Image("CRM-Icon.png"));
            stage.setTitle("Creaza Companie noua");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
