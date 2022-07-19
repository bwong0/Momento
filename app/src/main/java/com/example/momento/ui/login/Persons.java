package com.example.momento.ui.login;

import java.io.Serializable;

public class Persons implements Serializable {

    public String uri;
    public String name;
    public String relationship;
    public boolean profilePresent;

    public Persons() {
        this.uri = null;
        this.name = null;
        this.relationship = null;
        this.profilePresent = false;

    }


    public Persons(String uri, String Name, boolean profilePresent) {
        this.uri = uri;
        this.name = Name;
        this.profilePresent = profilePresent;


    }
    //copy constructor
//    public Persons(Persons person){
//        this.uri = person.uri;
//        this.name = person.name;
//        this.profilePresent = person.profilePresent;
//    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.uri;
    }
}
