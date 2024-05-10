package com.example.crmjavafx;

import java.sql.Date;
import java.time.LocalDate;


public class Companies {

    private String nume_companie;
    private String adresa_companie;
    private String telefon;
    private String email;
    private Integer id;
    private Boolean activ;
    private Integer responsabil;
    private Date data_creare;
    private Integer creat_de;
    private String responsabilNume;
    private String creatDeNume;


    // Constructor
    public Companies(
            String nume_companie,
            String adresa_companie,
            String telefon,
            String email,
            Integer id,
            Boolean activ,
            Integer responsabil,
            Date data_creare,
            Integer creat_de,
            String responsabilNume,
            String creatDeNume) {
        this.nume_companie = nume_companie;
        this.adresa_companie = adresa_companie;
        this.telefon = telefon;
        this.email = email;
        this.id = id;
        this.activ = activ;
        this.responsabil = responsabil;
        this.data_creare = data_creare;
        this.creat_de = creat_de;
        this.responsabilNume = responsabilNume;
        this.creatDeNume = creatDeNume;
    }


    //Functii de tip Get
    public String getAdresa_companie() {
        return adresa_companie;
    }

    public String getNume_companie() {
        return nume_companie;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public Integer getId() {
        return id;
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
    public void setNume_companie(String nume_companie) {
        this.nume_companie = nume_companie;
    }

    public void setAdresa_companie(String adresa_companie) {
        this.adresa_companie = adresa_companie;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Companies(String nume_companie, String adresa_companie, String telefon,
                     String email, Boolean activ, Integer responsabil, Date data_creare, Integer creat_de) {
        this.nume_companie = nume_companie;
        this.adresa_companie = adresa_companie;
        this.telefon = telefon;
        this.email = email;
        this.responsabil = responsabil;
        this.creat_de = creat_de;
        this.data_creare = Date.valueOf(LocalDate.now());
        this.activ = true;
    }

    public Companies(Boolean activ, Integer id) {
        this.activ = activ;
        this.id = id;
    }

}
