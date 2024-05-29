package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 12:51 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class NSSFEmployeeListRequest  implements Serializable {
    private String idType="companyRegistrationNumber"; //can be TIN, licence number
    private String idValue=null; //can be TIN, licence number

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }
}
