package com.akanwkasa.dcic.nssf.controllers;


import com.akanwkasa.dcic.nssf.Core_Proccessing.NssfCoreImplementation;
import com.akanwkasa.dcic.nssf.helpers.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 7/13/2021
 * Time: 3:37 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Controller
@RequestMapping("/dcic/login")
public class LoginController {
    @RequestMapping(path = {"/user_login"})
    public String login() {
        return "login";
    }

    @PostMapping(path = {"/user_login/verify"})
    public String login_verify(HttpServletRequest request) {
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        String errorResponse = null;

        return "login";
    }

    @GetMapping
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/dcic/web/user_login";
    }
}
