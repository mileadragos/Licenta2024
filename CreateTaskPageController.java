package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTaskPageController implements Initializable {


    @FXML
    private ComboBox<Opportunities> taskOpportunityComboBox;

    @FXML
    private ComboBox<Leads> taskLeadComboBox;

    @FXML
    private ComboBox<String> taskStatusComboBox;

    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private Label statusUpdate;

    @FXML
    private CheckBox taskActiveCheckBox;

    @FXML
    private ComboBox<Users> taskAssigneeComboBox;

    @FXML
    private DatePicker taskCreatedDate;

    @FXML
    private DatePicker taskDueDate;

    @FXML
    private ComboBox<Users> taskCreatedByComboBox;

    @FXML
    private Button backButton;

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";


    private Tasks task;

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
                "LEFT JOIN Utilizatori u1 ON o.creat_de = u1.id";

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
        return opportunitiesData;
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
                "LEFT JOIN Utilizatori u1 ON l.creat_de = u1.id WHERE l.activ = true"; // Use LEFT JOIN in case there's no associated company or contact


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
        return leadsData;
    }

    public void createTask() {
        Opportunities updatedOpportunity = taskOpportunityComboBox.getValue();
        Leads updatedLead = taskLeadComboBox.getValue();
        String updatedStatus = "in progres";
        String updatedDescription = taskDescriptionField.getText();

        Boolean updatedActive = true;
        Users updatedAssignee = taskAssigneeComboBox.getValue();
        LocalDate localDateToday = LocalDate.now();
        Date today = Date.valueOf(localDateToday);
        Date updatedDueDate = Date.valueOf(taskDueDate.getValue());
        Users updatedCreatedBy = taskCreatedByComboBox.getValue();


        Tasks createdTask = new Tasks (
                updatedOpportunity.getId(),
                updatedLead.getId(),
                updatedStatus,
                updatedDescription,
                updatedActive,
                today,
                updatedDueDate,
                updatedAssignee.getId(),
                updatedCreatedBy.getId()

        );
        createTaskInDatabase(createdTask);
    }

    private void createTaskInDatabase(Tasks createdTask) {
        String insertQuery = "INSERT INTO task(descriere, deadline, lead_id, oportunitate_id, status, activ, responsabil, data_creare, creat_de) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1,createdTask.getDescriere());
            pstmt.setDate(2, createdTask.getDeadline());
            pstmt.setInt(3, createdTask.getLead_id());
            pstmt.setInt(4, createdTask.getOportunitate_id());
            pstmt.setString(5, createdTask.getStatus());

            pstmt.setBoolean(6, createdTask.getActiv());
            pstmt.setInt(7, createdTask.getResponsabil_id());
            pstmt.setDate(8, createdTask.getData_creare());
            pstmt.setInt(9, createdTask.getCreat_de_id());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Task-ul a fost creat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(CreateTaskPageController.class.getName());
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
            Logger.getLogger(com.example.crmjavafx.DetailsTaskPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return usersData;
    }


    public static class OpportunitiesListCell extends ListCell<Opportunities> {
        @Override
        protected void updateItem(Opportunities opportunity, boolean empty) {
            super.updateItem(opportunity, empty);
            if (opportunity != null) {
                setText(opportunity.getNume_oportunitate());
            } else {
                setText(null);
            }
        }
    }

    public static class LeadListCell extends ListCell<Leads> {
        @Override
        protected void updateItem(Leads lead, boolean empty) {
            super.updateItem(lead, empty);
            if (lead != null) {
                setText(lead.getNume_lead());
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

    private ObservableList<String> getTaskStatuses() {
        return FXCollections.observableArrayList(
                "deschis", "in progres", "inchis"
        );
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

        ObservableList<Opportunities> opportunityList = getOpportunitiesFromDB();
        taskOpportunityComboBox.setItems(opportunityList);

        ObservableList<Leads> leadList = getLeadsFromDB();
        taskLeadComboBox.setItems(leadList);

        ObservableList<Users> assignedUserList = getUsersFromDB();
        taskAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        taskCreatedByComboBox.setItems(createdByUserList);



        taskOpportunityComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsTaskPageController.OpportunitiesListCell());
        taskOpportunityComboBox.setButtonCell(new com.example.crmjavafx.DetailsTaskPageController.OpportunitiesListCell());

        taskLeadComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsTaskPageController.LeadListCell());
        taskLeadComboBox.setButtonCell(new com.example.crmjavafx.DetailsTaskPageController.LeadListCell());

        taskAssigneeComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsTaskPageController.UserListCell());
        taskAssigneeComboBox.setButtonCell(new com.example.crmjavafx.DetailsTaskPageController.UserListCell());

        taskCreatedByComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsTaskPageController.UserListCell());
        taskCreatedByComboBox.setButtonCell(new com.example.crmjavafx.DetailsTaskPageController.UserListCell());

    }
}
