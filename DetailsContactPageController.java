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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DetailsContactPageController implements Initializable{


    @FXML
    private TextField contactNameField;
    @FXML
    private TextField contactSurnameField;
    @FXML
    private TextField contactJobField;
    @FXML
    private TextField contactEmailField;
    @FXML
    private TextField contactPhoneField;
    @FXML
    private ComboBox<Companies> contactCompanyComboBox;
    @FXML
    private ComboBox<Users> contactAssigneeComboBox;
    @FXML
    private CheckBox contactActiveCheckBox;
    @FXML
    private DatePicker contactCreatedDate;
    @FXML
    private ComboBox<Users> contactCreatedByComboBox;
    @FXML
    private Button contactUpdateButton;
    @FXML
    private Label statusUpdate;
    @FXML
    private Button backButton;
    @FXML
    private Button createButton;


    //Declararea variabilelor ajutatoare pentru conexiunea la baza de date
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";

    private Contacts contact;


    public void setContactData(Contacts contact) {
        this.contact = contact;
        populateDetails();
    }



    private void populateDetails() {
        contactNameField.setText(contact.getNume_contact());
        contactSurnameField.setText(contact.getPrenume_contact());
        contactJobField.setText(contact.getFunctie());
        contactPhoneField.setText(contact.getTelefon());
        contactEmailField.setText(contact.getEmail());


        if (contact.getCompanie_id() != null) {
            for (Companies company : contactCompanyComboBox.getItems()) {
                if (company.getId().equals(contact.getCompanie_id())) {
                    contactCompanyComboBox.setValue(company);
                    break;
                }
            }

        }
        contactActiveCheckBox.setSelected(contact.getActiv());
        contactCreatedDate.setValue(contact.getData_creare() != null ? contact.getData_creare().toLocalDate() : null);

        ObservableList<Users> userList = getUsersFromDB();
        contactAssigneeComboBox.setItems(userList);
        for (Users user : userList) {
            if (user.getId().equals(contact.getResponsabil())) {
                contactAssigneeComboBox.setValue(user);
                break;
            }
        }


        ObservableList<Users> createdByList = getUsersFromDB();
        contactCreatedByComboBox.setItems(createdByList);
        for (Users user : createdByList) {
            if (user.getId().equals(contact.getCreat_de())) {
                contactCreatedByComboBox.setValue(user);
                break;
            }
        }
    }


    @FXML
    private void updateContact() {

        String newName = contactNameField.getText();
        String newSurname = contactSurnameField.getText();
        String newJob = contactJobField.getText();
        String newEmail = contactEmailField.getText();
        String newPhone = contactPhoneField.getText();
        Companies newCompany = contactCompanyComboBox.getValue();
        Boolean newActive = contactActiveCheckBox.isSelected();
        Users newAssignee = contactAssigneeComboBox.getValue();
        Date newCreatedDate = Date.valueOf(contactCreatedDate.getValue());
        Users newCreatedBy = contactCreatedByComboBox.getValue();



        Contacts updatedContact = new Contacts (
                newName,
                newSurname,
                newJob,
                newEmail,
                newPhone,
                newCompany.getNume_companie(),
                contact.getId(),
                newCompany.getId(),
                newActive,
                newAssignee.getId(),
                newCreatedDate,
                newCreatedBy.getId(),
                newAssignee.getUsername(),
                newCreatedBy.getUsername()
        );

        updateContactInDatabase(updatedContact);
    }


    private void updateContactInDatabase(Contacts updatedContact) {
        String updateQuery = "UPDATE contact SET " +
                "nume_contact = ?, " +
                "prenume_contact = ?, " +
                "functie = ?, " +
                "email = ?, " +
                "telefon = ?, " +
                "companie_id = ?, " +
                "activ = ?, " +
                "responsabil = ?, " +
                "data_creare = ?, " +
                "creat_de = ? " +
                "WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

                pstmt.setString(1, updatedContact.getNume_contact());
                pstmt.setString(2, updatedContact.getPrenume_contact());
                pstmt.setString(3, updatedContact.getFunctie());
                pstmt.setString(4, updatedContact.getEmail());
                pstmt.setString(5, updatedContact.getTelefon());
                Companies selectedCompany = contactCompanyComboBox.getValue();
                if (selectedCompany != null) {
                    pstmt.setInt(6, selectedCompany.getId());
                } else {
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                }
                pstmt.setBoolean(7, updatedContact.getActiv());
                pstmt.setInt(8, updatedContact.getResponsabil());
                pstmt.setDate(9, updatedContact.getData_creare());
                pstmt.setInt(10, updatedContact.getCreat_de());
                pstmt.setInt(11, contact.getId());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Contactul a fost actualizat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DetailsContactPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }



    private ObservableList<Companies> getCompaniesFromDB() {
        ObservableList<Companies> companiesData = FXCollections.observableArrayList();

        String query = "SELECT c.nume_companie, " +
                "c.adresa_companie, " +
                "c.telefon, " +
                "c.email, " +
                "c.id, " +
                "c.activ, " +
                "c.responsabil, " +
                "c.data_creare, " +
                "c.creat_de, " +
                "u.username AS responsabilNume, " +
                "u1.username AS creatDeNume " +
                "FROM Companie c " +
                "LEFT JOIN Utilizatori u ON c.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON c.creat_de = u1.id";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String numeCompanie = rs.getString("nume_companie");
                String adresaCompanie = rs.getString("adresa_companie");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                Integer id = rs.getInt("id");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil = rs.getInt("responsabil");
                Date dataCreare = rs.getDate("data_creare");
                Integer creatDe = rs.getInt("creat_de");
                String responsabilNume = rs.getString("responsabilNume");
                String creatDeNume = rs.getString("creatDeNume");

                companiesData.add(new Companies(numeCompanie, adresaCompanie, telefon, email, id, activ, responsabil, dataCreare, creatDe, responsabilNume, creatDeNume));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(LoginController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return companiesData;
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
            Logger.getLogger(DetailsContactPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return usersData;
    }

    public static class CompanyListCell extends ListCell<Companies> {
        @Override
        protected void updateItem(Companies company, boolean empty) {
            super.updateItem(company, empty);
            if (company != null) {
                setText(company.getNume_companie());
            } else {
                setText(null);
            }
        }
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
        alert.setTitle("Detalii Contact");
        alert.setHeaderText("Mesaj de confirmare");
        alert.setContentText("Ești sigur că vrei să părăsești această pagină?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private void createContact() {
        FXMLLoader loader = new FXMLLoader(CreateCompanyPageController.class.getResource("create-contact-page.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.setScene(new Scene(root, 667, 496));
            stage.setResizable(false);
            stage.getIcons().add(new Image("CRM-Icon.png"));
            stage.setTitle("Creaza Contact nou");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void archiveContact () {
        Boolean activ = false;

        Contacts archivedContact = new Contacts (activ, contact.getId());


        archiveContactInDatabase(archivedContact);

    }

    private void archiveContactInDatabase (Contacts archivedContact) {
        String updateArchiveQuery = "UPDATE contact SET activ=? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(updateArchiveQuery)) {

            pstmt.setBoolean(1, false);
            pstmt.setInt(2, archivedContact.getId());
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Contact-ul a fost arhivat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DetailsContactPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Companies> companyList = getCompaniesFromDB();
        contactCompanyComboBox.setItems(companyList);

        ObservableList<Users> assignedUserList = getUsersFromDB();
        contactAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        contactCreatedByComboBox.setItems(createdByUserList);


        contactCompanyComboBox.setCellFactory(lv -> new DetailsContactPageController.CompanyListCell());
        contactCompanyComboBox.setButtonCell(new DetailsContactPageController.CompanyListCell());

        contactAssigneeComboBox.setCellFactory(lv -> new DetailsContactPageController.UserListCell());
        contactAssigneeComboBox.setButtonCell(new DetailsContactPageController.UserListCell());

        contactCreatedByComboBox.setCellFactory(lv -> new DetailsContactPageController.UserListCell());
        contactCreatedByComboBox.setButtonCell(new DetailsContactPageController.UserListCell());
    }
}


