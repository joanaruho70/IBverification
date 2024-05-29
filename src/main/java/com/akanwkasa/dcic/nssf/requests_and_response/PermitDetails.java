package com.akanwkasa.dcic.nssf.requests_and_response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermitDetails implements Serializable {
    private Object applicationType;
    private Object category;
    private Object subcategory;
    private Object status;
    private Object permitNumber;
    private Object permitType;
    private Object issuingCountry;
    private Object documentDateOfIssue;
    private Object documentDateOfExpiry;
    private Object organizationCode;
    private Object permitDateOfExpiry;
    private Object permitDateOfIssue;
    private Object birthDay;
}
