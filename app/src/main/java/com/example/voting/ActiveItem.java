package com.example.voting;

import java.util.Date;

public class ActiveItem {
    Date date;
    String active;

    ActiveItem( Date date, String active){
        this.date=date;
        this.active=active;
    }

    public Date getDate() {
        return date;
    }

    public String getActive() {
        return active;
    }
}
