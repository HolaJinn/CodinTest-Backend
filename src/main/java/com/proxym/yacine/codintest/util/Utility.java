package com.proxym.yacine.codintest.util;

import javax.servlet.http.HttpServletRequest;

/*
* This class contains the getSiteURL method
* Thie method helps with getting the current URL to be used to send the verification emails
* This method is used currently in the AuthController class
* */

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
