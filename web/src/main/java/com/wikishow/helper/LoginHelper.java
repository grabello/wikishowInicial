package com.wikishow.helper;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/15/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginHelper {

    public static JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            JSONObject json = new JSONObject(readFromUrl(url));
            return json;
        } catch (JSONException e) {
            System.out.println("***********ERRO**********");
            return null;
        } finally {
            is.close();
        }
    }

    public static String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String text = readAll(rd);
            return text;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        } else {
            // http://en.wikipedia.org/wiki/X-Forwarded-For
            if (ipAddress.indexOf(",") > 0) {
                String[] ipAddresses = ipAddress.split(",");
                ipAddress = ipAddresses[0].trim();
            }
        }

        if (ipAddress == null) {
            System.err.println("Ip not found correctly: " + ipAddress);
            System.err.println("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
            System.err.println("getRemoteAddr: " + request.getRemoteAddr());
            System.err.println("Ip is not valid: " + ipAddress + " - " + request.getHeader("X-FORWARDED-FOR")
                    + " - " + request.getRemoteAddr());
        }

        return ipAddress;
    }

}
