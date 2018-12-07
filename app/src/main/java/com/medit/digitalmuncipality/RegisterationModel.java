package com.medit.digitalmuncipality;

public class RegisterationModel {

    String name;
    String pass;
    String phone;

    public RegisterationModel() {
        //empty for firebase database
    }

    public RegisterationModel(String name, String pass, String phone) {
        this.name = name;
        this.pass = pass;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
