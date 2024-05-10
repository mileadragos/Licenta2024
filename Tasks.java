package com.example.crmjavafx;
import java.sql.Date;
public class Tasks {

    private Integer id;
    private String descriere;
    private Date deadline;
    private Integer lead_id;
    private String lead_nume;
    private Integer oportunitate_id;
    private String oportunitate_nume;
    private String status;
    private Boolean activ;
    private Integer responsabil_id;
    private String responsabil_nume;
    private Date data_creare;
    private Integer creat_de_id;
    private String creat_de_nume;

    public Tasks(
            Integer id,
            String descriere,
            Date deadline,
            Integer lead_id,
            String lead_nume,
            Integer oportunitate_id,
            String oportunitate_nume,
            String status,
            Boolean activ,
            Integer responsabil_id,
            String responsabil_nume,
            Date data_creare,
            Integer creat_de_id,
            String creat_de_nume
    ) {
        this.id = id;
        this.descriere = descriere;
        this.deadline = deadline;
        this.lead_id = lead_id;
        this.lead_nume = lead_nume;
        this.oportunitate_id = oportunitate_id;
        this.oportunitate_nume = oportunitate_nume;
        this.status = status;
        this.activ = activ;
        this.responsabil_id = responsabil_id;
        this.responsabil_nume = responsabil_nume;
        this.data_creare = data_creare;
        this.creat_de_id = creat_de_id;
        this.creat_de_nume = creat_de_nume;
    }

    //Functii de tip get

    public Integer getId() {
        return id;
    }

    public String getDescriere() {
        return descriere;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Integer getLead_id() {
        return lead_id;
    }

    public String getLead_nume() {
        return lead_nume;
    }

    public Integer getOportunitate_id() {
        return oportunitate_id;
    }

    public String getOportunitate_nume() {
        return oportunitate_nume;
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


    //Functii de tip set

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setLead_id(Integer lead_id) {
        this.lead_id = lead_id;
    }

    public void setLead_nume(String lead_nume) {
        this.lead_nume = lead_nume;
    }

    public void setOportunitate_id(Integer oportunitate_id) {
        this.oportunitate_id = oportunitate_id;
    }

    public void setOportunitate_nume(String oportunitate_nume) {
        this.oportunitate_nume = oportunitate_nume;
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

    public Tasks (
            Integer oportunitate_id,
            Integer lead_id,
            String status,
            String descriere,
            Boolean activ,
            Date data_creare,
            Date deadline,
            Integer creat_de_id,
            Integer responsabil_id
    ) {
        this.oportunitate_id = oportunitate_id;
        this.lead_id = lead_id;
        this.status = status;
        this.descriere = descriere;
        this.activ = activ;
        this.data_creare = data_creare;
        this.deadline = deadline;
        this.creat_de_id = creat_de_id;
        this.responsabil_id = responsabil_id;

    }

    public Tasks (Boolean activ, Integer id) {
        this.activ = activ;
        this.id = id;

    }
}