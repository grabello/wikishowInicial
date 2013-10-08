package com.wikishow.controller;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/14/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class FacebookLoginController {

    private static final String SCOPE = "email,offline_access,user_about_me,user_birthday,read_friendlists";
    private static final String REDIRECT_URI = "http://wikishow.herokuapp.com/facebook-callback";
    private static final String CLIENT_ID = "245900605557134";
    private static final String APP_SECRET = "c0409e0636d7dd2edb6bb4818757f4e1";
    private static final String DIALOG_OAUTH = "https://www.facebook.com/dialog/oauth";
    private static final String ACCESS_TOKEN = "https://graph.facebook.com/oauth/access_token";

    @RequestMapping(value = "/facebook-signin", method = RequestMethod.GET)
    public String signin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            //TODO: if already have a valid access token, no need to redirect, just login
            response.sendRedirect(DIALOG_OAUTH + "?client_id=" + CLIENT_ID +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&scope=" + SCOPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @RequestMapping(value = "/facebook-callback", params = "code", method = RequestMethod.GET)
    public String accessCode(@RequestParam("code") String code,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String url = ACCESS_TOKEN + "?client_id=" + CLIENT_ID + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&code=" + code + "&client_secret=" + APP_SECRET;

        String token = null;
        try {
            URL u = new URL(url);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }
            in.close();
            token = b.toString();
            if (token.startsWith("{"))
                throw new Exception("error on requesting token: " + token + " with code: " + code);
        } catch (Exception e) {
            // an error occurred, handle this
        }

        String graph = null;
        try {
            String g = "https://graph.facebook.com/me?" + token;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");
            in.close();
            graph = b.toString();
        } catch (Exception e) {
            // an error occurred, handle this
        }
        JSONObject jsonObject = new JSONObject(graph);

        request.setAttribute("json", jsonObject.toString());
        request.setAttribute("code", token);

        return "loggedin";
    }

    @RequestMapping(value = "/facebook-callback", params = "error_reason", method = RequestMethod.GET)
    @ResponseBody
    public void error(@RequestParam("error_reason") String errorReason,
                      @RequestParam("error") String error,
                      @RequestParam("error_description") String description,
                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, description);
            System.out.println(errorReason);
            System.out.println(error);
            System.out.println(description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
