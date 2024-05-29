package com.akanwkasa.dcic.nssf.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/13/2021
 * Time: 11:34 AM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, ModelMap modelMap) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = Integer.valueOf(status.toString());

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            modelMap.put("code", status.toString()+" - Not Found");
            modelMap.put("message","Sorry, the resource you are trying to access cannot be found, Make sure you typed the correct resource name in the browser");
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            modelMap.put("code", status.toString()+" - Internal Server Error");
            modelMap.put("message","Sorry, An Internal Server Error has occurred.  Please go to home page and start afresh. if the error persists, contact an administrator");
        }else{
            modelMap.put("code", "Error : "+ status.toString());
            modelMap.put("message",HttpStatus.valueOf(statusCode).toString());
        }


        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
