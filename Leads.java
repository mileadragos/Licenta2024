package com.example.crmjavafx;

import java.math.BigDecimal;
import java.sql.Date;

public class Leads {

    private String nume_lead;
    private Integer companie_id; // Foreign key reference
    private Integer contact_id; // Foreign key reference
    private String functie;
    private String telefon;
    private String email;
    private BigDecimal venit_estimat; // Using BigDecimal for accurate decimal handling
    private String status;

    private Integer id; // Primary Key

    private String numeCompanie; // To store the company name
    private String numeComplet;  // To store the contact's full name

    private Boolean activ;
    private Integer responsabilId;
    private Date data_creare;
    private Integer creat_deId;

    private String responsabil;

    private String creat_de;

    // Constructor
    public Leads(String nume_lead,
                 String numeCompanie,
                 String numeComplet,
                 String functie,
                 String telefon,
                 String email,
                 BigDecimal venit_estimat,
                 String status,
                 Integer id,
                 Integer companie_id,
                 Integer contact_id,
                 Boolean activ,
                 Integer responsabilId,
                 Date data_creare,
                 Integer creat_deId,
                 String responsabil,
                 String creat_de) {

        this.nume_lead = nume_lead;
        this.companie_id = companie_id;
        this.contact_id = contact_id;
        this.functie = functie;
        this.telefon = telefon;
        this.email = email;
        this.venit_estimat = venit_estimat;
        this.status = status;
        this.id = id;
        this.numeCompanie = numeCompanie;
        this.numeComplet = numeComplet;
        this.activ = activ;
        this.responsabil = responsabil;
        this.data_creare = data_creare;
        this.creat_de = creat_de;
        this.responsabilId = responsabilId;
        this.creat_deId = creat_deId;
    }

    //Functii de tip Get
    public String getNume_lead() {
        return nume_lead;
    }

    public Integer getCompanie_id() {
        return companie_id;
    }

    public Integer getContact_id() {
        return contact_id;
    }

    public String getFunctie() {
        return functie;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getVenit_estimat() {
        return venit_estimat;
    }

    public String getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public String getNumeCompanie() {
        return numeCompanie;
    }

    public String getNumeComplet() {
        return numeComplet;
    }


    public Boolean getActiv() {
        return activ;
    }

    public Integer getResponsabilId() {
        return responsabilId;
    }

    public Date getData_creare() {
        return data_creare;
    }

    public Integer getCreat_deId() {
        return creat_deId;
    }

    public String getCreat_de() {
        return creat_de;
    }

    public String getResponsabil() {
        return responsabil;
    }

    //Functii de tip Set
    public void setNume_lead(String nume_lead) {
        this.nume_lead = nume_lead;
    }

    public void setCompanie_id(Integer companie_id) {
        this.companie_id = companie_id;
    }

    public void setContact_id(Integer contact_id) {
        this.contact_id = contact_id;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVenit_estimat(BigDecimal venit_estimat) {
        this.venit_estimat = venit_estimat;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }

    public void setResponsabilId(Integer responsabil) {
        this.responsabilId = responsabilId;
    }

    public void setData_creare(Date data_creare) {
        this.data_creare = data_creare;
    }

    public void setCreat_deId(Integer creat_de) {
        this.creat_deId = creat_deId;
    }

    public void setResponsabil(String responsabil) {
        this.responsabil = responsabil;
    }

    public void setCreat_de(String creat_de) {
        this.creat_de = creat_de;
    }

    public Leads (

    ){

    }
}
