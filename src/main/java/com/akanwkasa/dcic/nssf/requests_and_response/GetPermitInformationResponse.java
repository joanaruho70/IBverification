package com.akanwkasa.dcic.nssf.requests_and_response;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 3:30 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class GetPermitInformationResponse {
    private String subcategory;
    private String category;
    private String applicationType;
    private PersonData personData;

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public PersonData getPersonData() {
        return personData;
    }

    public void setPersonData(PersonData personData) {
        this.personData = personData;
    }
}

