package com.flys.dao.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class User extends BaseEntity {
    @DatabaseField
    private String nom;
    @DatabaseField
    private String email;
    @DatabaseField
    private String imageUrl;
    @DatabaseField
    private String phone;
    @DatabaseField
    private Type type;

    public User() {
    }

    public User(String nom) {
        this.nom = nom;
    }

    public User(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public User(String nom, String email, String phone) {
        this.nom = nom;
        this.email = email;
        this.phone = phone;
    }

    public User(String nom, String email, String imageUrl, String phone) {
        this.nom = nom;
        this.email = email;
        this.imageUrl = imageUrl;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                '}';
    }

    public enum Type {
        FACEBOOK,
        GOOGLE,
        MAIL,
        PHONE
    }
}
