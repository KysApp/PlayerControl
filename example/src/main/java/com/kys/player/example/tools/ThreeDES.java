package com.kys.player.example.tools;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by bsy on 2016/4/12.
 * 3DES加密
 */
public class ThreeDES {
    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish

    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//在单一方面的加密或解密
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //转换成十六进制字符串
    public static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
//            if (n < b.length - 1) hs = hs + ":";
        }
        return hs.toUpperCase();
    }

    public static byte[] hex(String password) {
//        String key = "test";//关键字
//        String f = DigestUtils.md5Hex(username);
//        byte[] bkeys = new String(Hex.encodeHex(DigestUtils.md5(username))).getBytes();

        byte[] bkeys =password.getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < bkeys.length; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }

    public static String  main(String[] args,String password) {
        //添加新安全算法,如果用JCE就要把它添加进去
//        byte[] enk = hex("123456");//用户密码
//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//        String password = "123456";//密码
//        System.out.println("加密前的字符串:" + password);
//        byte[] encoded = encryptMode(enk,password.getBytes());
//        String pword = Base64.encodeToString(encoded,0);
//        System.out.println("加密后的字符串:" + pword);
        Log.e("in", args[0]);
        String out = "";
//        String outtest="";
        for (int i = 0; i < args.length; i++) {
            byte[] enk = hex(password);//用户密码
            byte[] encoded = encryptMode(enk, args[i].getBytes());
            out += byte2Hex(encoded);
//            outtest+=Base64.encodeToString(encoded,0);
        }
        Log.e("out", out);
//        Log.e("outtest", outtest);
        return out;
//        byte[] reqPassword = Base64.decode(pword);
//        byte[] srcBytes = decryptMode(enk,reqPassword);
//        System.out.println("解密后的字符串:" + (new String(srcBytes)));
    }
}
