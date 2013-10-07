package com.wikishow.controller;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.wikishow.helper.AuthCookie;
import com.wikishow.helper.LoginHelper;
import com.wikishow.service.LoginService;
import com.wikishow.vo.OAuthProperties;
import com.wikishow.vo.PersonVO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/10/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GoogleLoginController {

    /**
     * The name of the Oauth code URL parameter
     */
    public static final String CODE_URL_PARAM_NAME = "code";
    /**
     * The name of the OAuth error URL parameter
     */
    public static final String ERROR_URL_PARAM_NAME = "error";
    /**
     * The URL suffix of the servlet
     */
    public static final String URL_MAPPING = "/google-callback";
    /**
     * The URL to redirect the user to after handling the callback. Consider
     * saving this in a cookie before redirecting users to the Google
     * authorization URL if you have multiple possible URL to redirect people to.
     */
    public static final String REDIRECT_URL = "loggedin.jsp";
    @Autowired
    LoginService loginService;

    /**
     * Construct the OAuth code callback handler URL.
     *
     * @param req the HttpRequest object
     * @return The constructed request's URL
     */
    public static String getOAuthCodeCallbackHandlerUrl(HttpServletRequest req) {
        String scheme = req.getScheme() + "://";
        String serverName = req.getServerName();
        String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = URL_MAPPING;
        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
        return scheme + serverName + serverPort + contextPath + servletPath + pathInfo;
    }

    /**
     * Construct the request's URL without the parameter part.
     *
     * @param req the HttpRequest object
     * @return The constructed request's URL
     */
    public static String getFullRequestUrl(HttpServletRequest req) {
        String scheme = req.getScheme() + "://";
        String serverName = req.getServerName();
        String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
        String queryString = (req.getQueryString() == null) ? "" : "?" + req.getQueryString();
        return scheme + serverName + serverPort + contextPath + servletPath + pathInfo + queryString;
    }

    @RequestMapping(value = "/google-signin")
    public String startSignin(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Checking if we already have tokens for this user in store
        AccessTokenResponse accessTokenResponse = null;//oauthTokenDao.getKeys(user.getEmail());

        // If we don't have tokens for this user
        if (accessTokenResponse == null) {
            OAuthProperties oauthProperties = new OAuthProperties();
            // Redirect to the Google OAuth 2.0 authorization endpoint
            resp.sendRedirect(new GoogleAuthorizationRequestUrl(oauthProperties.getClientId(),
                    getOAuthCodeCallbackHandlerUrl(req), oauthProperties
                    .getScopesAsString()).build());
            return "index";
        }
        return "index";
    }

    @RequestMapping(value = "/google-callback")
    public String callBack(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Getting the "error" URL parameter
        String[] error = req.getParameterValues(ERROR_URL_PARAM_NAME);

        // Checking if there was an error such as the user denied access
        if (error != null && error.length > 0) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "There was an error: \"" + error[0] + "\".");
            return "error";
        }
        // Getting the "code" URL parameter
        String[] code = req.getParameterValues(CODE_URL_PARAM_NAME);

        // Checking conditions on the "code" URL parameter
        if (code == null || code.length == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The \"code\" URL parameter is missing");
            return "error";
        }

        // Construct incoming request URL
        String requestUrl = getOAuthCodeCallbackHandlerUrl(req);

        // Exchange the code for OAuth tokens
        AccessTokenResponse accessTokenResponse = exchangeCodeForAccessAndRefreshTokens(code[0],
                requestUrl);
        System.out.println("refreshToken = " + accessTokenResponse.refreshToken);

        JSONObject jsonObject = LoginHelper.readJsonFromUrl("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessTokenResponse.accessToken);
        try {
            req.setAttribute("json", jsonObject.get("email"));
        } catch (JSONException e) {
            return "index";
        }

        PersonVO personVO = null;
        try {
            personVO = loginService.saveLogin((String) jsonObject.get("email"), accessTokenResponse.refreshToken, accessTokenResponse.accessToken, 'G');
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (personVO != null) {
            AuthCookie authCookie = new AuthCookie(personVO.getId(), new Date().getTime(), accessTokenResponse.expiresIn.intValue());
            authCookie.createHttpOnlyCookie(resp);
        }

//        try {
//            Cookie wikiCookie = new Cookie("com/wikishow", jsonObject.getString("email"));
//            resp.addCookie(wikiCookie);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return "loggedin";

    }

    /**
     * Exchanges the given code for an exchange and a refresh token.
     *
     * @param code       The code gotten back from the authorization service
     * @param currentUrl The URL of the callback
     * @return The object containing both an access and refresh token
     * @throws IOException
     */
    public AccessTokenResponse exchangeCodeForAccessAndRefreshTokens(String code, String currentUrl)
            throws IOException {

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        // Loading the oauth config file
        OAuthProperties oauthProperties = new OAuthProperties();

        return new GoogleAuthorizationCodeGrant(httpTransport, jsonFactory, oauthProperties
                .getClientId(), oauthProperties.getClientSecret(), code, currentUrl).execute();
    }

}



