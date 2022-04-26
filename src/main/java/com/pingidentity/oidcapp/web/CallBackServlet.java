package com.pingidentity.oidcapp.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pingidentity.oidcapp.OIDCClient;
import com.pingidentity.oidcapp.User;

import java.io.IOException;

@WebServlet(urlPatterns = {"/callback"}, loadOnStartup = 1)
public class CallBackServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	
    	System.out.println ("Starting CallBackServlet");
        String code = request.getParameter("code");
        User user = OIDCClient.getInstance().endAuthentication(code);
        request.getSession().setAttribute("user", user);
        response.sendRedirect("/openid-connect-example-java/");
    }
}