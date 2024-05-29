package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 12:38 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class GetPermitInformationRequest implements Serializable {
    private String documentNumber;
    private String documentType;
    private String issuingCountry;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }
}
