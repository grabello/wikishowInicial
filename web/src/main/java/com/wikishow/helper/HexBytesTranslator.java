package com.wikishow.helper;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class HexBytesTranslator {
    public static String toHex(byte[] cryptedBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : cryptedBytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static byte[] fromHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
