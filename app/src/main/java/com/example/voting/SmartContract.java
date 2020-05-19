package com.example.voting;

public class SmartContract {
    private String address;
    private String name;
    private String description;
    private boolean statusActive;


    public SmartContract() {
    }

    SmartContract(String address, String name, String description ){
        this.address=address;
        this.name=name;
        this.description=description;
        this.statusActive=true;
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

    public void setStatusActive(boolean statusActive){
        this.statusActive = statusActive;
    }

    public boolean isStatusActive() {
        return statusActive;
    }

}
