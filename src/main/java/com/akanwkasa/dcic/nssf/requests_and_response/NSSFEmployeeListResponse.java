package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 12:53 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class NSSFEmployeeListResponse implements Serializable {
    private List<NSSFEmployeeDetails> nssfEmplolyeeList;

    public List<NSSFEmployeeDetails> getNssfEmplolyeeList() {
        return nssfEmplolyeeList;
    }

    public void setNssfEmplolyeeList(List<NSSFEmployeeDetails> nssfEmplolyeeList) {
        this.nssfEmplolyeeList = nssfEmplolyeeList;
    }
}

class NSSFEmployeeDetails {
    private String NSSFNumber;
    private String employeeLastName;
    private String employeeSurname;

    public String getNSSFNumber() {
        return NSSFNumber;
    }

    public void setNSSFNumber(String NSSFNumber) {
        this.NSSFNumber = NSSFNumber;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(String employeeSurname) {
        this.employeeSurname = employeeSurname;
    }
}
