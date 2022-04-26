package com.pingidentity.oidcapp.web;

import org.json.JSONObject;

import com.pingidentity.oidcapp.OIDCClient;
import com.pingidentity.oidcapp.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/"}, loadOnStartup = 1)
public class WelcomeServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

       System.out.println ("Starting WelcomeServlet");
        User user = (User) request.getSession().getAttribute("user");

        response.getWriter().append("<html>")
                .append("<head></head><body>")
                .append("<h1>Welcome</h1>");

        if (user == null) {
            response.getWriter()
                    .append("<p>User not logged in</p>")
                    .append("<a href=\"/openid-connect-example-java/login\">OIDC Login</a>");
        } else {

                response.getWriter()
                        .append("<h2>Access token</h2>")
                        .append(String.format("<p>%s</p>", user.getAccessToken()))
                        .append("<h2>Id token</h2>")
                        .append(String.format("<p>%s</p>", user.getIdToken()))
                        .append(prettifyJSON(new JSONObject (user.getIdTokenPayload().toJson())))                      
                       ;
        }

        response.getWriter()
                .append("</body></html");
    }

    private String prettifyJSON(JSONObject json) {
        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<p>{</p>");
        for (String key : json.keySet()) {
            htmlString.append(String.format("<p>&nbsp;&nbsp;&nbsp;&nbsp;<b>%s:</b> %s</p>", key, json.get(key)));
        }
        htmlString.append("<p>}</p>");
        return htmlString.toString();
    }
}