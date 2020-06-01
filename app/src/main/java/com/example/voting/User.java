package com.example.voting;

public class User {
    private String fam, name, pat, email, phone, pass, privateKey, publicKey, FCMtoken;
    private boolean secretary;

    public User() {
        secretary=false;
    }

    public  User(String fam, String name, String pat, String email, String phone, String pass, String privateKey, String publicKey, String FCMtoken) {
        this.fam = fam;
        this.name = name;
        this.pat = pat;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.FCMtoken = FCMtoken;

        secretary=false;

    }

    public String getInfo(){
        String result="";
        result+=getFam() + " " + getName()+ " " +getPat();
        return result;
    }

    public void setFCMtoken(String FCMtoken) {
        this.FCMtoken = FCMtoken;
    }

    public String getFCMtoken() {
        return FCMtoken;
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

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
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

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
