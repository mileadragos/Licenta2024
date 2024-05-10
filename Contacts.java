package com.example.crmjavafx;

import java.sql.Date;

public class Contacts {

    private String nume_contact;
    private String prenume_contact;
    private String functie;
    private String email;
    private String telefon;
    private String companieNume;
    private Integer id;
    private Integer companie_id;
    private Boolean activ;
    private Integer responsabil;
    private Date data_creare;
    private Integer creat_de;
    private String responsabilNume;
    private String creatDeNume;


    // Constructor
    public Contacts(String nume_contact,
                    String prenume_contact,
                    String functie,
                    String email,
                    String telefon,
                    String companieNume,
                    Integer id,
                    Integer companie_id,
                    Boolean activ,
                    Integer responsabil,
                    Date data_creare,
                    Integer creat_de,
                    String responsabilNume,
                    String creatDeNume) {
        this.nume_contact = nume_contact;
        this.prenume_contact = prenume_contact;
        this.functie = functie;
        this.email = email;
        this.telefon = telefon;
        this.companieNume = companieNume;
        this.id = id;
        this.companie_id = companie_id;
        this.activ = activ;
        this.responsabil = responsabil;
        this.data_creare = data_creare;
        this.creat_de = creat_de;
        this.responsabilNume = responsabilNume;
        this.creatDeNume = creatDeNume;
    }


    //Functii de tip Get
    public String getNume_contact() {
        return nume_contact;
    }
    public String getPrenume_contact() {
        return prenume_contact;
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
    public String getCompanieNume() {
        return companieNume;
    }
    public Integer getId() {
        return id;
    }
    public Integer getCompanie_id() {
        return companie_id;
    }
    public Boolean getActiv() {
        return activ;
    }
    public Integer getResponsabil() {
        return responsabil;
    }
    public Date getData_creare() {
        return data_creare;
    }
    public Integer getCreat_de() {
        return creat_de;
    }
    public String getResponsabilNume() {
        return responsabilNume;
    }
    public String getCreatDeNume() {
        return creatDeNume;
    }

    //Functii de tip Set
    public void setCompanieNume(String companieNume) {
        this.companieNume = companieNume;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFunctie(String functie) {
        this.functie = functie;
    }
    public void setNume_contact(String nume_contact) {
        this.nume_contact = nume_contact;
    }
    public void setPrenume_contact(String prenume_contact) {
        this.prenume_contact = prenume_contact;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setCompanie_id(Integer companie_id) {
        this.companie_id = companie_id;
    }
    public void setActiv(Boolean activ) {
        this.activ = activ;
    }
    public void setResponsabil(Integer responsabil) {
        this.responsabil = responsabil;
    }
    public void setData_creare(Date data_creare) {
        this.data_creare = data_creare;
    }
    public void setCreat_de(Integer creat_de) {
        this.creat_de = creat_de;
    }
    public void setResponsabilNume(String responsabilNume) {
        this.responsabilNume = responsabilNume;
    }
    public void setCreatDeNume(String creatDeNume) {
        this.creatDeNume = creatDeNume;
    }

    public Contacts (
            String nume_contact,
            String prenume_contact,
            String functie,
            String email,
            String telefon,
            Integer companie_id,
            Boolean activ,
            Integer responsabil,

            Date data_creare,
            Integer creat_de

    ) {
        this.nume_contact = nume_contact;
        this.prenume_contact = prenume_contact;
        this.functie = functie;
        this.email = email;
        this.telefon = telefon;
        this.companie_id = companie_id;
        this.activ = activ;
        this.responsabil = responsabil;

        this.data_creare = data_creare;
        this.creat_de = creat_de;
    }


    public Contacts (
            Boolean activ,
            Integer id
    ) {
        this.activ = activ;
        this.id = id;
    }
}
