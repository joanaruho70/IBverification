package com.akanwkasa.dcic.nssf.helpers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 3:48 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

@Component
public class CommonOperations {

    public String convertBlobToBase64String(Object blobData) {
        byte[] blob = (byte[])blobData;

        if (blob == null) {
            return StringUtils.EMPTY;
        }

        return Base64.getEncoder().encodeToString(blob);
    }

    public void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            boolean isLoggedIn = session.getAttribute("is_logged_in") != null && (boolean) session.getAttribute("is_logged_in");

            if (!isLoggedIn) {
                response.sendRedirect(request.getContextPath()+"/dcic/web/login");
            }

        } catch (Exception ex) {
            response.sendRedirect("/dcic/web/login");
        }
    }

}
