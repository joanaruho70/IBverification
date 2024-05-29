package com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/18/2023
 * Time: 2:34 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Data
public class FingerPrintBiometricVerificationRequest implements Serializable {

    private String firtName;
    private String transactionId;
    private String middleName;
    private String lastName;
    private String documentNumber;
    private String documentType="passport";
    private String issuingcountry;
    private String passportExpiryDate;
    private List<Fingers> fingers;
}
