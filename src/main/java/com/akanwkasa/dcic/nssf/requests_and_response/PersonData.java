package com.akanwkasa.dcic.nssf.requests_and_response;

import java.util.Date;
import java.util.List;

//personal data defining the person/foreighner
public class PersonData {
    private String surName = "";
    private String firstName = "";
    private String gender = "";
    private String birthday;
    private String nationality = "";
    private String placeOfBirth = "";
    private String email = "";
    private String permitNumber = "";
    private String nameOfEmployer = "";
    private String occupation = "";
    private String profession = "";
    private String documentDateOfIssue;
    private String documentDateOfExpiry;
    private String previousPassports = "";
    private Object photoImage;//base64encoded
    private String organizationCode = "";
    private String phoneNumberPersonal = "";
    private String residentialAddress = "";
    private String countryOfResidence = "";
    private List<FingerImages> fingerImages;


    public String getNameOfEmployer() {
        return nameOfEmployer;
    }

    public void setNameOfEmployer(String nameOfEmployer) {
        this.nameOfEmployer = nameOfEmployer;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDocumentDateOfIssue() {
        return documentDateOfIssue;
    }

    public void setDocumentDateOfIssue(String documentDateOfIssue) {
        this.documentDateOfIssue = documentDateOfIssue;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public Object getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(Object photoImage) {
        this.photoImage = photoImage;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDocumentDateOfExpiry() {
        return documentDateOfExpiry;
    }

    public void setDocumentDateOfExpiry(String documentDateOfExpiry) {
        this.documentDateOfExpiry = documentDateOfExpiry;
    }

    public String getPreviousPassports() {
        return previousPassports;
    }

    public void setPreviousPassports(String previousPassports) {
        this.previousPassports = previousPassports;
    }


    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }


    public String getPhoneNumberPersonal() {
        return phoneNumberPersonal;
    }

    public void setPhoneNumberPersonal(String phoneNumberPersonal) {
        this.phoneNumberPersonal = phoneNumberPersonal;
    }


    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }


    public List<FingerImages> getFingerImages() {
        return fingerImages;
    }

    public void setFingerImages(List<FingerImages> fingerImages) {
        this.fingerImages = fingerImages;
    }
}
