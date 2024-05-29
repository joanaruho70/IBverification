package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 3:16 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class GetImmigrationStatusResponse implements Serializable {
    private Object previousPassports;
    private Object surName;
    private Object firstName;
    private Object maidenName;
    private Object gender;
    private Object dateOfBirth;
    private Object countryOfBirth;
    private Object placeOfBirth;
    private Object nationality;
    private Object countryOfResidence;
    private Object permitNumber;
    private Object applicationType;
    private Object category;
    private Object nameOfEmployer;
    private Object employerTelephoneNumber;
    private Object spouseName;
    private Object signatureImage;
    private Object photoImage;
    private List<PermitDetails> permits;
    private Object travelDate;
    private Object pointOfExit;
    private Object travelStatus;

    public Object getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(Object spouseName) {
        this.spouseName = spouseName;
    }

    public Object getPreviousPassports() {
        return previousPassports;
    }

    public void setPreviousPassports(Object previousPassports) {
        this.previousPassports = previousPassports;
    }

    public Object getSurName() {
        return surName;
    }

    public void setSurName(Object surName) {
        this.surName = surName;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(Object maidenName) {
        this.maidenName = maidenName;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(Object countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public Object getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(Object placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }


    public Object getNationality() {
        return nationality;
    }

    public void setNationality(Object nationality) {
        this.nationality = nationality;
    }

    public Object getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(Object countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public Object getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(Object permitNumber) {
        this.permitNumber = permitNumber;
    }

    public Object getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(Object applicationType) {
        this.applicationType = applicationType;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getNameOfEmployer() {
        return nameOfEmployer;
    }

    public void setNameOfEmployer(Object nameOfEmployer) {
        this.nameOfEmployer = nameOfEmployer;
    }

    public Object getEmployerTelephoneNumber() {
        return employerTelephoneNumber;
    }

    public void setEmployerTelephoneNumber(Object employerTelephoneNumber) {
        this.employerTelephoneNumber = employerTelephoneNumber;
    }

    public Object getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(Object signatureImage) {
        this.signatureImage = signatureImage;
    }

    public Object getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(Object photoImage) {
        this.photoImage = photoImage;
    }

    public List<PermitDetails> getPermits() {
        return permits;
    }

    public void setPermits(List<PermitDetails> permits) {
        this.permits = permits;
    }

    public Object getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Object travelDate) {
        this.travelDate = travelDate;
    }

    public Object getPointOfExit() {
        return pointOfExit;
    }

    public void setPointOfExit(Object pointOfExit) {
        this.pointOfExit = pointOfExit;
    }

    public Object getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(Object travelStatus) {
        this.travelStatus = travelStatus;
    }
}

