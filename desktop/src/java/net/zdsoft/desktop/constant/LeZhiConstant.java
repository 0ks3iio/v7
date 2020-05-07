package net.zdsoft.desktop.constant;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class LeZhiConstant {
    public static final String LEZHI_ENCRYPT_KEY = "jiaoyuyundandianbanbanto";
    public static final String ROLE_STUDENT = "0";
    public static final String ROLE_TEACHER = "2";
    public static final String ROLE_SCHADMIN = "3";
    public static final String ROLE_ADMIN = "4";
    // 定义 加密算法
    private static final String Algorithm = "DESede";

    /**
     * keybyte为加密密钥，长度为24字节
     * src为被加密的数据缓冲区（源）
     * @param keybyte
     * @param src
     * @return
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * keybyte为加密密钥，长度为24字节
     * src为加密后的缓冲区
     * @param keybyte
     * @param src
     * @return
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 转换成十六进制字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = (Integer.toHexString(b[i] & 0XFF));
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
            // if (i < b.length - 1)
            // hs += ":";
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2Byte(String str) {
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer
                        .decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 将16进制字符转换为10进制整数
     * @param c
     * @return
     */
    public static int char2Int(char c) {
        int rs = 0;
        switch (c) {
            case '0':
                rs = 0;
                break;
            case '1':
                rs = 1;
                break;
            case '2':
                rs = 2;
                break;
            case '3':
                rs = 3;
                break;
            case '4':
                rs = 4;
                break;
            case '5':
                rs = 5;
                break;
            case '6':
                rs = 6;
                break;
            case '7':
                rs = 7;
                break;
            case '8':
                rs = 8;
                break;
            case '9':
                rs = 9;
                break;
            case 'a':
                rs = 10;
                break;
            case 'b':
                rs = 11;
                break;
            case 'c':
                rs = 12;
                break;
            case 'd':
                rs = 13;
                break;
            case 'e':
                rs = 14;
                break;
            case 'f':
                rs = 15;
                break;
            case 'A':
                rs = 10;
                break;
            case 'B':
                rs = 11;
                break;
            case 'C':
                rs = 12;
                break;
            case 'D':
                rs = 13;
                break;
            case 'E':
                rs = 14;
                break;
            case 'F':
                rs = 15;
                break;
        }
        return rs;
    }
}
