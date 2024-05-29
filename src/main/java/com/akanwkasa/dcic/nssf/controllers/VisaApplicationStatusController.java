package com.akanwkasa.dcic.nssf.controllers;

import com.akanwkasa.dcic.nssf.Core_Proccessing.ApplicationCoreProcessing;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.HasLeftCountryRequest;
import com.akanwkasa.dcic.nssf.requests_and_response.visaapplicationstatus.VisaApplicationStatusRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 7/7/2022
 * Time: 11:40 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@RestController
@RequestMapping("/evisa/visa_status/")
public class VisaApplicationStatusController {
    private Logger logger = LoggerFactory.getLogger("application-log");
    private final ApplicationCoreProcessing app;
    private ObjectMapper objectMapper=new ObjectMapper();
    public VisaApplicationStatusController(ApplicationCoreProcessing app) {
        this.app = app;
    }


    //################# this is for returning the visa status to the airlines who wish to confirm the visa status of the users
    @PostMapping(path = {"confirm"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    @CrossOrigin(origins = "*")
    public String checkVisaApplicationStatus(@RequestBody String jsonPayload) throws JsonProcessingException {
        try {
            VisaApplicationStatusRequest input=this.objectMapper.readValue(jsonPayload, VisaApplicationStatusRequest.class);
            logger.info("request|check _visa_status|" + input);
            Response response = app.checkVisaApplicationStatus(input);
            logger.info("response|_visa_status|" + "|" + new ObjectMapper().writeValueAsString(response));
            return new ObjectMapper().writeValueAsString(response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectMapper().writeValueAsString(new Response(500, "internal Server Error"));
        }
    }

    //################# this is for returning the visa status to the airlines who wish to confirm the visa status of the users
    @GetMapping(path = {"confirm_status/{applicationNumber}/{passportNumber}"},produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    @CrossOrigin(origins = "*")
    public String checkVisaApplicationStatus(@PathVariable String applicationNumber,@PathVariable String passportNumber) throws JsonProcessingException {
        try {
            logger.info("request|check _visa_status|" + applicationNumber+"|"+passportNumber);
            VisaApplicationStatusRequest input = new VisaApplicationStatusRequest();
            input.setPassport(passportNumber);
            input.setApplicationId(applicationNumber);
            Response response = app.checkVisaApplicationStatus(input);
            logger.info("response|_visa_status|" + "|" + new ObjectMapper().writeValueAsString(response));
            return new ObjectMapper().writeValueAsString(response);
        } catch (Exception e) {
            logger.error("request|check _visa_status|" + applicationNumber+"|"+passportNumber+e.getMessage());
            e.printStackTrace();
            return new ObjectMapper().writeValueAsString(new Response(500, "internal Server Error"));
        }
    }
}
