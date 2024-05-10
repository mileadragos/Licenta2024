package com.example.crmjavafx;

import java.sql.Date;

public class Sales {
    private Integer id;
    private Integer companie_id;
    private String companie_nume;
    private Integer contact_id;
    private String contact_nume;
    private Integer produs_id;
    private String nume_produs;
    private Integer cantitate;
    private Float total;
    private Boolean activ;
    private Integer responsabil_id;
    private String responsabil_nume;
    private Date data_creare;
    private Integer creat_de_id;
    private String creat_de_nume;
    private Double pret;

    public Sales(
            Integer id,
            Integer companie_id,
            String companie_nume,
            Integer contact_id,
            String contact_nume,
            Integer produs_id,
            String nume_produs,
            Integer cantitate,
            Float total,
            Boolean activ,
            Integer responsabil_id,
            String responsabil_nume,
            Date data_creare,
            Integer creat_de_id,
            String creat_de_nume,
            Double pret) {
        this.id = id;
        this.companie_id = companie_id;
        this.companie_nume = companie_nume;
        this.contact_id = contact_id;
        this.contact_nume = contact_nume;
        this.produs_id = produs_id;
        this.nume_produs = nume_produs;
        this.cantitate = cantitate;
        this.total = total;
        this.activ = activ;
        this.responsabil_id = responsabil_id;
        this.responsabil_nume = responsabil_nume;
        this.data_creare = data_creare;
        this.creat_de_id = creat_de_id;
        this.creat_de_nume = creat_de_nume;
        this.pret = pret;
    }

    //Functii de tip get


    public Integer getId() {
        return id;
    }

    public Integer getCompanie_id() {
        return companie_id;
    }

    public String getCompanie_nume() {
        return companie_nume;
    }

    public Integer getContact_id() {
        return contact_id;
    }

    public String getContact_nume() {
        return contact_nume;
    }

    public Integer getProdus_id() {
        return produs_id;
    }

    public String getNume_produs() {
        return nume_produs;
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public Float getTotal() {
        return total;
    }

    public Boolean getActiv() {
        return activ;
    }

    public Integer getResponsabil_id() {
        return responsabil_id;
    }

    public String getResponsabil_nume() {
        return responsabil_nume;
    }

    public Date getData_creare() {
        return data_creare;
    }

    public Integer getCreat_de_id() {
        return creat_de_id;
    }

    public String getCreat_de_nume() {
        return creat_de_nume;
    }

    public Double getPret() {
        return pret;
    }

    //Functii de tip set


    public void setId(Integer id) {
        this.id = id;
    }

    public void setCompanie_id(Integer companie_id) {
        this.companie_id = companie_id;
    }

    public void setCompanie_nume(String companie_nume) {
        this.companie_nume = companie_nume;
    }

    public void setContact_id(Integer contact_id) {
        this.contact_id = contact_id;
    }

    public void setContact_nume(String contact_nume) {
        this.contact_nume = contact_nume;
    }

    public void setProdus_id(Integer produs_id) {
        this.produs_id = produs_id;
    }

    public void setNuume_produs(String produs_nume) {
        this.nume_produs = nume_produs;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }

    public void setData_creare(Date data_creare) {
        this.data_creare = data_creare;
    }

    public void setResponsabil_id(Integer responsabil_id) {
        this.responsabil_id = responsabil_id;
    }

    public void setResponsabil_nume(String responsabil_nume) {
        this.responsabil_nume = responsabil_nume;
    }

    public void setCreat_de_id(Integer creat_de_id) {
        this.creat_de_id = creat_de_id;
    }

    public void setCreat_de_nume(String creat_de_nume) {
        this.creat_de_nume = creat_de_nume;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

}
