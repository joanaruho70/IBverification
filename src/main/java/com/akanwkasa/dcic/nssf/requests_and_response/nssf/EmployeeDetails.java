package com.akanwkasa.dcic.nssf.requests_and_response.nssf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 11/8/2021
 * Time: 1:48 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDetails implements Serializable {
    private String surname;
    private String otherName;
    private String sex;
    private String dateOfBirth;
    private String nationality;
    private String jobDescription;
    private String passportNumber;
    private String nssfNumber;
    private List<EmployeePaymentDetails> payments;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNssfNumber() {
        return nssfNumber;
    }

    public void setNssfNumber(String nssfNumber) {
        this.nssfNumber = nssfNumber;
    }

    public List<EmployeePaymentDetails> getPayments() {
        return payments;
    }

    public void setPayments(List<EmployeePaymentDetails> payments) {
        this.payments = payments;
    }
}
