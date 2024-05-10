package com.example.crmjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetailsProductPageController implements Initializable {


    @FXML
    private TextField productNameField;

    @FXML
    private TextArea productDescriptionField;


    @FXML
    private Label statusUpdate;

    @FXML
    private CheckBox productActiveCheckBox;

    @FXML
    private ComboBox<Users> productAssigneeComboBox;

    @FXML
    private DatePicker productCreatedDate;

    @FXML
    private ComboBox<Users> productCreatedByComboBox;


    private static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "dragos";


    private Products product;




    public void updateProduct() {


        String updatedName = productNameField.getText();
        String updatedDescription = productDescriptionField.getText();
        Boolean updatedActive = productActiveCheckBox.isSelected();
        Users updatedAssignee = productAssigneeComboBox.getValue();
        Date updatedDate = Date.valueOf(productCreatedDate.getValue());
        Users updatedCreatedBy = productCreatedByComboBox.getValue();


        Products updatedProduct = new Products(
                product.getId(),
                updatedName,
                updatedDescription,
                updatedActive,
                updatedAssignee.getId(),
                updatedAssignee.getUsername(),
                updatedDate,
                updatedCreatedBy.getId(),
                updatedCreatedBy.getUsername()
        );

        updateProductInDatabase(updatedProduct);

    }



    private void updateProductInDatabase(Products updatedProduct) {
        String updateSql = "UPDATE produs SET " +
                "nume = ?, " +
                "descriere = ?, " +
                "activ = ?, " +
                "responsabil = ?, " +
                "data_creare = ?, " +
                "creat_de = ? " +
                "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, updatedProduct.getNume());
            stmt.setString(2, updatedProduct.getDescriere());
            stmt.setBoolean(3, updatedProduct.getActiv());
            stmt.setInt(4, updatedProduct.getResponsabil_id());
            stmt.setDate(5, updatedProduct.getData_creare());
            stmt.setInt(6, updatedProduct.getCreat_de_id());
            stmt.setInt(7, updatedProduct.getId());


            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                statusUpdate.setText("Produsul a fost actualizat cu succes!");

            } else {
                statusUpdate.setText("Au intampinat erori la nivelul bazei de date. Va rog sa incercati mai tarziu.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(com.example.crmjavafx.DetailsProductPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }




    public void setProductData(Products product) {
        this.product = product;
        populateDetails();
    }


    private void populateDetails() {

        productNameField.setText((product.getNume()));
        productDescriptionField.setText(product.getDescriere());
        productActiveCheckBox.setSelected(product.getActiv());

        productCreatedDate.setValue(product.getData_creare() != null ? product.getData_creare().toLocalDate() : null);

        ObservableList<Users> userList = getUsersFromDB();
        productAssigneeComboBox.setItems(userList);
        for (Users user : userList) {
            if (user.getId().equals(product.getResponsabil_id())) {
                productAssigneeComboBox.setValue(user);
                break;
            }
        }


        ObservableList<Users> createdByList = getUsersFromDB();
        productCreatedByComboBox.setItems(createdByList);
        for (Users user : createdByList) {
            if (user.getId().equals(product.getCreat_de_id())) {
                productCreatedByComboBox.setValue(user);
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
            Logger.getLogger(com.example.crmjavafx.DetailsProductPageController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ObservableList<Users> assignedUserList = getUsersFromDB();
        productAssigneeComboBox.setItems(assignedUserList);

        ObservableList<Users> createdByUserList = getUsersFromDB();
        productCreatedByComboBox.setItems(createdByUserList);


        productAssigneeComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsProductPageController.UserListCell());
        productAssigneeComboBox.setButtonCell(new com.example.crmjavafx.DetailsProductPageController.UserListCell());

        productCreatedByComboBox.setCellFactory(lv -> new com.example.crmjavafx.DetailsProductPageController.UserListCell());
        productCreatedByComboBox.setButtonCell(new com.example.crmjavafx.DetailsProductPageController.UserListCell());

    }
}
