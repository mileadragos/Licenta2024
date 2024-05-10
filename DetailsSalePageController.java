package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetailsSalePageController implements Initializable {


    @FXML
    private TextField salesPriceField;

    @FXML
    private ComboBox<Companies> saleCompanyComboBox;

    @FXML
    private ComboBox<Contacts> saleContactComboBox;

    @FXML
    private ComboBox<Products> saleProductComboBox;


    @FXML
    private TextField saleQuantityField;

    @FXML
    private TextField saleTotalField;

    @FXML
    private Label statusUpdate;

    @FXML
    private CheckBox saleActiveCheckBox;

    @FXML
    private ComboBox<Users> saleAssigneeComboBox;

    @FXML
    private DatePicker saleCreatedDate;

    @FXML
    private ComboBox<Users> saleCreatedByComboBox;


    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";


    private Sales sale;


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
            Logger lgr = Logger.getLogger(DetailsSalePageController.class.getName());
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
            Logger.getLogger(com.example.crmjavafx.DetailsSalePageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return contactsData;
    }


    public void updateSale() {

        Companies updatedCompany = saleCompanyComboBox.getValue();
        Contacts updatedContact = saleContactComboBox.getValue();
        Products updatedProduct = saleProductComboBox.getValue();
        Double updatedPrice = Double.valueOf(salesPriceField.getText());
        Integer updatedQuantity = Integer.valueOf(saleQuantityField.getText());
        Float updatedTotal = Float.valueOf(saleTotalField.getText());


        Boolean updatedActive = saleActiveCheckBox.isSelected();
        Users updatedAssignee = saleAssigneeComboBox.getValue();
        Date updatedDate = Date.valueOf(saleCreatedDate.getValue());
        Users updatedCreatedBy = saleCreatedByComboBox.getValue();
        Users updatedResponsabil = saleAssigneeComboBox.getValue();
        Users updatedCreatDe = saleCreatedByComboBox.getValue();

        Sales updatedSale = new Sales(
                sale.getId(),
                updatedCompany.getId(),
                updatedCompany.getNume_companie(),
                updatedContact.getId(),
                updatedContact.getPrenume_contact() + " " + updatedContact.getNume_contact(),
                updatedProduct.getId(),
                updatedProduct.getNume(),
                updatedQuantity,
                updatedTotal,
                updatedActive,
                updatedAssignee.getId(),
                updatedResponsabil.getUsername(),
                updatedDate,
                updatedCreatedBy.getId(),
                updatedCreatDe.getUsername(),
                updatedPrice
        );

        updateSaleInDatabase(updatedSale);

    }



    private void updateSaleInDatabase(Sales updatedSale) {
        String updateSql = "UPDATE vanzare SET " +
                " companie_id = ?, " +
                "contact_id = ?, " +
                "produs_id = ?, " +
                "cantitate = ?, " +
                "total = ?, " +
                "activ = ?, " +
                "responsabil = ?, " +
                "data_creare = ?, " +
                "creat_de = ?, " +
                "pret = ? " +
                "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setInt(1, updatedSale.getCompanie_id());
            stmt.setInt(2, updatedSale.getContact_id());
            stmt.setInt(3, updatedSale.getProdus_id());
            stmt.setInt(4, updatedSale.getCantitate());
            stmt.setFloat(5, updatedSale.getTotal());
            stmt.setBoolean(6, updatedSale.getActiv());
            stmt.setInt(7, updatedSale.getResponsabil_id());
            stmt.setDate(8, updatedSale.getData_creare());
            stmt.setInt(9,updatedSale.getCreat_de_id());
            stmt.setDouble(10, updatedSale.getPret());
            stmt.setInt(11, updatedSale.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Vanzarea a fost actualizat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(com.example.crmjavafx.DetailsSalePageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }




    public void setSaleData(Sales sale) {
        this.sale = sale;
        populateDetails();
    }


    private void populateDetails() {


        if (sale.getCompanie_id() != null) {
            for (Companies company : saleCompanyComboBox.getItems()) {
                if (company.getId().equals(sale.getCompanie_id())) {
                    saleCompanyComboBox.setValue(company);
                    break;
                }
            }
        }


        if (sale.getContact_id() != null) {
            for (Contacts contact : saleContactComboBox.getItems()) {
                if (contact.getId().equals(sale.getContact_id())) {
                    saleContactComboBox.setValue(contact);
                    break;
                }
            }
        }

        if (sale.getProdus_id() != null) {
            for (Products product : saleProductComboBox.getItems()) {
                if (product.getId().equals(sale.getProdus_id())) {
                    saleProductComboBox.setValue(product);
                    break;
                }
            }
        }

        saleQuantityField.setText(String.valueOf(sale.getCantitate()));
        saleTotalField.setText(String.valueOf(sale.getTotal()));
        saleActiveCheckBox.setSelected(sale.getActiv());

        saleCreatedDate.setValue(sale.getData_creare() != null ? sale.getData_creare().toLocalDate() : null);


        ObservableList<Users> userList = getUsersFromDB();
        saleAssigneeComboBox.setItems(userList);
        for (Users user : userList) {
            if (user.getId().equals(sale.getResponsabil_id())) {
                saleAssigneeComboBox.setValue(user);
                break;
            }
        }


        ObservableList<Users> createdByList = getUsersFromDB();
        saleCreatedByComboBox.setItems(createdByList);
        for (Users user : createdByList) {
            if (user.getId().equals(sale.getCreat_de_id())) {
                saleCreatedByComboBox.setValue(user);
                break;
            }
        }

        salesPriceField.setText(String.valueOf(sale.getPret()));
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
            Logger.getLogger(com.example.crmjavafx.DetailsSalePageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return usersData;
    }

    private ObservableList<Products> getProductsFromDB() {
        ObservableList<Products> productsData = FXCollections.observableArrayList();
        String query = "SELECT produs.id, " +
                "produs.nume, " +
                "produs.descriere, " +
                "produs.activ, " +
                "produs.responsabil, " +
                "produs.data_creare, " +
                "produs.creat_de, " +
                "u.username AS responsabilNume, " +
                "u1.username AS creatDeNume " +
                "FROM Produs " +
                "LEFT JOIN Utilizatori u ON produs.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON produs.creat_de = u1.id";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nume = rs.getString("nume");
                String descriere = rs.getString("descriere");
                Boolean activ = rs.getBoolean("activ");
                Integer responabil = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabilNume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creatDeNume");


                productsData.add(new Products(id, nume, descriere, activ, responabil, responsabil_nume, data_creare, creat_de, creat_de_nume));
            }

        } catch (SQLException ex) {
            Logger.getLogger(com.example.crmjavafx.DetailsSalePageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return productsData;
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


    public static class ProductListCell extends ListCell<Products> {
        @Override
        protected void updateItem(Products product, boolean empty) {
            super.updateItem(product, empty);
            if (product != null) {
                setText(product.getNume());
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
        saleCompanyComboBox.setItems(companyList);

        ObservableList<Contacts> contactList = getContactsFromDB();
        saleContactComboBox.setItems(contactList);

        ObservableList<Users> assignedUserList = getUsersFromDB();
        saleAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        saleCreatedByComboBox.setItems(createdByUserList);

        ObservableList<Products> productList = getProductsFromDB();
        saleProductComboBox.setItems(productList);

        saleCompanyComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsSalePageController.CompanyListCell());
        saleCompanyComboBox.setButtonCell(new com.example.crmjavafx.DetailsSalePageController.CompanyListCell());

        saleContactComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsSalePageController.ContactListCell());
        saleContactComboBox.setButtonCell(new com.example.crmjavafx.DetailsSalePageController.ContactListCell());

        saleAssigneeComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsSalePageController.UserListCell());
        saleAssigneeComboBox.setButtonCell(new com.example.crmjavafx.DetailsSalePageController.UserListCell());

        saleCreatedByComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsSalePageController.UserListCell());
        saleCreatedByComboBox.setButtonCell(new com.example.crmjavafx.DetailsSalePageController.UserListCell());

        saleProductComboBox.setCellFactory(lv -> new DetailsSalePageController.ProductListCell());
        saleProductComboBox.setButtonCell(new com.example.crmjavafx.DetailsSalePageController.ProductListCell());
    }
}
