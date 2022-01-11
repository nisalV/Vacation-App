package com.work.vacationapp;

public class Users {
    String firstName;
    String LastName;
    String telephone;
    String nic;
    String email;
    String password;

    public Users(String firstName, String lastName, String telephone, String nic, String email, String password) {
        this.firstName = firstName;
        this.LastName = lastName;
        this.telephone = telephone;
        this.nic = nic;
        this.email = email;
        this.password = password;
    }

    public Users(String firstName, String lastName, String telephone, String nic) {
        this.firstName = firstName;
        this.LastName = lastName;
        this.telephone = telephone;
        this.nic = nic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNic() {
        return nic;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
