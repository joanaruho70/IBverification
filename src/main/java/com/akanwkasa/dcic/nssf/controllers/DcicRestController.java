package com.akanwkasa.dcic.nssf.controllers;

import com.akanwkasa.dcic.nssf.Core_Proccessing.NssfCoreImplementation;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 8/30/2021
 * Time: 4:10 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@RestController
@RequestMapping("/dcic/rest/")


public class DcicRestController {
    private Logger logger = LoggerFactory.getLogger("application-log");
    private final NssfCoreImplementation nssf;

    public DcicRestController(NssfCoreImplementation nssf) {
        this.nssf = nssf;
    }


    @GetMapping(path = {"/company/{companyName}"})
    public String getEmployeesOfGivenCompany(@PathVariable String companyName) throws JsonProcessingException {
        logger.info("request|company|" + companyName);
        Response response = nssf.companies(companyName);
        logger.info("response|company|" + companyName + "|" + new ObjectMapper().writeValueAsString(response));
        return new ObjectMapper().writeValueAsString(response);
    }

    //get the details of one employee
    @GetMapping(path = {"/foreighner/{nssfNumber}"})
    public String getEmployeeByNssfNumber(@PathVariable String nssfNumber) throws JsonProcessingException {
        logger.info("request|foreignee|" + nssfNumber);
        Response response = nssf.employees(nssfNumber);
        logger.info("response|foreigner|" + nssfNumber + "|" + new ObjectMapper().writeValueAsString(response));
        return new ObjectMapper().writeValueAsString(response);
    }



    //#######################3these are the APIS for DCIC to NSSF
    //get the list of company employees using the company nssf number
    @GetMapping(path = {"/list_of_foreighners/{nssfNumber}"})
    public String getListOfEmployeeByCompanyNssfNumber(@PathVariable String nssfNumber) throws JsonProcessingException {
        logger.info("request|list_of_foreighners|" + nssfNumber);
        Response response = nssf.companyEmployees(nssfNumber);
        logger.info("response|list_of_foreighners|" + nssfNumber + "|" + new ObjectMapper().writeValueAsString(response));
        return new ObjectMapper().writeValueAsString(response);
    }


    //get the list of company employees using the company nssf number
    @PostMapping(path = {"/search_payments_by_date_range/{nssfNumber}"},consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchPaymentsByDateRange(@RequestBody String formInput,@PathVariable String nssfNumber) throws JsonProcessingException {
        logger.info("request|search_payments_by_date_range|" + nssfNumber);
        System.out.println(new ObjectMapper().writeValueAsString(URLDecoder.decode(formInput)));

        String[] split_dates=formInput.split("&");
        String sDate=split_dates[0].split("=")[1];
        String eDate=split_dates[1].split("=")[1];

        sDate=formatDate(sDate);
        eDate=formatDate(eDate);

        Response response = nssf.getCompanyPaymentsByDateRange(nssfNumber,sDate, eDate);

        logger.info("response|search_payments_by_date_range|" + nssfNumber + "|" + new ObjectMapper().writeValueAsString(response));
        return new ObjectMapper().writeValueAsString(response);
    }


    private  String formatDate(String date){
        String[] _d=date.split("-");return _d[1]+"-"+_d[0];
    }


    //################# - blank method used to execute requests on behalf of non vpn applications
    @GetMapping(path = {"/proxy_link"})
    private Response proxyLink(@RequestHeader String link) {

        try {
            URL url = new URL(null, link.trim(), new sun.net.www.protocol.http.Handler());

            URLConnection con = url.openConnection();
            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("GET");
            http.setInstanceFollowRedirects(false);

            http.setDoOutput(true);

            String httpResponseMessage = http.getResponseMessage();
            String responseBody = IOUtils.toString(http.getInputStream(), "UTF-8");
            int http_response_code = http.getResponseCode();


            if (http_response_code == 200 || http_response_code == 201 || http_response_code == 202) {
                //close the connection
                http.disconnect();
                return new Response(0, httpResponseMessage, responseBody);

            } else {
                responseBody = IOUtils.toString(http.getErrorStream(), "UTF-8");
                //close the connection
                http.disconnect();
                return new Response(http.getResponseCode(), HttpStatus.valueOf(http_response_code).toString(), responseBody);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage() != null ? exception.getMessage() : exception.getCause().getMessage());

        } catch (Exception exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage());

        }
    }

}

