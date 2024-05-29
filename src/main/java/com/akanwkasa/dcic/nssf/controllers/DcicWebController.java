package com.akanwkasa.dcic.nssf.controllers;

import com.akanwkasa.dcic.nssf.Core_Proccessing.ApplicationCoreProcessing;
import com.akanwkasa.dcic.nssf.Core_Proccessing.NssfCoreImplementation;
import com.akanwkasa.dcic.nssf.helpers.Response;
import com.akanwkasa.dcic.nssf.requests_and_response.DCICUSerProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 7/13/2021
 * Time: 3:37 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Controller
@RequestMapping("/dcic/web")
public class DcicWebController {
    private final NssfCoreImplementation nssf;
    private ApplicationCoreProcessing applicationCoreProccessing;


    public DcicWebController(NssfCoreImplementation nssf, @Autowired JdbcTemplate jdbcTemplate) {
        applicationCoreProccessing = new ApplicationCoreProcessing(jdbcTemplate);
        this.nssf = nssf;
    }

    @GetMapping(path = {"/index", "/"})
    public String index(ModelMap modelMap) {
        modelMap.put("data", null);
        modelMap.put("error", null);
        modelMap.put("title", "Search For Company");
        modelMap.put("page", "companies.jsp");
        return "index";
    }

    @GetMapping(path = {"/companies"})
    public String companies(ModelMap modelMap) {
        modelMap.put("data", null);
        modelMap.put("error", null);
        modelMap.put("title", "Search For Company");
        modelMap.put("page", "companies.jsp");
        return "index";
    }

    @GetMapping(path = {"/view_payments_list/{companyNssfNumber}/{companyName}"})
    public String view_payments_list(ModelMap modelMap, @PathVariable String companyNssfNumber, @PathVariable String companyName) {
        modelMap.put("data", null);
        modelMap.put("error", null);
        modelMap.put("title", "View Payments for " + companyName);
        modelMap.put("page", "payments.jsp");
        modelMap.put("nssf_number", companyNssfNumber);

        return "index";
    }

    @GetMapping(path = {"/list_of_employees/{companyNssfNumber}/{companyName}"})
    public String list_of_employees(ModelMap modelMap, @PathVariable String companyNssfNumber, @PathVariable String companyName) {
        Response response = nssf.companyEmployees(companyNssfNumber);
        modelMap.put("data", response.getData());
        modelMap.put("error", response.getCode() != 0 ? response.getMessage() : null);
        modelMap.put("title", companyName + " Employees");
        modelMap.put("page", "employees.jsp");
        return "index";
    }


    @GetMapping(path = {"/employee_details/{employeeNssfNumber}"})
    public String employeeDetails(ModelMap modelMap, @PathVariable String employeeNssfNumber) {
        Response response = nssf.employees(employeeNssfNumber);
        modelMap.put("data", response.getData());
        modelMap.put("error", response.getCode() != 0 ? response.getMessage() : null);
        modelMap.put("title", "Employee Details For " + employeeNssfNumber);
        modelMap.put("page", "employee_details.jsp");

        return "index";
    }

    @GetMapping(path = {"/login"})
    public String login(ModelMap modelMap) {
        modelMap.put("error", null);
        modelMap.put("title", "Login");
        modelMap.put("page", "login.jsp");

        return "login";
    }

    @GetMapping(path = {"/logout"})
    public String logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/dcic/web/index";
    }


    @RequestMapping(path = {"/user_login/verify"})
    public String loginService(HttpServletRequest request, ModelMap modelMap) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String response = applicationCoreProccessing.login(username, password);

        if (response != null) {

            DCICUSerProfile user = new ObjectMapper().readValue(response.toLowerCase(), DCICUSerProfile.class);

            //if the user is not allowed to view the information, redirect to login with error
            boolean isUserAllowedToUseSystem=this.applicationCoreProccessing.getUserRoles(user.getId());
            if(!isUserAllowedToUseSystem){
                modelMap.put("error", "Insufficient Rights to View this Information, Please Contact system administrator");
                modelMap.put("title", "Login");
                modelMap.put("page", "login.jsp");
                return "login";
            }


            if (user.getActive().toString().equalsIgnoreCase("1")) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("name", user.getFirstname() + " " + user.getLastname());
                httpSession.setAttribute("email", user.getEmail());
                httpSession.setAttribute("description", user.getDescription());
                httpSession.setAttribute("active", user.getActive());
                httpSession.setAttribute("is_logged_in", true);


                System.out.println(request.getRequestURI());
                System.out.println(request.getRequestURL().toString());
                System.out.println(request.getPathInfo());
                System.out.println(request.getContextPath());
                System.out.println(request.getServletPath());
                System.out.println(request.getRemoteAddr());
                System.out.println(request.getRequestURI().substring(request.getContextPath().length()));


                return "redirect:/dcic/web/index";

            } else {
                modelMap.put("error", "Your Account is not Active, Please Contact system administrator");
                modelMap.put("title", "Login");
                modelMap.put("page", "login.jsp");
                return "login";
            }


        } else {
            modelMap.put("error", "Username or Password is Incorrect");
            modelMap.put("title", "Login");
            modelMap.put("page", "login.jsp");
            return "login";
        }

    }

}
