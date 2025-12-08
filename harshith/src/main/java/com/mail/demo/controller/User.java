package com.mail.demo.controller;

import java.util.ArrayList;

public class User {
    private String email;
    private String name;
    private String password;
    private String dob;
    private String gender;
    //private int count=0;
    //private boolean spam=false;

    private boolean isSpammer=false;
    private ArrayList<String> reporters=new ArrayList<>();

    public String getDOB() {
        return dob;
    }

    public void setDOB(String dateOfBirth) {
        dob = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addReporter(String reporter){
        if(reporters.contains(reporter)){
            return;
        }
        if (reporters.size() < 5) {
            reporters.add(reporter);
        }
        if (reporters.size() >= 5) {
            isSpammer = true;
        }
    }

    public boolean isSpammer() {
        return isSpammer;
    }

    public boolean getSpammer() {  // Add this
        return isSpammer;
    }

    public void setSpammer(boolean spammer) {  // Add this
        this.isSpammer = spammer;
    }

}
