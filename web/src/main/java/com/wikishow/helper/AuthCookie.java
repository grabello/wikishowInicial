package com.wikishow.helper;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 9:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthCookie {
    private Cookie authCookie;
    private BigInteger personId;
    private long timestamp;
    private int sessionTime;

    public AuthCookie(BigInteger personId, long timestamp, int sessionTime) {
        super();
        this.personId = personId;
        this.timestamp = timestamp;
        this.sessionTime = sessionTime;
    }

    public AuthCookie(HttpServletRequest request) {

        this.authCookie = getAuthCookie(request);
        if (authCookie == null) {
            System.err.println("Cookie de autenticacao do usuario nao localizado");
        }

        String encryptedValue = authCookie.getValue();
        String decryptedValue = null;
        try {
            Crypt crypt = Crypt.getInstance();
            decryptedValue = crypt.decrypt(encryptedValue);
        } catch (Exception e) {
            System.err.println("Erro ao descriptar conteudo do cookie " + encryptedValue);
        }

        String[] cookie = StringUtils.split(decryptedValue, "|");
        if (cookie.length != 4) {
            System.err.println("Conteudo do cookie invalido");
        }

        int idx = 0;
        this.personId = BigInteger.valueOf(Long.parseLong(cookie[idx++]));
        this.timestamp = Long.parseLong(cookie[idx++]);
        this.sessionTime = Integer.parseInt(cookie[idx++]);
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        Cookie authCookie = null;
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                String cookieName = cookie.getName();

                if (cookieName == null || !StringUtils.equals(cookieName, CookieUtil.AUTHENTICATION_COOKIE_NAME)) {
                    continue;
                }

                authCookie = cookie;
                break;
            }
        }
        return authCookie;
    }

    public String createHttpOnlyCookie(HttpServletResponse response) {

        StringBuilder decryptedValue = new StringBuilder();
        decryptedValue.append(personId);
        decryptedValue.append("|");
        decryptedValue.append(timestamp);
        decryptedValue.append("|");
        decryptedValue.append(sessionTime);

        String ecryptedValue = null;

        Crypt crypt = Crypt.getInstance();
        ecryptedValue = crypt.encrypt(decryptedValue.toString());

        authCookie = new Cookie(CookieUtil.AUTHENTICATION_COOKIE_NAME, ecryptedValue);
        authCookie.setDomain(CookieUtil.COOKIE_WIKISHOW_DOMAIN);
        authCookie.setMaxAge(sessionTime);
        authCookie.setPath("/");
        return CookieUtil.createHttpOnlyCookie(response, authCookie);
    }

    public String updateCookie(HttpServletResponse resp) {
        if (authCookie != null) {
            this.timestamp = Calendar.getInstance().getTimeInMillis();
            return createHttpOnlyCookie(resp);
        }
        return null;
    }

    public String removeCookie(HttpServletResponse resp) {
        if (authCookie != null) {
            this.sessionTime = 0;
            return createHttpOnlyCookie(resp);
        }
        return null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigInteger getPersonId() {
        return personId;
    }

}
