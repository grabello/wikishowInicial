package com.wikishow.helper;

import org.json.JSONException;
import org.json.JSONObject;

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
}
