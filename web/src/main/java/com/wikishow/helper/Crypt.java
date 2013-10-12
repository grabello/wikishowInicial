package com.wikishow.helper;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Crypt {
//    private String PROVIDER = CryptographyTool.SYMETRIC_PROVIDER;

    private static String ALGORITHM = "AES";
    private static String MODE = "CBC";
    private static String PADDING = "PKCS5Padding";
    private static Crypt instance;
    private String publicKey;
    private String param;

    /**
     * Instantiates a new crypt.
     */
    private Crypt() {
        this.publicKey = "552AAAAAD0A76DBF0D52A5C56A25D4AC";
        this.param = "D72AA92AD0A76DBF6D52A5C56A2554AC";
    }

    /**
     * Gets the single instance of Crypt.
     *
     * @return single instance of Crypt
     */
    public static Crypt getInstance() {
        if (instance == null) {
            instance = new Crypt();
        }
        return instance;
    }

    /**
     * Gets the cipher string.
     *
     * @param cipher  the cipher
     * @param mode    the mode
     * @param padding the padding
     * @return the cipher string
     */
    private static String getCipherString(String cipher, String mode, String padding) {
        return cipher + "/" + mode + "/" + padding;
    }

    /**
     * Gets the symmetric param.
     *
     * @param param the param
     * @return the symmetric param
     */
    private static final AlgorithmParameters getSymmetricParam(String param) {
        try {
            AlgorithmParameters parameters = AlgorithmParameters.getInstance(ALGORITHM);
            parameters.init(HexBytesTranslator.fromHex(param));
            return parameters;
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Gets the symmetric key.
     *
     * @param key the key
     * @return the symmetric key
     */
    private static final Key getSymmetricKey(String key) {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(HexBytesTranslator.fromHex(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            return secretKey;
        } catch (InvalidKeyException e) {
        } catch (InvalidKeySpecException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String encrypt(String plainString) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(HexBytesTranslator.fromHex(publicKey), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            final String encryptedString = HexBytesTranslator.toHex(cipher.doFinal(plainString.getBytes()));
//            return encryptedString;
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(plainString.getBytes()));
            return encryptedString;
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;

//        Key secretKey = getSymmetricKey(publicKey);
//        AlgorithmParameters params = getSymmetricParam(param);
//        byte[] cryptedBytes = encryptSymmetric(plainString.getBytes(), secretKey, params);
//        return HexBytesTranslator.toHex(cryptedBytes);
    }

    public byte[] encrypt(byte[] plainBytes) {
        Key secretKey = getSymmetricKey(publicKey);
        AlgorithmParameters parameters = getSymmetricParam(param);
        return encryptSymmetric(plainBytes, secretKey, parameters);
    }

    public String decrypt(String s) {
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(HexBytesTranslator.fromHex(publicKey), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(s)));
            return decryptedString;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
//        byte[] encryptedData = HexBytesTranslator.fromHex(s);
//        Key secretKey = getSymmetricKey(publicKey);
//        AlgorithmParameters parameters = getSymmetricParam(param);
//        byte[] decryptedBytes = decryptSymmetric(encryptedData, secretKey, parameters);
//        return new String(decryptedBytes);
    }

    public byte[] decrypt(byte[] encryptedBytes) {
        Key secretKey = getSymmetricKey(publicKey);
        AlgorithmParameters parameters = getSymmetricParam(param);
        return decryptSymmetric(encryptedBytes, secretKey, parameters);
    }

    public String getTransformation() {
        return ALGORITHM + "/" + MODE + "/" + PADDING;
    }

    public String getALGORITHM() {
        return ALGORITHM;
    }

    public void setALGORITHM(String algorithm) {
        ALGORITHM = algorithm;
    }

    public String getMODE() {
        return MODE;
    }

//    public String getPROVIDER() {
//        return PROVIDER;
//    }
//
//    public void setPROVIDER(String provider) {
//        PROVIDER = provider;
//    }

    public void setMODE(String mode) {
        MODE = mode;
    }

    public String getPADDING() {
        return PADDING;
    }

    public void setPADDING(String padding) {
        PADDING = padding;
    }

    /**
     * Encrypt symmetric.
     *
     * @param encryptedData the encrypted data
     * @param secretKey     the secret key
     * @param params        the params
     * @return the byte[]
     */
    private byte[] encryptSymmetric(byte[] encryptedData, Key secretKey, AlgorithmParameters params) {
        try {
            String transformation = getCipherString(ALGORITHM, MODE, PADDING);
            Cipher synchCipher = Cipher.getInstance(transformation);
            synchCipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
            return synchCipher.doFinal(encryptedData);
        } catch (InvalidAlgorithmParameterException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        }
        return null;
    }

    /**
     * Decrypt symmetric.
     *
     * @param encryptedData the encrypted data
     * @param secretKey     the secret key
     * @param paramData     the param data
     * @return the byte[]
     */
    public byte[] decryptSymmetric(byte[] encryptedData, Key secretKey, AlgorithmParameters paramData) {
        try {
            //AES/CBC/PKCS5Padding
            String transformation = getCipherString(ALGORITHM, MODE, PADDING);
            Cipher synchCipher = Cipher.getInstance(transformation);
            synchCipher.init(Cipher.DECRYPT_MODE, secretKey, paramData);
            return synchCipher.doFinal(encryptedData);
        } catch (InvalidAlgorithmParameterException e) {
        } catch (InvalidKeyException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (NoSuchPaddingException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

}


