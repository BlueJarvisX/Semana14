package com.example.kevin.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private String photoUrl;

    public User() {}

    public User(String uid, String name, String email, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    // Getters
    public String getUid() {
        return uid != null ? uid : "";
    }

    public String getName() {
        return name != null && !name.isEmpty() ? name : "(Sin nombre)";
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public String getPhotoUrl() {
        return photoUrl != null ? photoUrl : "";
    }

    // Setters
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // Utilidad para mostrar en listas
    @Override
    public String toString() {
        return getName();
    }
}
