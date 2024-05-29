package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.util.Date;

//known children of the foreginer
public class Children implements Serializable {
    private String name;
    private Date dateOfBirth;
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
