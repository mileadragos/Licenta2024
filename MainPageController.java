package com.example.crmjavafx;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainPageController implements Initializable {

    //Declararea variabilelor ajutatoare pentru conexiunea la baza de date
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";



    //Declararea campurile folosite in FXML
    @FXML
    private Label helloUser;

    @FXML
    private Label welcomeUser;

    @FXML
    private TableView<Companies> companyDetailsTable;

    @FXML
    private TableView<Contacts> contactDetailsTable;

    @FXML
    private TableView<Leads> leadDetailsTable;

    @FXML
    private TableView<Opportunities> opportunitiesDetailsTable;

    @FXML
    private TableView<Sales> salesDetailsTable;

    @FXML
    private TableView<Products> productsDetailsTable;

    @FXML
    private TableView<Tasks> tasksDetailsTable;

    @FXML
    private TableView<Tasks> myTasksTableView;

    @FXML
    private TableView<Opportunities> myOpportunitiesTableView;

    @FXML
    private MenuBar menuBarUser;

    @FXML
    private Menu menuUser;

    @FXML
    private MenuItem menuItemLogOff;

    @FXML
    private MenuItem menuItemExit;



    @FXML
    private ImageView taskImage;

    @FXML
    private ImageView contactImage;

    @FXML
    private ImageView companyImage;

    @FXML
    private ImageView leadImage;

    @FXML
    private ImageView opportunityImage;

    @FXML
    private ImageView saleImage;

    @FXML
    private ImageView productImage;

    @FXML
    private ImageView homeImage;

    @FXML
    private ImageView reportImage;

    @FXML
    private ImageView refreshCompanies;

    @FXML
    private ImageView refreshContacts;

    @FXML
    private ImageView refreshLeads;

    @FXML
    private ImageView refreshOpportunities;

    @FXML
    private ImageView refreshSales;

    @FXML
    private ImageView refreshTasks;

    @FXML
    private ImageView refreshProducts;

    @FXML
    private AnchorPane dashboardAnchorPane;

    private ImageView currentlyHighlighted = null;

    private String userName;

    private Username currentUser;

    private Integer currentUserId;




    //Functii de control si evenimente

    @FXML
    void setLoggedInMessage (Username user) {

        menuUser.setText("Utilizator: " + user.getUsername());
    }

    @FXML
    void setNavigationMessage(Username user) {
        welcomeUser.setText("Bun venit, " + user.getUsername() + "!");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        welcomeUser.setFont(helloUserFont);
    }


    @FXML
    void getUser(Username user) {
        userName = user.getUsername();
    }

    public void getUserId(Username currentUser) {
        this.currentUser = currentUser;
        currentUserId = retrievedUserId(currentUser.getUsername());
    }

    private Integer retrievedUserId(String userName) {

        String getUserQuery = "SELECT id FROM utilizatori WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            PreparedStatement pstmt = conn.prepareStatement(getUserQuery);
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return  rs.getInt("id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
    @FXML
    private void highlightImage(ImageView imageView) {

        if(currentlyHighlighted != null) {
            currentlyHighlighted.setEffect(null);
        }
        DropShadow effect = new DropShadow();
        effect.setRadius(2.0);
        effect.setColor(Color.BLUEVIOLET);

        imageView.setEffect(effect);
        currentlyHighlighted = imageView;
    }

    @FXML
    void handleShowHome() {
        myTasksTableView.setItems(getMyTasksFromDB());
        highlightImage(homeImage);

    }

    private ObservableList<Tasks> getMyTasksFromDB() {
        ObservableList<Tasks> tasksData = FXCollections.observableArrayList();

        String query = "SELECT t.id, " +
                "t.descriere, " +
                "t.deadline, " +
                "t.lead_id, " +
                "l.nume_lead AS lead_nume, " +
                "t.oportunitate_id, " +
                "o.nume_oportunitate AS oportunitate_nume, " +
                "t.status, " +
                "t.activ, " +
                "t.responsabil, " +
                "t.data_creare, " +
                "t.creat_de, " +
                "u.username AS responsabil_nume, " +
                "u1.username AS creat_de_nume " +
                "FROM Task t " +
                "LEFT JOIN Oportunitate o ON t.oportunitate_id = o.id " +
                "LEFT JOIN Lead l ON t.lead_id = l.id " +
                "LEFT JOIN Utilizatori u ON t.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON t.creat_de = u1.id WHERE t.activ = true AND u.username = ?";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
             PreparedStatement pstmt = conn.prepareStatement(query);
             pstmt.setString(1, userName);
             ResultSet rs = pstmt.executeQuery();




            while (rs.next()) {
                Integer id = rs.getInt("id");
                String descriere = rs.getString("descriere");
                Date deadline = rs.getDate("deadline");
                Integer lead_id = rs.getInt("lead_id");
                String lead_nume = rs.getString("lead_nume");
                Integer oportunitate_id = rs.getInt("oportunitate_id");
                String oportunitate_nume = rs.getString("oportunitate_nume");
                String status = rs.getString("status");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil_id = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabil_nume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de_id = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creat_de_nume");


                tasksData.add(new Tasks(
                        id,
                        descriere,
                        deadline,
                        lead_id,
                        lead_nume,
                        oportunitate_id,
                        oportunitate_nume,
                        status,
                        activ,
                        responsabil_id,
                        responsabil_nume,
                        data_creare,
                        creat_de_id,
                        creat_de_nume
                ));


            }
            showHome();
            return tasksData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleShowCompanies() {
        companyDetailsTable.setItems(getCompaniesFromDB());
        highlightImage(companyImage);

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
                "LEFT JOIN Utilizatori u1 ON c.creat_de = u1.id " +
                "WHERE c.id != 9999 AND c.activ = true";

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
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showCompanyTableView();
        return companiesData;
    }

    @FXML
    private void handleShowContacts() {
        contactDetailsTable.setItems(getContactsFromDB());
        highlightImage(contactImage);
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
                "LEFT JOIN utilizatori u1 ON contact.creat_de = u1.id " +
                "WHERE contact.id != 9999 AND contact.activ = true";

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
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showContactTableView();
        return contactsData;
    }

    @FXML
    private void handleShowLeads() {
        leadDetailsTable.setItems(getLeadsFromDB());
        highlightImage(leadImage);
    }

    private ObservableList<Leads> getLeadsFromDB() {
        ObservableList<Leads> leadsData = FXCollections.observableArrayList();

        String query = "SELECT l.nume_lead," +
                " c.nume_companie," +
                " CONCAT(co.prenume_contact, ' ', co.nume_contact) AS nume_complet, " +
                "l.functie, " +
                " l.telefon, " +
                "l.email, " +
                "l.venit_estimat, " +
                "l.status, " +
                "l.id, " +
                "l.companie_id, " +
                "l.contact_id, " +
                "l.activ, " +
                "u.username AS username_responsabil, " +
                "u1.username AS username_creat, " +
                "l.data_creare, " +
                "l.responsabil, " +
                "l.creat_de " +
                "FROM Lead l " +
                "LEFT JOIN Companie c ON l.companie_id = c.id " +
                "LEFT JOIN Contact co ON l.contact_id = co.id " +
                "LEFT JOIN Utilizatori u ON l.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON l.creat_de = u1.id WHERE l.activ = true AND l.id != 9999";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String numeLead = rs.getString("nume_lead");
                String numeCompanie = rs.getString("nume_companie");
                String numeComplet = rs.getString("nume_complet");
                String functie = rs.getString("functie");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                BigDecimal venitEstimat = rs.getBigDecimal("venit_estimat");

                if (rs.wasNull()) { // Check if the retrieved value was null
                    venitEstimat = BigDecimal.ZERO; // Or another default value that makes sense
                }
                String status = rs.getString("status");
                Integer id = rs.getInt("id");
                Integer companie_id = rs.getInt("companie_id");
                Integer contact_id = rs.getInt("contact_id");

                Boolean activ = rs.getBoolean("activ");
                Integer creat_deId = rs.getInt("creat_de");
                Integer responsabilId = rs.getInt("responsabil");
                String responsabil = rs.getString("username_responsabil");
                Date data_creare = rs.getDate("data_creare");
                String creat_de = rs.getString("username_creat");


                //Leads constructor
                leadsData.add(new Leads(
                        numeLead,
                        numeCompanie,
                        numeComplet,
                        functie,
                        telefon,
                        email,
                        venitEstimat,
                        status,
                        id,
                        companie_id,
                        contact_id,
                        activ,
                        responsabilId,
                        data_creare,
                        creat_deId,
                        responsabil,
                        creat_de));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }


        showLeadTableView();
        return leadsData;
    }

    @FXML
    private void handleShowOpportunities() {
        opportunitiesDetailsTable.setItems(getOpportunitiesFromDB());
        highlightImage(opportunityImage);
    }

    private ObservableList<Opportunities> getOpportunitiesFromDB() {
        ObservableList<Opportunities> opportunitiesData = FXCollections.observableArrayList();

        String query = "SELECT o.id, " +
                "o.nume_oportunitate, " +
                "o.companie_id, " +
                "c.nume_companie AS nume_companie, " +
                "o.contact_id, " +
                "CONCAT(co.prenume_contact, ' ', co.nume_contact) AS nume_complet, " +
                "o.venit_estimat, " +
                "o.venit_real, " +
                "o.status, " +
                "o.activ, " +
                "o.responsabil, " +
                "o.data_creare, " +
                "o.creat_de, " +
                "u.username AS responsabil_nume, " +
                "u1.username AS creat_de_nume " +
                "FROM Oportunitate o " +
                "LEFT JOIN Companie c ON o.companie_id = c.id " +
                "LEFT JOIN Contact co ON o.contact_id = co.id " +
                "LEFT JOIN Utilizatori u ON o.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON o.creat_de = u1.id WHERE o.id != 9999 AND o.activ = true";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nume_oportunitate = rs.getString("nume_oportunitate");
                Integer companie_id = rs.getInt("companie_id");
                String companie_nume = rs.getString("nume_companie");
                Integer contact_id = rs.getInt("contact_id");
                String contact_nume = rs.getString("nume_complet");
                BigDecimal venit_estimat = BigDecimal.valueOf(rs.getInt("venit_estimat"));
                BigDecimal venit_real = BigDecimal.valueOf(rs.getInt("venit_real"));
                String status = rs.getString("status");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil_id = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabil_nume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de_id = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creat_de_nume");

                opportunitiesData.add(new Opportunities(id,
                        nume_oportunitate,
                        companie_id,
                        companie_nume,
                        contact_id,
                        contact_nume,
                        venit_estimat,
                        venit_real,
                        status,
                        activ,
                        responsabil_id,
                        responsabil_nume,
                        data_creare,
                        creat_de_id,
                        creat_de_nume));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showOpportunityTableView();
        return opportunitiesData;
    }


    @FXML
    private void handleShowSales() {
        salesDetailsTable.setItems(getSalesFromDB());
        highlightImage(saleImage);
    }

    private ObservableList<Sales> getSalesFromDB() {
        ObservableList<Sales> salesData = FXCollections.observableArrayList();

        String query = "SELECT v.id, " +
                "v.companie_id, " +
                "c.nume_companie AS nume_companie, " +
                "v.contact_id, " +
                "CONCAT(co.prenume_contact, ' ', co.nume_contact) AS nume_complet, " +
                "v.produs_id, " +
                "p.nume AS nume_produs, " +
                "v.cantitate, " +
                "v.total, " +
                "v.activ, " +
                "v.responsabil, " +
                "v.data_creare, " +
                "v.creat_de, " +
                "v.pret, " +
                "u.username AS responsabil_nume, " +
                "u1.username AS creat_de_nume " +
                "FROM Vanzare v " +
                "LEFT JOIN Companie c ON v.companie_id = c.id " +
                "LEFT JOIN Contact co ON v.contact_id = co.id " +
                "LEFT JOIN Utilizatori u ON v.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON v.creat_de = u1.id " +
                "LEFT JOIN Produs p ON v.produs_id = p.id WHERE v.activ = true";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer companie_id = rs.getInt("companie_id");
                String companie_nume = rs.getString("nume_companie");
                Integer contact_id = rs.getInt("contact_id");
                String contact_nume = rs.getString("nume_complet");
                Integer produs_id = rs.getInt("produs_id");
                String nume_produs = rs.getString("nume_produs");
                Integer cantitate = rs.getInt("cantitate");
                Float total = rs.getFloat("total");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil_id = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabil_nume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de_id = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creat_de_nume");
                Double pret = rs.getDouble("pret");

                salesData.add(new Sales(
                        id,
                        companie_id,
                        companie_nume,
                        contact_id,
                        contact_nume,
                        produs_id,
                        nume_produs,
                        cantitate,
                        total,
                        activ,
                        responsabil_id,
                        responsabil_nume,
                        data_creare,
                        creat_de_id,
                        creat_de_nume,
                        pret));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showSalesTableView();
        return salesData;
    }

    @FXML
    private void handleShowProducts() {
        productsDetailsTable.setItems(getProductsFromDB());
        highlightImage(productImage);
    }

    private ObservableList<Products> getProductsFromDB() {
        ObservableList<Products> productsData = FXCollections.observableArrayList();

        String query = "SELECT p.id, " +
                "p.nume, " +
                "p.descriere, " +
                "p.activ, " +
                "p.responsabil, " +
                "p.data_creare, " +
                "p.creat_de, " +
                "u.username AS responsabil_nume, " +
                "u1.username AS creat_de_nume " +
                "FROM Produs p " +
                "LEFT JOIN Utilizatori u ON p.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON p.creat_de = u1.id WHERE p.activ = true";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nume = rs.getString("nume");
                String descriere = rs.getString("descriere");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil_id = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabil_nume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de_id = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creat_de_nume");

                productsData.add(new Products(
                        id,
                        nume,
                        descriere,
                        activ,
                        responsabil_id,
                        responsabil_nume,
                        data_creare,
                        creat_de_id,
                        creat_de_nume
                        ));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showProductsTableView();
        return productsData;
    }

    @FXML
    private void handleShowTasks() {
        tasksDetailsTable.setItems(getTasksFromDB());
        highlightImage(taskImage);
    }

    private ObservableList<Tasks> getTasksFromDB() {
        ObservableList<Tasks> tasksData = FXCollections.observableArrayList();

        String query = "SELECT t.id, " +
                "t.descriere, " +
                "t.deadline, " +
                "t.lead_id, " +
                "l.nume_lead AS lead_nume, " +
                "t.oportunitate_id, " +
                "o.nume_oportunitate AS oportunitate_nume, " +
                "t.status, " +
                "t.activ, " +
                "t.responsabil, " +
                "t.data_creare, " +
                "t.creat_de, " +
                "u.username AS responsabil_nume, " +
                "u1.username AS creat_de_nume " +
                "FROM Task t " +
                "LEFT JOIN Oportunitate o ON t.oportunitate_id = o.id " +
                "LEFT JOIN Lead l ON t.lead_id = l.id " +
                "LEFT JOIN Utilizatori u ON t.responsabil = u.id " +
                "LEFT JOIN Utilizatori u1 ON t.creat_de = u1.id WHERE t.activ = true";


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String descriere = rs.getString("descriere");
                Date deadline = rs.getDate("deadline");
                Integer lead_id = rs.getInt("lead_id");
                String lead_nume = rs.getString("lead_nume");
                Integer oportunitate_id = rs.getInt("oportunitate_id");
                String oportunitate_nume = rs.getString("oportunitate_nume");
                String status = rs.getString("status");
                Boolean activ = rs.getBoolean("activ");
                Integer responsabil_id = rs.getInt("responsabil");
                String responsabil_nume = rs.getString("responsabil_nume");
                Date data_creare = rs.getDate("data_creare");
                Integer creat_de_id = rs.getInt("creat_de");
                String creat_de_nume = rs.getString("creat_de_nume");

                tasksData.add(new Tasks(
                        id,
                        descriere,
                        deadline,
                        lead_id,
                        lead_nume,
                        oportunitate_id,
                        oportunitate_nume,
                        status,
                        activ,
                        responsabil_id,
                        responsabil_nume,
                        data_creare,
                        creat_de_id,
                        creat_de_nume
                ));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(MainPageController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        showTasksTableView();
        return tasksData;
    }

    @FXML
    private void showHome() {
        dashboardAnchorPane.setVisible(true);

        companyDetailsTable.setVisible(false);
        refreshCompanies.setVisible(false);

        refreshContacts.setVisible(false);
        refreshLeads.setVisible(false);

        contactDetailsTable.setVisible(false);
        leadDetailsTable.setVisible(false);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);

        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);


        helloUser.setText("Aceasta este pagina principală. Navighează în stânga pentru a accesa detaliile dorite!");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);

    }

    @FXML
    private void showCompanyTableView() {
        companyDetailsTable.setVisible(true);
        refreshCompanies.setVisible(true);

        refreshContacts.setVisible(false);
        refreshLeads.setVisible(false);

        contactDetailsTable.setVisible(false);
        leadDetailsTable.setVisible(false);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);

        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Companiilor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }

    @FXML
    private void showContactTableView() {
        contactDetailsTable.setVisible(true);
        refreshContacts.setVisible(true);

        refreshCompanies.setVisible(false);
        refreshLeads.setVisible(false);

        companyDetailsTable.setVisible(false);
        leadDetailsTable.setVisible(false);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);

        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Contactelor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }

    @FXML
    private void showLeadTableView() {
        leadDetailsTable.setVisible(true);
        refreshLeads.setVisible(true);

        refreshContacts.setVisible(false);
        refreshCompanies.setVisible(false);

        companyDetailsTable.setVisible(false);
        contactDetailsTable.setVisible(false);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);

        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Lead-urilor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }

    @FXML
    private void showOpportunityTableView() {
        opportunitiesDetailsTable.setVisible(true);
        refreshOpportunities.setVisible(true);

        leadDetailsTable.setVisible(false);
        refreshLeads.setVisible(false);

        refreshContacts.setVisible(false);
        refreshCompanies.setVisible(false);

        companyDetailsTable.setVisible(false);
        contactDetailsTable.setVisible(false);

        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Oportunităților. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }


    @FXML
    private void showSalesTableView() {
        salesDetailsTable.setVisible(true);
        refreshSales.setVisible(true);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);


        leadDetailsTable.setVisible(false);
        refreshLeads.setVisible(false);

        refreshContacts.setVisible(false);
        refreshCompanies.setVisible(false);

        companyDetailsTable.setVisible(false);
        contactDetailsTable.setVisible(false);

        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Vânzărilor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }

    @FXML
    private void showProductsTableView() {

        productsDetailsTable.setVisible(true);
        refreshProducts.setVisible(true);


        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);

        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);


        leadDetailsTable.setVisible(false);
        refreshLeads.setVisible(false);

        refreshContacts.setVisible(false);
        refreshCompanies.setVisible(false);

        companyDetailsTable.setVisible(false);
        contactDetailsTable.setVisible(false);

        tasksDetailsTable.setVisible(false);
        refreshTasks.setVisible(false);

        dashboardAnchorPane.setVisible(false);

        helloUser.setText("Aceasta este lista Serviciilor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);
    }

    @FXML
    private void showTasksTableView() {

        tasksDetailsTable.setVisible(true);
        refreshTasks.setVisible(true);


        productsDetailsTable.setVisible(false);
        refreshProducts.setVisible(false);


        salesDetailsTable.setVisible(false);
        refreshSales.setVisible(false);


        opportunitiesDetailsTable.setVisible(false);
        refreshOpportunities.setVisible(false);


        leadDetailsTable.setVisible(false);
        refreshLeads.setVisible(false);


        refreshContacts.setVisible(false);
        refreshCompanies.setVisible(false);


        companyDetailsTable.setVisible(false);
        contactDetailsTable.setVisible(false);

        dashboardAnchorPane.setVisible(false);


        helloUser.setText("Aceasta este lista Task-urilor. Navighează spre Mai Multe... pentru a accesa acțiuni suplimentare.");
        Font helloUserFont = Font.font("Arial", FontWeight.BOLD, 14);
        helloUser.setFont(helloUserFont);

    }

    @FXML
    private void handleDoubleClickCompany(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Companies selectedCompany = companyDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedCompany != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-company-page.fxml")));

                    Parent root = loader.load();
                    DetailsCompanyPageController detailsCompanyPageController = loader.getController();
                    detailsCompanyPageController.setCompanyData(selectedCompany);
                    detailsCompanyPageController.setCurrentUserId(currentUserId);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Companie " + selectedCompany.getNume_companie());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void handleDoubleClickContact(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Contacts selectedContact = contactDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedContact != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-contact-page.fxml")));

                    Parent root = loader.load();
                    DetailsContactPageController detailsContactPageController = loader.getController();
                    detailsContactPageController.setContactData(selectedContact);

                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("Detalii Contact " + selectedContact.getPrenume_contact() + " " + selectedContact.getNume_contact());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.initStyle(StageStyle.DECORATED);

                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void handleDoubleClickLead(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Leads selectedLead = leadDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedLead != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-lead-page.fxml")));

                    Parent root = loader.load();
                    DetailsLeadPageController detailsLeadPageController = loader.getController();
                    detailsLeadPageController.setLeadData(selectedLead);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Lead " + selectedLead.getNume_lead());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void handleDoubleClickOpportunity(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Opportunities selectedOpportunity = opportunitiesDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedOpportunity != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-opportunity-page.fxml")));

                    Parent root = loader.load();
                    DetailsOpportunityPageController detailsOpportunityPageController = loader.getController();
                    detailsOpportunityPageController.setOpportunityData(selectedOpportunity);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Oportunitate " + selectedOpportunity.getNume_oportunitate());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void handleDoubleClickSale(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Sales selectedSale = salesDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedSale != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-sale-page.fxml")));

                    Parent root = loader.load();
                    DetailsSalePageController detailsSalePageController = loader.getController();
                    detailsSalePageController.setSaleData(selectedSale);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Vanzare pentru " + selectedSale.getCompanie_nume());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void handleDoubleClickProduct(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Products selectedProduct = productsDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-product-page.fxml")));

                    Parent root = loader.load();
                    DetailsProductPageController detailsProductPageController = loader.getController();
                    detailsProductPageController.setProductData(selectedProduct);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Produs/Serviciu " + selectedProduct.getNume());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void handleDoubleClickTask(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Tasks selectedTask = tasksDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("details-task-page.fxml")));

                    Parent root = loader.load();
                    DetailsTaskPageController detailsTaskPageController = loader.getController();
                    detailsTaskPageController.setTaskData(selectedTask);


                    Stage primaryStage = new Stage();
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Detalii Task cu termen limita la " + selectedTask.getDeadline());
                    primaryStage.setResizable(false);
                    primaryStage.getIcons().add(new Image("CRM-Icon.png"));
                    primaryStage.setScene( new Scene( root, 667, 496));
                    primaryStage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    private void logOff() {

        try {
            FXMLLoader loader =  new FXMLLoader(LoginController.class.getResource("login-page.fxml"));

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Deconectare din Aplicația Now CRM");
            alert.setHeaderText("Mesaj de confirmare");
            alert.setContentText("Ești sigur că vrei să te deconectezi? Vei fi redirecționat către pagina de Login!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent newRoot = loader.load();
                Stage primaryStage = new Stage();
                primaryStage.initStyle(StageStyle.DECORATED);

                primaryStage.setScene(new Scene(newRoot,  667, 496));
                primaryStage.setResizable(false);
                primaryStage.getIcons().add(new Image("CRM-Icon.png"));

                primaryStage.setTitle("Pagina Login");
                primaryStage.show();

                Stage stage = (Stage) menuBarUser.getScene().getWindow();
                stage.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @FXML
    private void exit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Închiderea Aplicației Now CRM");
        alert.setHeaderText("Mesaj de confirmare");
        alert.setContentText("Ești sigur că vrei să închizi Aplicația Now CRM?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) menuBarUser.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private void showReports () {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test.fxml")));
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setScene( new Scene( root, 1000, 800));
            primaryStage.setTitle("Rapoarte");
            primaryStage.getIcons().add(new Image("CRM-Icon.png"));
            primaryStage.setMaximized(true);
            primaryStage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        showHome();


        companyDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        companyDetailsTable.setOnMouseClicked(this::handleDoubleClickCompany);

        contactDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contactDetailsTable.setOnMouseClicked(this::handleDoubleClickContact);

        leadDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leadDetailsTable.setOnMouseClicked(this::handleDoubleClickLead);

        opportunitiesDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        opportunitiesDetailsTable.setOnMouseClicked(this::handleDoubleClickOpportunity);

        salesDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        salesDetailsTable.setOnMouseClicked(this::handleDoubleClickSale);

        productsDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productsDetailsTable.setOnMouseClicked(this::handleDoubleClickProduct);

        tasksDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tasksDetailsTable.setOnMouseClicked(this::handleDoubleClickTask);

    }
}
