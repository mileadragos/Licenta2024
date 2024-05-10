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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetailsLeadPageController implements Initializable {

    @FXML
    private TextField leadNameField;

    @FXML
    private ComboBox<Companies> leadCompanyComboBox;

    @FXML
    private ComboBox<Contacts> leadContactComboBox;

    @FXML
    private TextField leadJobField;

    @FXML
    private TextField leadPhoneField;

    @FXML
    private TextField leadEmailField;

    @FXML
    private TextField leadEstRevField;

    @FXML
    private ComboBox<String> leadStatusComboBox;

    @FXML
    private Label statusUpdate;

    @FXML
    private CheckBox leadActiveCheckBox;

    @FXML
    private ComboBox<Users> leadAssigneeComboBox;

    @FXML
    private DatePicker leadCreatedDate;

    @FXML
    private ComboBox<Users> leadCreatedByComboBox;

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";


    private Leads lead;


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

    private ObservableList<Contacts> getContactsFromDB() {
        ObservableList<Contacts> contactsData = FXCollections.observableArrayList();
        String query = "SELECT contact.companie_id, " +
                "contact.id, " +
                "contact.nume_contact, " +
                "contact.prenume_contact, " +
                "contact.functie, " +
                "contact.email, " +
                "contact.activ, " +
                "contact.responsabil, " +
                "contact.data_creare, " +
                "contact.creat_de, " +
                "contact.telefon, " +
                "companie.nume_companie AS companieNume, " +
                "u.username AS responsabilNume, " +
                "u1.username AS creatDeNume " +
                " FROM contact " +
                "LEFT JOIN companie ON contact.companie_id = companie.id " +
                "LEFT JOIN utilizatori u ON contact.responsabil = u.id " +
                "LEFT JOIN utilizatori u1 ON contact.creat_de = u1.id";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String numeContact = rs.getString("nume_contact");
                String prenumeContact = rs.getString("prenume_contact");
                String functie = rs.getString("functie");
                String email = rs.getString("email");
                String telefon = rs.getString("telefon");
                String companieNume = rs.getString("companieNume");
                Integer id = rs.getInt("id");
                Integer companie_id = rs.getInt("companie_id");

                Boolean activ = rs.getBoolean("activ");
                Integer responsabil = rs.getInt("responsabil");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de = rs.getInt("creat_de");
                String responsabilNume = rs.getString("responsabilNume");
                String creatDeNume = rs.getString("creatDeNume");
                contactsData.add(new Contacts(numeContact, prenumeContact, functie, email, telefon, companieNume, id, companie_id, activ, responsabil, data_creare, creat_de, responsabilNume, creatDeNume));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DetailsLeadPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return contactsData;
    }


    public void updateLead() {

        String updatedName = leadNameField.getText();
        Companies updatedCompany = leadCompanyComboBox.getValue();
        Contacts updatedContact = leadContactComboBox.getValue();
        String updatedJob = leadJobField.getText();
        String updatedPhone = leadPhoneField.getText();
        String updatedEmail = leadEmailField.getText();
        String updatedStatus = leadStatusComboBox.getValue();

        String updatedEstRevStr = leadEstRevField.getText();
        BigDecimal updatedEstRev;
        try {
            if (updatedEstRevStr.isEmpty()) {
                updatedEstRev = null;
            } else {
                updatedEstRev = new BigDecimal(updatedEstRevStr);
            }
        } catch (NumberFormatException ex) {
            statusUpdate.setText("Valoare invalida pentru Venit Estimat. Te rog sa folosesti un numar!");
            return;
        }

        Boolean updatedActive = leadActiveCheckBox.isSelected();
        Users updatedAssignee = leadAssigneeComboBox.getValue();
        Date updatedDate = Date.valueOf(leadCreatedDate.getValue());
        Users updatedCreatedBy = leadCreatedByComboBox.getValue();
        Users updatedResponsabil =leadAssigneeComboBox.getValue();
        Users updatedCreatDe = leadCreatedByComboBox.getValue();

        Leads updatedLead = new Leads(
                updatedName,
                updatedCompany.getNume_companie(),
                updatedContact.getPrenume_contact() + " " + updatedContact.getNume_contact(),
                updatedJob,
                updatedPhone,
                updatedEmail,
                updatedEstRev,
                updatedStatus,
                lead.getId(),
                updatedCompany.getId(),
                updatedContact.getId(),
                updatedActive,
                updatedAssignee.getId(),
                updatedDate,
                updatedCreatedBy.getId(),
                updatedResponsabil.getUsername(),
                updatedCreatDe.getUsername()
                );

        updateLeadInDatabase(updatedLead);

    }



    private void updateLeadInDatabase(Leads updatedLead) {
        String updateSql = "UPDATE lead SET nume_lead = ?," +
                " companie_id = ?, " +
                "contact_id = ?, " +
                "functie = ?, " +
                "telefon = ?, " +
                "email = ?, " +
                "venit_estimat = ?, " +
                "status = ?, " +
                "activ = ?, " +
                "responsabil = ?, " +
                "data_creare = ?, " +
                "creat_de = ? " +
                "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, updatedLead.getNume_lead());
            stmt.setInt(2, updatedLead.getCompanie_id());
            stmt.setInt(3, updatedLead.getContact_id());
            stmt.setString(4, updatedLead.getFunctie());
            stmt.setString(5, updatedLead.getTelefon());
            stmt.setString(6, updatedLead.getEmail());
            stmt.setBigDecimal(7, updatedLead.getVenit_estimat());
            stmt.setString(8, updatedLead.getStatus());
            stmt.setBoolean(9, updatedLead.getActiv());
            stmt.setInt(10, updatedLead.getResponsabilId());
            stmt.setDate(11, updatedLead.getData_creare());
            stmt.setInt(12,updatedLead.getCreat_deId());
            stmt.setInt(13, updatedLead.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Lead-ul a fost actualizat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DetailsLeadPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }




    public void setLeadData(Leads lead) {
        this.lead = lead;
        populateDetails();
    }


    private void populateDetails() {
        leadNameField.setText(lead.getNume_lead());
        leadPhoneField.setText(lead.getTelefon());
        leadEmailField.setText(lead.getEmail());
        leadJobField.setText(lead.getFunctie());
        leadEstRevField.setText(String.valueOf(lead.getVenit_estimat()));



        if (lead.getCompanie_id() != null) {
            for (Companies company : leadCompanyComboBox.getItems()) {
                if (company.getId().equals(lead.getCompanie_id())) {
                    leadCompanyComboBox.setValue(company);
                    break;
                }
            }
        }


        if (lead.getContact_id() != null) {
            for (Contacts contact : leadContactComboBox.getItems()) {
                if (contact.getId().equals(lead.getContact_id())) {
                    leadContactComboBox.setValue(contact);
                    break;
                }
            }
        }

        leadStatusComboBox.setItems(getLeadStatuses());
        leadStatusComboBox.setValue(lead.getStatus());
        leadActiveCheckBox.setSelected(lead.getActiv());

        leadCreatedDate.setValue(lead.getData_creare() != null ? lead.getData_creare().toLocalDate() : null);


        ObservableList<Users> userList = getUsersFromDB();
        leadAssigneeComboBox.setItems(userList);
        for (Users user : userList) {
            if (user.getId().equals(lead.getResponsabilId())) {
                leadAssigneeComboBox.setValue(user);
                break;
            }
        }


        ObservableList<Users> createdByList = getUsersFromDB();
        leadCreatedByComboBox.setItems(createdByList);
        for (Users user : createdByList) {
            if (user.getId().equals(lead.getCreat_deId())) {
                leadCreatedByComboBox.setValue(user);
                break;
            }
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

    private ObservableList<String> getLeadStatuses() {
        return FXCollections.observableArrayList(
                "dezvoltare", "in progres", "negociere", "castigat", "pierdut"
        );
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


    public static class ContactListCell extends ListCell<Contacts> {
        @Override
        protected void updateItem(Contacts contact, boolean empty) {
            super.updateItem(contact, empty);
            if (contact != null) {
                setText(contact.getPrenume_contact() + " " + contact.getNume_contact());
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
    private void createLead() {
        FXMLLoader loader = new FXMLLoader(CreateCompanyPageController.class.getResource("create-lead-page.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.setScene(new Scene(root, 667, 496));
            stage.setResizable(false);
            stage.getIcons().add(new Image("CRM-Icon.png"));
            stage.setTitle("Creaza Lead nou");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    @FXML
    public void archiveLead () {
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
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Companies> companyList = getCompaniesFromDB();
        leadCompanyComboBox.setItems(companyList);

        ObservableList<Contacts> contactList = getContactsFromDB();
        leadContactComboBox.setItems(contactList);

        ObservableList<Users> assignedUserList = getUsersFromDB();
        leadAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        leadCreatedByComboBox.setItems(createdByUserList);



        leadCompanyComboBox.setCellFactory(lv -> new CompanyListCell());
        leadCompanyComboBox.setButtonCell(new CompanyListCell());

        leadContactComboBox.setCellFactory(lv -> new ContactListCell());
        leadContactComboBox.setButtonCell(new ContactListCell());

        leadAssigneeComboBox.setCellFactory(lv -> new UserListCell());
        leadAssigneeComboBox.setButtonCell(new UserListCell());

        leadCreatedByComboBox.setCellFactory(lv -> new UserListCell());
        leadCreatedByComboBox.setButtonCell(new UserListCell());


    }
}
