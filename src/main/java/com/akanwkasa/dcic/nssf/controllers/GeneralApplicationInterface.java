package com.akanwkasa.dcic.nssf.controllers;

import com.akanwkasa.dcic.nssf.Core_Proccessing.ApplicationCoreProcessing;
import com.akanwkasa.dcic.nssf.Core_Proccessing.NssfCoreImplementation;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.GetImmigrationStatusRequest;
import com.akanwkasa.dcic.nssf.requests_and_response.GetPermitInformationRequest;
import com.akanwkasa.dcic.nssf.requests_and_response.HasLeftCountryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 12:36 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@RestController
@RequestMapping(path = "coesys/external/rest/nssf/")
public class GeneralApplicationInterface {
    private ObjectMapper objectMapper;
    private final NssfCoreImplementation nssf;
    private Response response;
    private String internal_server_error = "Internal Server Error has occurred";
    private String un_authorized = "Invalid Login Credentials";
    private ApplicationCoreProcessing applicationCoreProccessing;
    private Logger logger = LoggerFactory.getLogger("application-log");

    GeneralApplicationInterface(NssfCoreImplementation nssf, @Autowired JdbcTemplate jdbcTemplate) {
        this.nssf = nssf;
        objectMapper = new ObjectMapper();
        response = new Response();
        applicationCoreProccessing = new ApplicationCoreProcessing(jdbcTemplate);
    }

