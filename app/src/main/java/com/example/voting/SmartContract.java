package com.example.voting;

public class SmartContract {
    private String address;
    private String name;
    private String description;
    private boolean statusActive;
    private boolean statusVoted;


    SmartContract(){
        this.address=null;
        this.name=null;
        this.description=null;
        this.statusActive=true;
        this.statusVoted=false;
    }

    SmartContract(String address, String name, String description ){
        this.address=address;
        this.name=name;
        this.description=description;
        this.statusActive=true;
        this.statusVoted=false;
    }

    SmartContract(String address, String name, String description, boolean statusActive, boolean statusVoted ){
        this.address=address;
        this.name=name;
        this.description=description;
        this.statusActive=statusActive;
        this.statusVoted=statusVoted;
    }


    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatusActive() {
        return statusActive;
    }

    public boolean isStatusVoted() {
        return statusVoted;
    }
}
