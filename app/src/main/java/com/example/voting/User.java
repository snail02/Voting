package com.example.voting;

public class User {
    private String fam, name, pat, email, phone, pass;
    private boolean secretary;

    public User() {}

    public  User(String fam, String name, String pat, String email, String phone, String pass) {
        this.fam = fam;
        this.name = name;
        this.pat = pat;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        secretary=false;

    }

    public String getName() {
        return name;
    }

    public String getFam() {
        return fam;
    }

    public String getPat() {
        return pat;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPass() {
        return pass;
    }

    public boolean isSecretary() {
        return secretary;
    }

    public void setSecretary(boolean secretary) {
        this.secretary = secretary;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPat(String pat) {
        this.pat = pat;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
