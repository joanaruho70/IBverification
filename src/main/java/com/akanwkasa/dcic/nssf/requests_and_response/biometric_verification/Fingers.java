package com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/18/2023
 * Time: 2:39 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Data
public class Fingers implements Serializable {
   private String fingerType;
   private String fingerPrint;
}
