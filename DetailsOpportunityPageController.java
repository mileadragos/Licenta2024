package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetailsOpportunityPageController implements Initializable {



        @FXML
        private TextField opportunityNameField;

        @FXML
        private ComboBox<Companies> opportunityCompanyComboBox;

        @FXML
        private ComboBox<Contacts> opportunityContactComboBox;

        @FXML
        private TextField opportunityEstRevField;

        @FXML
        private TextField opportunityActRevField;

        @FXML
        private ComboBox<String> opportunityStatusComboBox;

        @FXML
        private Label statusUpdate;

        @FXML
        private CheckBox opportunityActiveCheckBox;

        @FXML
        private ComboBox<Users> opportunityAssigneeComboBox;

        @FXML
        private DatePicker opportunityCreatedDate;

        @FXML
        private ComboBox<Users> opportunityCreatedByComboBox;


        private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        private static final String dbUser = "postgres";
        private static final String dbPassword = "dragos";


        private Opportunities opportunity;


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
                Logger.getLogger(com.example.crmjavafx.DetailsOpportunityPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }

            return contactsData;
        }


        public void updateOpportunity() {

            String updatedName = opportunityNameField.getText();
            Companies updatedCompany = opportunityCompanyComboBox.getValue();
            Contacts updatedContact = opportunityContactComboBox.getValue();
            String updatedStatus = opportunityStatusComboBox.getValue();

            String updatedEstRevStr = opportunityEstRevField.getText();
            String updatedActRevStr = opportunityActRevField.getText();
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

            BigDecimal updatedActRev;
            try {
                if (updatedActRevStr.isEmpty()) {
                    updatedActRev = null;
                } else {
                    updatedActRev = new BigDecimal(updatedActRevStr);
                }
            } catch (NumberFormatException ex) {
                statusUpdate.setText("Valoare invalida pentru Venit Real. Te rog sa folosesti un numar!");
                return;
            }


            Boolean updatedActive = opportunityActiveCheckBox.isSelected();
            Users updatedAssignee = opportunityAssigneeComboBox.getValue();
            Date updatedDate = Date.valueOf(opportunityCreatedDate.getValue());
            Users updatedCreatedBy = opportunityCreatedByComboBox.getValue();
            Users updatedResponsabil =opportunityAssigneeComboBox.getValue();
            Users updatedCreatDe = opportunityCreatedByComboBox.getValue();

            Opportunities updatedOpportunity = new Opportunities(
                    opportunity.getId(),
                    updatedName,
                    updatedCompany.getId(),
                    updatedCompany.getNume_companie(),
                    updatedContact.getId(),
                    updatedContact.getPrenume_contact() + " " + updatedContact.getNume_contact(),
                    updatedEstRev,
                    updatedActRev,
                    updatedStatus,
                    updatedActive,
                    updatedAssignee.getId(),
                    updatedResponsabil.getUsername(),
                    updatedDate,
                    updatedCreatedBy.getId(),
                    updatedCreatDe.getUsername()
            );

            updateOpportunityInDatabase(updatedOpportunity);

        }



        private void updateOpportunityInDatabase(Opportunities updatedOpportunity) {
            String updateSql = "UPDATE oportunitate SET nume_oportunitate = ?," +
                    " companie_id = ?, " +
                    "contact_id = ?, " +
                    "venit_estimat = ?, " +
                    "venit_real = ?, " +
                    "status = ?, " +
                    "activ = ?, " +
                    "responsabil = ?, " +
                    "data_creare = ?, " +
                    "creat_de = ? " +
                    "WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(updateSql)) {

                stmt.setString(1, updatedOpportunity.getNume_oportunitate());
                stmt.setInt(2, updatedOpportunity.getCompanie_id());
                stmt.setInt(3, updatedOpportunity.getContact_id());
                stmt.setBigDecimal(4, updatedOpportunity.getVenit_estimat());
                stmt.setBigDecimal(5, updatedOpportunity.getVenit_real());
                stmt.setString(6, updatedOpportunity.getStatus());
                stmt.setBoolean(7, updatedOpportunity.getActiv());
                stmt.setInt(8, updatedOpportunity.getResponsabil_id());
                stmt.setDate(9, updatedOpportunity.getData_creare());
                stmt.setInt(10,updatedOpportunity.getCreat_de_id());
                stmt.setInt(11, updatedOpportunity.getId());

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    statusUpdate.setText("Oportunitatea a fost actualizata cu succes!");

                } else {
                    statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
                }

            } catch (SQLException ex) {
                Logger.getLogger(com.example.crmjavafx.DetailsOpportunityPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }




        public void setOpportunityData(Opportunities opportunity) {
            this.opportunity = opportunity;
            populateDetails();
        }


        private void populateDetails() {
            opportunityNameField.setText(opportunity.getNume_oportunitate());
            opportunityEstRevField.setText(String.valueOf(opportunity.getVenit_estimat()));
            opportunityActRevField.setText(String.valueOf(opportunity.getVenit_real()));


            if (opportunity.getCompanie_id() != null) {
                for (Companies company : opportunityCompanyComboBox.getItems()) {
                    if (company.getId().equals(opportunity.getCompanie_id())) {
                        opportunityCompanyComboBox.setValue(company);
                        break;
                    }
                }
            }


            if (opportunity.getContact_id() != null) {
                for (Contacts contact : opportunityContactComboBox.getItems()) {
                    if (contact.getId().equals(opportunity.getContact_id())) {
                        opportunityContactComboBox.setValue(contact);
                        break;
                    }
                }
            }

            opportunityStatusComboBox.setItems(getOpportunityStatuses());
            opportunityStatusComboBox.setValue(opportunity.getStatus());
            opportunityActiveCheckBox.setSelected(opportunity.getActiv());

            opportunityCreatedDate.setValue(opportunity.getData_creare() != null ? opportunity.getData_creare().toLocalDate() : null);


            ObservableList<Users> userList = getUsersFromDB();
            opportunityAssigneeComboBox.setItems(userList);
            for (Users user : userList) {
                if (user.getId().equals(opportunity.getResponsabil_id())) {
                    opportunityAssigneeComboBox.setValue(user);
                    break;
                }
            }


            ObservableList<Users> createdByList = getUsersFromDB();
            opportunityCreatedByComboBox.setItems(createdByList);
            for (Users user : createdByList) {
                if (user.getId().equals(opportunity.getCreat_de_id())) {
                    opportunityCreatedByComboBox.setValue(user);
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
                Logger.getLogger(com.example.crmjavafx.DetailsOpportunityPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }

            return usersData;
        }

        private ObservableList<String> getOpportunityStatuses() {
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
            opportunityCompanyComboBox.setItems(companyList);

            ObservableList<Contacts> contactList = getContactsFromDB();
            opportunityContactComboBox.setItems(contactList);

            ObservableList<Users> assignedUserList = getUsersFromDB();
            opportunityAssigneeComboBox.setItems(assignedUserList);

            ObservableList<Users> createdByUserList = getUsersFromDB();
            opportunityCreatedByComboBox.setItems(createdByUserList);



            opportunityCompanyComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsOpportunityPageController.CompanyListCell());
            opportunityCompanyComboBox.setButtonCell(new com.example.crmjavafx.DetailsOpportunityPageController.CompanyListCell());

            opportunityContactComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsOpportunityPageController.ContactListCell());
            opportunityContactComboBox.setButtonCell(new com.example.crmjavafx.DetailsOpportunityPageController.ContactListCell());

            opportunityAssigneeComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsOpportunityPageController.UserListCell());
            opportunityAssigneeComboBox.setButtonCell(new com.example.crmjavafx.DetailsOpportunityPageController.UserListCell());

            opportunityCreatedByComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsOpportunityPageController.UserListCell());
            opportunityCreatedByComboBox.setButtonCell(new com.example.crmjavafx.DetailsOpportunityPageController.UserListCell());


        }
    }


