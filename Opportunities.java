package com.example.crmjavafx;

import java.math.BigDecimal;
import java.sql.Date;

public class Opportunities {

    private Integer id;
    private String nume_oportunitate;
    private Integer companie_id;
    private String companie_nume;
    private Integer contact_id;
    private String contact_nume;
    private BigDecimal venit_estimat;
    private BigDecimal venit_real;
    private String status;
    private Boolean activ;
    private Integer responsabil_id;
    private String responsabil_nume;
    private Date data_creare;
    private Integer creat_de_id;
    private String creat_de_nume;

    public Opportunities(
            Integer id,
            String nume_oportunitate,
            Integer companie_id,
            String companie_nume,
            Integer contact_id,
            String contact_nume,
            BigDecimal venit_estimat,
            BigDecimal venit_real,
            String status,
            Boolean activ,
            Integer responsabil_id,
            String responsabil_nume,
            Date data_creare,
            Integer creat_de_id,
            String creat_de_nume) {
        this.id = id;
        this.nume_oportunitate = nume_oportunitate;
        this.companie_id = companie_id;
        this.companie_nume = companie_nume;
        this.contact_id = contact_id;
        this.contact_nume = contact_nume;
        this.venit_estimat = venit_estimat;
        this.venit_real = venit_real;
        this.status = status;
        this.activ = activ;
        this.responsabil_id = responsabil_id;
        this.responsabil_nume = responsabil_nume;
        this.data_creare = data_creare;
        this.creat_de_id = creat_de_id;
        this.creat_de_nume = creat_de_nume;
    }

    public Integer getId() {
        return id;
    }

    public String getNume_oportunitate() {
        return nume_oportunitate;
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

    public BigDecimal getVenit_estimat() {
        return venit_estimat;
    }

    public BigDecimal getVenit_real() {
        return venit_real;
    }

    public String getStatus() {
        return status;
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


    public void setId(Integer id) {
        this.id = id;
    }

    public void setNume_oportunitate(String nume_oportunitate) {
        this.nume_oportunitate = nume_oportunitate;
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

    public void setVenit_estimat(BigDecimal venit_estimat) {
        this.venit_estimat = venit_estimat;
    }

    public void setVenit_real(BigDecimal venit_real) {
        this.venit_real = venit_real;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActiv(Boolean activ) {
        this.activ = activ;
    }

    public void setResponsabil_id(Integer responsabil_id) {
        this.responsabil_id = responsabil_id;
    }

    public void setResponsabil_nume(String responsabil_nume) {
        this.responsabil_nume = responsabil_nume;
    }

    public void setData_creare(Date data_creare) {
        this.data_creare = data_creare;
    }

    public void setCreat_de_id(Integer creat_de_id) {
        this.creat_de_id = creat_de_id;
    }

    public void setCreat_de_nume(String creat_de_nume) {
        this.creat_de_nume = creat_de_nume;
    }
}

//Integer id = rs.getInt("id");
//String opportunityName = rs.getString("nume_oportunitate");
//Integer companyId = rs.getInt("companie_id");
//String companyName = rs.getString("nume_companie");
//Integer contactId = rs.getInt("contact_id");
//String contactName = rs.getString("nume_complet");
//Number estimatedRevenue = rs.getInt("venit_estimat");
//Number actualRevenue = rs.getInt("venit_real");
//String status = rs.getString("status");
//Boolean activ = rs.getBoolean("activ");
//Integer assignedToId = rs.getInt("responsabil_id");
//String assignedToName = rs.getString("responsabil_nume");
//Date creationDate = rs.getDate("data_creare");
//Integer createdById = rs.getInt("creat_de_id");
//String createdByName = rs.getString("creat_de_nume");
