package com.wikishow.helper;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class HexBytesTranslator {

    /**
     * Constructor for HexBytesTranslator.
     */
    private HexBytesTranslator() {
        super();
    }

    /**
     * To hex.
     *
     * @param seq
     *            the seq
     *
     * @return the string
     */
    public static String toHex(byte[] seq) {
        StringBuffer buffer = new StringBuffer(seq.length * 2);
        int i, j;

        for (i = 0; i < seq.length; i++) {
            j = seq[i];
            if (j < 0) {
                j += 256;
            }
            buffer.append((j < 16 ? "0" : "") + Integer.toHexString(j));
        }

        return buffer.toString();

    }

    /**
     * From hex.
     *
     * @param seq
     *            the seq
     *
     * @return the byte[]
     */
    public static byte[] fromHex(String seq) {
        byte[] buffer = new byte[seq.length() / 2];
        String hexNumber;

        for (int i = 0; i < seq.length(); i += 2) {
            hexNumber = seq.substring(i, i + 2).toUpperCase();
            buffer[i / 2] = (byte) Integer.parseInt(hexNumber, 16);
        }
        return buffer;
    }
}
