package com.akanwkasa.dcic.nssf.requests_and_response.nssf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 11/8/2021
 * Time: 1:49 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Setter
@Getter
@NoArgsConstructor
public class EmployeePaymentDetails implements Serializable {
    private String id;
    private String schedulenumber;
    private String receiptDate;
    private String schedulemonth;
    private String scheduleYear;
    private String nssfnumber;
    private String employerNssfnumber;
    private String totalcontribution;
    private String taxablewages;
    private String name;
    private String contributionType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchedulenumber() {
        return schedulenumber;
    }

    public void setSchedulenumber(String schedulenumber) {
        this.schedulenumber = schedulenumber;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getSchedulemonth() {
        return schedulemonth;
    }

    public void setSchedulemonth(String schedulemonth) {
        this.schedulemonth = schedulemonth;
    }

    public String getScheduleYear() {
        return scheduleYear;
    }

    public void setScheduleYear(String scheduleYear) {
        this.scheduleYear = scheduleYear;
    }

    public String getNssfnumber() {
        return nssfnumber;
    }

    public void setNssfnumber(String nssfnumber) {
        this.nssfnumber = nssfnumber;
    }

    public String getEmployerNssfnumber() {
        return employerNssfnumber;
    }

    public void setEmployerNssfnumber(String employerNssfnumber) {
        this.employerNssfnumber = employerNssfnumber;
    }

    public String getTotalcontribution() {
        return totalcontribution;
    }

    public void setTotalcontribution(String totalcontribution) {
        this.totalcontribution = totalcontribution;
    }

    public String getTaxablewages() {
        return taxablewages;
    }

    public void setTaxablewages(String taxablewages) {
        this.taxablewages = taxablewages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContributionType() {
        return contributionType;
    }

    public void setContributionType(String contributionType) {
        this.contributionType = contributionType;
    }
}
