package com.muhanbit.sycrethealth;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hwjoo on 2017-01-13.
 */
/*
 * 서버 통신할 경우, json 암호화에 사용된다.
 *  ex) { "request" : "Encrypt.encrypt로 암호화된 json"}
 *  서버에서 request를 get하여, 암호화된 data를 복호화한다.
 *  복호화된 data는 json형태이다. 따라서
 *   ex ) {"request" : " {"id":"1","step":"100"} " } 이런 형태가 된다.
 */
public class Encrypt {
    public Encrypt() {
    }

    public static String encrypt(boolean encrypt, String plainText, String keyBase64) {
        byte[] bIV = new byte[16];
        byte[] key = Base64.decode(keyBase64, 2);
        if(encrypt) {
            try {
                Cipher encByte = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encByte.init(1, new SecretKeySpec(key, "AES"), new IvParameterSpec(bIV));
                byte[] e = encByte.doFinal(plainText.getBytes());
                return new String(Base64.encode(e, 2));
            } catch (NoSuchAlgorithmException var8) {
                var8.printStackTrace();
            } catch (NoSuchPaddingException var9) {
                var9.printStackTrace();
            } catch (InvalidKeyException var10) {
                var10.printStackTrace();
            } catch (InvalidAlgorithmParameterException var11) {
                var11.printStackTrace();
            } catch (IllegalBlockSizeException var12) {
                var12.printStackTrace();
            } catch (BadPaddingException var13) {
                var13.printStackTrace();
            }
        } else {
            byte[] encByte1 = Base64.decode(plainText, 2);

            try {
                Cipher e1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
                e1.init(2, new SecretKeySpec(key, "AES"), new IvParameterSpec(bIV));
                byte[] dec = e1.doFinal(encByte1);
                return new String(dec);
            } catch (NoSuchAlgorithmException var14) {
                var14.printStackTrace();
            } catch (NoSuchPaddingException var15) {
                var15.printStackTrace();
            } catch (InvalidKeyException var16) {
                var16.printStackTrace();
            } catch (InvalidAlgorithmParameterException var17) {
                var17.printStackTrace();
            } catch (IllegalBlockSizeException var18) {
                var18.printStackTrace();
            } catch (BadPaddingException var19) {
                var19.printStackTrace();
            }
        }

        return null;
    }

    public static String encrypt(boolean encrypt, String plainText, byte[] key) {
        byte[] bIV = new byte[8];
        if(encrypt) {
            try {
                Cipher encByte = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encByte.init(1, new SecretKeySpec(key, "AES"), new IvParameterSpec(bIV));
                byte[] e = encByte.doFinal(plainText.getBytes());
                return new String(Base64.encode(e, 2));
            } catch (NoSuchAlgorithmException var7) {
                var7.printStackTrace();
            } catch (NoSuchPaddingException var8) {
                var8.printStackTrace();
            } catch (InvalidKeyException var9) {
                var9.printStackTrace();
            } catch (InvalidAlgorithmParameterException var10) {
                var10.printStackTrace();
            } catch (IllegalBlockSizeException var11) {
                var11.printStackTrace();
            } catch (BadPaddingException var12) {
                var12.printStackTrace();
            }
        } else {
            byte[] encByte1 = Base64.decode(plainText, 2);

            try {
                Cipher e1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
                e1.init(2, new SecretKeySpec(key, "AES"), new IvParameterSpec(bIV));
                byte[] dec = e1.doFinal(encByte1);
                return new String(dec);
            } catch (NoSuchAlgorithmException var13) {
                var13.printStackTrace();
            } catch (NoSuchPaddingException var14) {
                var14.printStackTrace();
            } catch (InvalidKeyException var15) {
                var15.printStackTrace();
            } catch (InvalidAlgorithmParameterException var16) {
                var16.printStackTrace();
            } catch (IllegalBlockSizeException var17) {
                var17.printStackTrace();
            } catch (BadPaddingException var18) {
                var18.printStackTrace();
            }
        }

        return null;
    }
}