    private Boolean authenticateUser(String username, String transactionId, String password) {
        try {
            String storedPassword="!Nssf#Kampala@Org";

            if (!username.equalsIgnoreCase("nssf_uganda_org")) {
                logger.info("request|authenticate_user|" + username + "|" + transactionId + "|user does not exist" );
                return false;
            } else {
                String combination = storedPassword.concat(transactionId);
                String md5Hex = DigestUtils.md5DigestAsHex(combination.getBytes(StandardCharsets.UTF_8)).toUpperCase();

                if (!md5Hex.equalsIgnoreCase(password)) {
                    logger.info("request|authenticate_user|" + username + "|" + transactionId + "|password is incorrect" );

                    return false;
                } else {
                    logger.info("request|authenticate_user|" + username + "|" + transactionId + "|successful" );

                    return true;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.info("request|authenticate_user|" + username + "|" + transactionId + "|"+e.getMessage() );
            throw e;
        }

    }

    private String returnResponse() {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } finally {
            response = new Response();
        }
    }

    /* ###### the API's that NSSF will consume on the DCIC interface to pick information from immigration */

    /**
     * @return a response class indicating the permit information of the searched person
     */
    @PostMapping(path = "getPermitInformation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public String getPermitInformationRequest(@RequestBody String jsonPayload, @RequestHeader String transactionId, @RequestHeader String username, @RequestHeader String password) {
        GetPermitInformationRequest getPermitInformationRequest = new GetPermitInformationRequest();

        try {
            if (authenticateUser(username, transactionId, password)) {
                getPermitInformationRequest = this.objectMapper.readValue(jsonPayload, GetPermitInformationRequest.class);

                //reset some information
                getPermitInformationRequest.setDocumentNumber(getPermitInformationRequest.getDocumentNumber().trim().replace(" ",""));
                getPermitInformationRequest.setDocumentType(getPermitInformationRequest.getDocumentType().trim().replace(" ",""));
                getPermitInformationRequest.setIssuingCountry(getPermitInformationRequest.getIssuingCountry().trim().replace(" ",""));

                logger.info("request|get_permit_information|" + username + "|" + transactionId + "|" + getPermitInformationRequest.getDocumentNumber() + "|" + getPermitInformationRequest.getIssuingCountry() + "|OK");
                Object query_response = applicationCoreProccessing.getPermitInformation(getPermitInformationRequest.getDocumentNumber(), getPermitInformationRequest.getIssuingCountry().trim());
                response.setData(query_response);
                response.setMessage(query_response == null ? "Sorry, your query for document number " + getPermitInformationRequest.getDocumentNumber() + " returned no results" : "successful");
                response.setCode(query_response == null ? 404 : 0);

                logger.info("response|get_permit_information|" + username + "|" + transactionId + "|" + getPermitInformationRequest.getDocumentNumber() + "|" + getPermitInformationRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());
            } else {
                response.setCode(401);
                response.setMessage(un_authorized);
                response.setData(null);
                logger.info("response|get_permit_information|" + username + "|" + transactionId + "|" + getPermitInformationRequest.getDocumentNumber() + "|" + getPermitInformationRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());
            }
        } catch (Exception e) {
            logger.info("response|get_permit_information|" + username + "|" + transactionId + "|" + getPermitInformationRequest.getDocumentNumber() + "|" + getPermitInformationRequest.getIssuingCountry() + "|" + 500 + "|" + e.getMessage());
            response.setCode(500);
            response.setMessage(internal_server_error);
            response.setData(null);
        }

        return this.returnResponse();
    }


    /**
     * @return String indicating the immigration status of a particular person
     */
    @PostMapping(path = "getImmigrationStatus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public String getImmigrationStatusRequest(@RequestBody String jsonPayload, @RequestHeader String transactionId, @RequestHeader String username, @RequestHeader String password) {
        GetImmigrationStatusRequest getImmigrationStatusRequest = new GetImmigrationStatusRequest();
        try {
            if (authenticateUser(username, transactionId, password)) {

                getImmigrationStatusRequest = this.objectMapper.readValue(jsonPayload, GetImmigrationStatusRequest.class);
                logger.info("request|get_immigration_status|" + username + "|" + transactionId + "|" + getImmigrationStatusRequest.getDocumentNumber() + "|" + getImmigrationStatusRequest.getIssuingCountry() + "|" + 0 + "|OK");

                getImmigrationStatusRequest.setDocumentNumber(getImmigrationStatusRequest.getDocumentNumber().trim().replace(" ",""));
                getImmigrationStatusRequest.setDocumentType(getImmigrationStatusRequest.getDocumentType().trim().replace(" ",""));
                getImmigrationStatusRequest.setIssuingCountry(getImmigrationStatusRequest.getIssuingCountry().trim().replace(" ",""));


                Object query_response = applicationCoreProccessing.getImmigrationstatus(getImmigrationStatusRequest.getDocumentNumber(), getImmigrationStatusRequest.getIssuingCountry().trim());
                response.setData(query_response);
                response.setMessage(query_response == null ? "Sorry, your query for document number " + getImmigrationStatusRequest.getDocumentNumber() + " returned no results" : "successful");
                response.setCode(query_response == null ? 404 : 0);

                logger.info("response|get_immigration_status|" + username + "|" + transactionId + "|" + getImmigrationStatusRequest.getDocumentNumber() + "|" + getImmigrationStatusRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());
            } else {
                response.setCode(401);
                response.setMessage(un_authorized);
                response.setData(null);
                logger.info("response|get_immigration_status|" + username + "|" + transactionId + "|" + getImmigrationStatusRequest.getDocumentNumber() + "|" + getImmigrationStatusRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());

            }

        } catch (Exception e) {
            logger.info("response|get_immigration_status|" + getImmigrationStatusRequest.getDocumentNumber() + "|" + getImmigrationStatusRequest.getIssuingCountry() + "|" + 500 + "|" + e.getMessage());
            response.setData(null);
            response.setCode(500);
            response.setMessage(internal_server_error);
        }

        return this.returnResponse();
    }


    @PostMapping(path = "hasLeftCountry", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public String hasLeftCountry(@RequestBody String jsonPayload, HttpServletResponse httpServletResponse, @RequestHeader String transactionId, @RequestHeader String username, @RequestHeader String password) {
        HasLeftCountryRequest hasLeftCountryRequest = new HasLeftCountryRequest();
        try {
            if (authenticateUser(username, transactionId, password)) {
                hasLeftCountryRequest = this.objectMapper.readValue(jsonPayload, HasLeftCountryRequest.class);
                logger.info("request|has_left_country|" + username + "|" + transactionId + "|" + hasLeftCountryRequest.getDocumentNumber() + "|" + hasLeftCountryRequest.getIssuingCountry() + "|" + 0 + "|OK");

                hasLeftCountryRequest.setDocumentNumber(hasLeftCountryRequest.getDocumentNumber().trim().replace(" ",""));
                hasLeftCountryRequest.setDocumentType(hasLeftCountryRequest.getDocumentType().trim().replace(" ",""));
                hasLeftCountryRequest.setIssuingCountry(hasLeftCountryRequest.getIssuingCountry().trim().replace(" ",""));


                Object query_response = applicationCoreProccessing.getTravelStatus(hasLeftCountryRequest.getDocumentNumber(), hasLeftCountryRequest.getIssuingCountry().trim());
                response.setData(query_response);
                response.setMessage(query_response == null ? "Sorry, your query for document number " + hasLeftCountryRequest.getDocumentNumber() + " returned no results" : "successful");
                response.setCode(query_response == null ? 404 : 0);

                logger.info("response|has_left_country|" + username + "|" + transactionId + "|" + hasLeftCountryRequest.getDocumentNumber() + "|" + hasLeftCountryRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());
            } else {
                response.setCode(401);
                response.setMessage(un_authorized);
                response.setData(null);
                logger.info("response|has_left_country|" + username + "|" + transactionId + "|" + hasLeftCountryRequest.getDocumentNumber() + "|" + hasLeftCountryRequest.getIssuingCountry() + "|" + response.getCode() + "|" + response.getMessage());

            }
        } catch (Exception e) {
            logger.info("response|has_left_country|" + username + "|" + transactionId + "|" + hasLeftCountryRequest.getDocumentNumber() + "|" + hasLeftCountryRequest.getIssuingCountry() + "|" + 500 + "|" + e.getMessage());
            response.setData(null);
            response.setCode(500);
            response.setMessage(internal_server_error);
        }

        httpServletResponse.setHeader("ContentType", "application/json");
        httpServletResponse.setHeader("Source", "DCIC");
        return this.returnResponse();
    }


    /*###### the API's that DCIC will consume to pick information from NSSF about the company information and i
        individual information of a foreighner
    */
    @GetMapping(path = "companies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public String companies() throws JsonProcessingException {
        return  new ObjectMapper().writeValueAsString(nssf.companies(null));
    }

    @GetMapping(path = "employees", produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public String employee() throws JsonProcessingException {
        return  new ObjectMapper().writeValueAsString(nssf.companies(null));
    }
}
