package com.flys.dao.entities;

import java.io.Serializable;

public class User implements Serializable {

    private String nom;
    private String email;

    public User(String nom) {
        this.nom = nom;
    }

    public User(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
