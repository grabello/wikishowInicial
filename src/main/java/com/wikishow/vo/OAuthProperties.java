package com.wikishow.vo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 <span id="signinButton">
 <span
 class="g-signin"
 data-callback="signinCallback"
 data-clientid="950592796361.apps.googleusercontent.com"
 data-cookiepolicy="single_host_origin"
 data-requestvisibleactions="http://schemas.google.com/AddActivity"
 data-scope="https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar">
 </span>
 </span>
 */

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/10/13
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OAuthProperties {

    public static final String DEFAULT_OAUTH_PROPERTIES_FILE_NAME = "oauth.properties";

    /**
     * The OAuth 2.0 Client ID
     */
    private String clientId;

    /**
     * The OAuth 2.0 Client Secret
     */
    private String clientSecret;

    /**
     * The Google APIs scopes to access
     */
    private String scopes;

    /**
     * Instantiates a new OauthProperties object reading its values from the
     * {@code OAUTH_PROPERTIES_FILE_NAME} properties file.
     *
     * @throws IOException                    IF there is an issue reading the {@code propertiesFile}
     * @throws OauthPropertiesFormatException If the given {@code propertiesFile}
     *                                        is not of the right format (does not contains the keys {@code
     *                                        clientId}, {@code clientSecret} and {@code scopes})
     */
    public OAuthProperties() throws IOException {
        this(OAuthProperties.class.getResourceAsStream(DEFAULT_OAUTH_PROPERTIES_FILE_NAME));
    }

    /**
     * Instantiates a new OauthProperties object reading its values from the given
     * properties file.
     *
     * @param propertiesFile the InputStream to read an OAuth Properties file. The
     *                       file should contain the keys {@code clientId}, {@code
     *                       clientSecret} and {@code scopes}
     * @throws IOException                    IF there is an issue reading the {@code propertiesFile}
     * @throws OAuthPropertiesFormatException If the given {@code propertiesFile}
     *                                        is not of the right format (does not contains the keys {@code
     *                                        clientId}, {@code clientSecret} and {@code scopes})
     */
    public OAuthProperties(InputStream propertiesFile) throws IOException {
        //Properties oauthProperties = new Properties();
        //oauthProperties.load(propertiesFile);
        clientId = "950592796361.apps.googleusercontent.com";
        //clientId = oauthProperties.getProperty("clientId");
        clientSecret = "wUZIEDfJ5YfMeTPIyBBKVFWr";
        //clientSecret = oauthProperties.getProperty("clientSecret");
        scopes = "https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar";
        //oauthProperties.getProperty("scopes");
        if ((clientId == null) || (clientSecret == null) || (scopes == null)) {
            throw new OAuthPropertiesFormatException();
        }
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @return the scopes
     */
    public String getScopesAsString() {
        return scopes;
    }

    /**
     * Thrown when the OAuth properties file was not at the right format, i.e not
     * having the right properties names.
     */
    @SuppressWarnings("serial")
    public class OAuthPropertiesFormatException extends RuntimeException {
    }

}
