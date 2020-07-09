package com.java.scm.util;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author hujunhui
  */
public class DesUtil {     
    @SuppressWarnings("unused")
	private byte[] desKey;
    private static final String KEY = "DES/CBC/PKCS5Padding";
    private static final String UTF8 = "UTF-8";
    private static final String DES = "DES";


    /**
     * 数据解密
      * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
              
            byte[] byteSrc =convertHexString(message);
            Cipher cipher = Cipher.getInstance(KEY);
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(UTF8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);        
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(UTF8));
            
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv); 
            
            byte[] retByte = cipher.doFinal(byteSrc);
            return new String(retByte,UTF8);
    }

    public static byte[] encrypt(String message, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(KEY);
    
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(UTF8));
    
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);     
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(UTF8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);     
    
        return cipher.doFinal(message.getBytes(UTF8));
    }     
         
    public static byte[] convertHexString(String ss){
        byte[] digest = new byte[ss.length() / 2];
        for(int i = 0; i < digest.length; i++)
        {
        String byteString = ss.substring(2 * i, 2 * i + 2);
        int byteValue = Integer.parseInt(byteString, 16);
        digest[i] = (byte)byteValue;
        }

        return digest;
    }      
         
    public  static String toHexString(byte[] b){
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < b.length; i++) {     
            String plainText = Integer.toHexString(0xff & b[i]);     
            if (plainText.length() < 2){
                plainText = "0" + plainText;
            }
            hexString.append(plainText);     
        }     
             
        return hexString.toString();     
    }

    public static void main(String[] args) throws Exception {
        String msg = "张三123";
        String af = toHexString(encrypt(msg,"12345678"));
        System.out.println(af);
        String af1 = decrypt(af,"12345678");
        System.out.println(af1);
    }
}    

