package com.example.crmjavafx;

public class Users {

    private Integer id;
    private String username;
    private String password;
    private String rol;

    //Constructorul clasei Users
    public Users(Integer id,
                 String username,
                 String password,
                 String rol) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;

    }

    //Functii de tip Get
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getRol() {
        return rol;
    }


    //Functii de tip Set
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
}
