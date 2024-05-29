package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.util.Date;

//dependants of the foreighner
public class Dependants implements Serializable {
    private String name;
    private Date dateOfBirth;
    private String relationship;
    private String otherRelationship;

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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOtherRelationship() {
        return otherRelationship;
    }

    public void setOtherRelationship(String otherRelationship) {
        this.otherRelationship = otherRelationship;
    }
}
