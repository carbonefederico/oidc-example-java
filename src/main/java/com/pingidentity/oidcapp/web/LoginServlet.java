package com.pingidentity.oidcapp.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pingidentity.oidcapp.OIDCClient;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login"}, loadOnStartup = 1)
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	System.out.println ("Starting LoginServlet");
        String redirectUrl = OIDCClient.getInstance().createAuthenticationUrl();
        System.out.println ("Redirecting to " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}