package com.akanwkasa.dcic.nssf.requests_and_response.visaapplicationstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 7/7/2022
 * Time: 11:47 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisaApplicationStatusResponse implements Serializable {
    private Object applicationId;
    private Object applicationStatus;
    private Object applicationType;
    private Object category;
    private Object creationDate;
    private Object visaIssueDate;
    private Object expiryDate;
    private Object subcategory;
    private Object paymentStatus;
    private Object reasonForCancellation;
    private Object birthDay;
    private Object firstName;
    private Object lastName;
    private Object email;
    private Object passportNumber;
    private Object placeOfIssue;
    private Object countryOfBirth;
    private Object passportIssuingCountry;
    private Object nationality;
    private Object paymentDetails;
    private Object visaIssuingCountry;

}
