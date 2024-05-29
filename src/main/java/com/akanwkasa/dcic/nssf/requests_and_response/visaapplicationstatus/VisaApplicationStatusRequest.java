package com.akanwkasa.dcic.nssf.requests_and_response.visaapplicationstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 7/7/2022
 * Time: 11:44 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisaApplicationStatusRequest implements Serializable {
    private String documentType;
    private String applicationId;
    private String passport;
    private String issuingCountry;
    private String dateOfBirth;
}
