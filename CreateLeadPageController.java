package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateLeadPageController implements Initializable {


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


    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";


    private Leads lead;



    public void createLead() {

        String updatedName = leadNameField.getText();
        Companies updatedCompany = leadCompanyComboBox.getValue();
        Contacts updatedContact = leadContactComboBox.getValue();
        String updatedJob = leadJobField.getText();
        String updatedPhone = leadPhoneField.getText();
        String updatedEmail = leadEmailField.getText();
        String updatedStatus = getLeadStatuses().toString();

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
        LocalDate localDateToday = LocalDate.now();
        Date today = Date.valueOf(localDateToday);
        Users updatedCreatedBy = leadCreatedByComboBox.getValue();
        Users updatedResponsabil =leadAssigneeComboBox.getValue();
        Users updatedCreatDe = leadCreatedByComboBox.getValue();

        Leads createdLead = new Leads(
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
                today,
                updatedCreatedBy.getId(),
                updatedResponsabil.getUsername(),
                updatedCreatDe.getUsername()
        );

        createLeadInDatabase(createdLead);

    }


    private void createLeadInDatabase(Leads createdLead) {
        String createSql =  "INSERT INTO lead (nume_lead, companie_id, contact_id, functie, telefon, email, venit_estimat, status, activ, responsabil, data_creare, creat_de) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(createSql)) {

            stmt.setString(1, createdLead.getNume_lead());
            stmt.setInt(2, createdLead.getCompanie_id());
            stmt.setInt(3, createdLead.getContact_id());
            stmt.setString(4, createdLead.getFunctie());
            stmt.setString(5, createdLead.getTelefon());
            stmt.setString(6, createdLead.getEmail());
            stmt.setBigDecimal(7, createdLead.getVenit_estimat());
            stmt.setString(8, createdLead.getStatus());
            stmt.setBoolean(9, createdLead.getActiv());
            stmt.setInt(10, createdLead.getResponsabilId());
            stmt.setDate(11, createdLead.getData_creare());
            stmt.setInt(12,createdLead.getCreat_deId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Lead-ul a fost creat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CreateLeadPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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

        leadStatusComboBox.setItems(getLeadStatuses());


        leadCompanyComboBox.setCellFactory(lv -> new CreateLeadPageController.CompanyListCell());
        leadCompanyComboBox.setButtonCell(new CreateLeadPageController.CompanyListCell());

        leadContactComboBox.setCellFactory(lv -> new CreateLeadPageController.ContactListCell());
        leadContactComboBox.setButtonCell(new CreateLeadPageController.ContactListCell());

        leadAssigneeComboBox.setCellFactory(lv -> new CreateLeadPageController.UserListCell());
        leadAssigneeComboBox.setButtonCell(new CreateLeadPageController.UserListCell());

        leadCreatedByComboBox.setCellFactory(lv -> new CreateLeadPageController.UserListCell());
        leadCreatedByComboBox.setButtonCell(new CreateLeadPageController.UserListCell());



    }
}
