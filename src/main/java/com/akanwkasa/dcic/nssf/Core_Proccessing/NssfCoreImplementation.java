package com.akanwkasa.dcic.nssf.Core_Proccessing;

import com.akanwkasa.dcic.nssf.helpers.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 6/28/2021
 * Time: 10:07 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Component(value = "NssfCoreImplementation")
public class NssfCoreImplementation {

    private String endpoint = null;

    @Value("${nssf.authenticationTokenUrl}")
    String authenticationTokenUrl;

    @Value("${nssf.getCompanyUrl}")
    String getCompanyUrl;

    @Value("${nssf.getForeighnerUrl}")
    String getForeighnerUrl;

    @Value("${nssf.getForeighnersUnderCompanyUrl}")
    String getCompanyEmployeesUrl;

    @Value("${nssf.getPaymentsUrl}")
    String getPaymentsUrl;

    public void getAuthenticationToken() {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("username", "nssf");
            payload.put("password", "password");
        } catch (Exception e) {

        }
    }


    //the below method is used to search for companies using the company name,
    //one can pass a whole name of part of the name
    public Response companies(String companyName) {
        try {

            if (companyName != null) {
                this.endpoint = getCompanyUrl + "/" + URLEncoder.encode(companyName, "UTF-8");
            } else {
                this.endpoint = getCompanyUrl;
            }

            return executeGet();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, e.getMessage());
        }
    }


    //get Single  Foreighner details by passing the foregner number picked from the list of employees returned in the get company employees API
    public Response employees(String nssfNumber) {
        try {
            if (nssfNumber != null) {
                this.endpoint = getForeighnerUrl + "/" + URLEncoder.encode(nssfNumber, "UTF-8");
            } else {
                this.endpoint = getForeighnerUrl;
            }


            return executeGet();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new Response(500, e.getMessage());

        }
    }

    public Response getCompanyPaymentsByDateRange(String nssfNumber, String startDate, String endDate) {
        try {
            if (nssfNumber != null) {
                this.endpoint = String.format(getPaymentsUrl, URLEncoder.encode(nssfNumber, "UTF-8"), startDate, endDate);
            } else {
                this.endpoint = getPaymentsUrl;
            }

            return executeGet();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new Response(500, e.getMessage());

        }
    }


    //this is used to get the list of employees under a certain company,
    //you first search for the company and then get he list of the employees under the company by passing the company nssf number
    public Response companyEmployees(String nssfNumber) {
        try {
            if (nssfNumber != null) {
                this.endpoint = String.format(getCompanyEmployeesUrl, nssfNumber);
            } else {
                this.endpoint = getCompanyEmployeesUrl;
            }

            return executeGet();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, e.getMessage());

        }
    }

    private Response executeGet(/*Map<String, Object> headers*/) {
        try {
            System.out.println("\nCalling = "+this.endpoint);

            URL url = new URL(null, this.endpoint.trim(), new sun.net.www.protocol.http.Handler());

            URLConnection con = url.openConnection();
            con.setConnectTimeout(40000);
            con.setReadTimeout(40000);
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("GET");
            http.setInstanceFollowRedirects(false);

            http.setDoOutput(true);
            http.connect();

            String httpResponseMessage = http.getResponseMessage();
            String responseBody = IOUtils.toString(http.getInputStream(), "UTF-8");
            int http_response_code = http.getResponseCode();


            if (http_response_code == 200 || http_response_code == 201 || http_response_code == 202) {
                //close the connection
                http.disconnect();
                this.endpoint = null;
                return new Response(0, httpResponseMessage, responseBody);

            } else {
                responseBody = IOUtils.toString(http.getErrorStream(), "UTF-8");
                //close the connection
                http.disconnect();
                this.endpoint = null;
                return new Response(http.getResponseCode(), HttpStatus.valueOf(http_response_code).toString(), responseBody);
            }

        } catch (UnknownHostException exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage() != null ? exception.getMessage() : exception.getCause().getMessage());

        } catch (IOException exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage() != null ? exception.getMessage() : exception.getCause().getMessage());

        } catch (Exception exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage());

        } finally {
            this.endpoint = null;
        }
    }


    //use this method only when you are calling the proxy application hosted on the train01 server
    private Response executeGetProxy(/*Map<String, Object> headers*/) {
        try {
            System.out.println("\nCalling = "+this.endpoint);
            String proxyLink="http://154.72.192.131:8888/interface/dcic/rest/proxy_link";

//            URL url = new URL(null, this.endpoint.trim(), new sun.net.www.protocol.http.Handler());
            URL url = new URL(null, proxyLink, new sun.net.www.protocol.http.Handler());

            //add some small headers to aid testing the proxy app
            Map<String,String> headers=new HashMap<>();
            headers.put("link",this.endpoint);

            URLConnection con = url.openConnection();
            con.setConnectTimeout(40000);
            con.setReadTimeout(40000);
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("GET");
            http.setInstanceFollowRedirects(false);

            http.setDoOutput(true);
            headers.forEach((k, v) -> http.setRequestProperty(k, v.toString()));
            http.connect();

            String httpResponseMessage = http.getResponseMessage();
            String responseBody = IOUtils.toString(http.getInputStream(), "UTF-8");
            int http_response_code = http.getResponseCode();


            if (http_response_code == 200 || http_response_code == 201 || http_response_code == 202) {
                //close the connection
                http.disconnect();
                this.endpoint = null;
                Response _res=new ObjectMapper().readValue(responseBody,Response.class);
                return new Response(0, httpResponseMessage, _res.getData());
//                return new Response(0, httpResponseMessage, responseBody);

            } else {
                responseBody = IOUtils.toString(http.getErrorStream(), "UTF-8");
                //close the connection
                http.disconnect();
                this.endpoint = null;
                return new Response(http.getResponseCode(), HttpStatus.valueOf(http_response_code).toString(), responseBody);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage() != null ? exception.getMessage() : exception.getCause().getMessage());

        } catch (Exception exception) {
            exception.printStackTrace();
            return new Response(500, exception.getMessage());

        } finally {
            this.endpoint = null;
        }
    }
}
