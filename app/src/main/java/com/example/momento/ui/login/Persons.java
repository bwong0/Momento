package com.example.momento.ui.login;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;
import java.net.URI;

public class Persons implements Serializable {

    public String profilePicutre;
    public String prompt1;
    public String prompt2;
    public String prompt3;
    public String name;
    public String relationship;
    public boolean profilePresent;

    public Persons() {
        this.profilePicutre = "";
        this.name = "";
        this.relationship = "";
        this.profilePresent = false;
        this.prompt1 = "";
        this.prompt2 = "";
        this.prompt3 = "";

    }


    public Persons(String profilePicutre, String Name, boolean profilePresent) {
        this.profilePicutre = profilePicutre;
        this.name = Name;
        this.profilePresent = profilePresent;
//        this.prompt1 = prompt1;
//        this.prompt2 = prompt2;
//        this.prompt3 = prompt3;


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
        return this.profilePicutre;
    }

    public String getPrompt1() {
        return prompt1;
    }

    public String getPrompt2() {
        return prompt2;
    }

    public String getPrompt3() {
        return prompt3;
    }

    public void setProfilePresent(boolean profilePresent) {
        this.profilePresent = profilePresent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
