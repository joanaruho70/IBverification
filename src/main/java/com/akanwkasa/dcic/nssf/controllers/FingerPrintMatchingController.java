package com.akanwkasa.dcic.nssf.controllers;

import com.akanwkasa.dcic.nssf.Core_Proccessing.FingerPrintMatchingApplicationLogic;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.biometric_verification.FingerPrintBiometricVerificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 1/18/2023
 * Time: 2:28 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@RestController
@RequestMapping(value = "/offsite_biometric")
public class FingerPrintMatchingController {

    private Logger logger = LoggerFactory.getLogger("offsite_biometric_verification");
    private final FingerPrintMatchingApplicationLogic app;

    public FingerPrintMatchingController(FingerPrintMatchingApplicationLogic applicationLogic) {
        this.app = applicationLogic;
    }

    @Validated
    @PostMapping(value = "/validate_finger_print", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response matchfingerPrints(@RequestBody FingerPrintBiometricVerificationRequest input) {
        try {
            String transactionId = input.getDocumentNumber() + "_" + UUID.randomUUID().toString();
            input.setTransactionId(transactionId);
            logger.info("{} | {} | {} | {} | request | 000 | OK ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType());

            Response response = app.validateFingerprint(input);

            logger.info("{} | {} | {} | {} | response | {} | {}  ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType(),response.getCode(), response.getMessage());

            response.setMessage(response.getCode()!=500?response.getMessage():"Internal Error Occurred While Proccessing");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Internal Server Error");
        }
    }


    @Validated
    @PostMapping(value = "/validate_document_number", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response validateDocumentNumber(@RequestBody FingerPrintBiometricVerificationRequest input) {
        try {
            String transactionId = input.getDocumentNumber() + "_" + UUID.randomUUID().toString();
            input.setTransactionId(transactionId);
            logger.info("{} | {} | {} | {} | request | 000 | OK ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType());

            Response response = app.validateDocumentNumber(input);

            logger.info("{} | {} | {} | {} | response | {} | {}  ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType(),response.getCode(), response.getMessage());

            response.setMessage(response.getCode()!=500?response.getMessage():"Internal Error Occurred While Proccessing");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Internal Server Error");
        }
    }

    @Validated
    @PostMapping(value = "/validate_document_number_fp", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response validateDocumentNumberFP(@RequestBody FingerPrintBiometricVerificationRequest input) {
        try {
            String transactionId = input.getDocumentNumber() + "_" + UUID.randomUUID().toString();
            input.setTransactionId(transactionId);
            logger.info("{} | {} | {} | {} | request | 000 | OK ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType());

            Response response = app.findPersonByFingerPrintOnly(input);

            logger.info("{} | {} | {} | {} | response | {} | {}  ", transactionId, input.getDocumentNumber(), input.getIssuingcountry(), input.getDocumentType(),response.getCode(), response.getMessage());

            response.setMessage(response.getCode()!=500?response.getMessage():"Internal Error Occurred While Proccessing");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Internal Server Error");
        }
    }
}
