package com.example.crmjavafx;
import java.sql.Date;
public class Products {
    private Integer id;
    private String nume;
    private String descriere;
    private Boolean activ;
    private Integer responsabil_id;
    private String responsabil_nume;
    private Date data_creare;
    private Integer creat_de_id;
    private String creat_de_nume;

    public Products(
            Integer id,
            String nume,
            String descriere,
            Boolean activ,
            Integer responsabil_id,
            String responsabil_nume,
            Date data_creare,
            Integer creat_de_id,
            String creat_de_nume
    ) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
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

    public String getNume() {
        return nume;
    }

    public String getDescriere() {
        return descriere;
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

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
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
