package com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/18/2023
 * Time: 4:06 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */



@Data
public class FingerPrintBiometricVerificationResponse implements Serializable {
    private String fullName;
    private String applicationNumber;
    private String passportNumber;
    private String nationality;
    private String dateOfBirth;
    private Object photo;
    private String applicationtype;
    private String applicationStatus;
    private String visaExpirationDate;
    private String message;
    private String placeOfBirth;
    private String email;
    private String documentPlaceOfIssue;
    private String matchingScore;
    private String providedFingerPrint;
    private String fingerPrintFromDatabase;
    private String fingerType;
}
