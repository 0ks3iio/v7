package net.zdsoft.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtils {

    private static final char[] chs = { 'L', 'K', 'J', '4', 'D', 'G', 'F', 'V', 'R', 'T', 'Y', 'B', 'N', 'U', 'P', 'W',
            '3', 'E', '5', 'H', 'M', '7', 'Q', '9', 'S', 'A', 'Z', 'X', '8', 'C', '6', '2' };

    static final String algorithmStr = "AES/ECB/PKCS5Padding";

    static private KeyGenerator keyGen;

    static private Cipher cipher;

    static boolean isInited = false;

    public static void main(String[] args) {
        String s = "tW9wLkY+GuYbtDJbD9Cpz+reHLpR4LxaGxsA6REF7+YfEcprC1TleXtZVERrbel/ySQI4V7XJpgK fy/L1IoyFZsPuXuBMMOYGKIXDyz0uzPnmbr3lxYxAyj/gs2In6tQnGgSbX8aMUvpLCANwUgvkeIH wh7rfi4J7THUtR4REs+9F7caaGCxGpgsVlMQsY15S/6zgdt1QsoNJPsKtAr7LIAJcx8KhWJC36K1 bWxXmov8bZL6gxqwM/R1prP8/ZZnDPGvBcxhw6ijYlg73kHcxJ0NNnluZEjl+hVOqtUV4ywm7eHA RFrA62d6GZpcPlZi2irtsfkMyRAlPfhHoPwZ+YO7s2u8S/HCcknk6N1vw1suLD1Z783SFLSXuSbt 4vDwwUZtQC69jtiaLFOZtYa+R0sXeymOiWo8ncRh4GwOkIbZMpRxFZOWyNgyQ+it27wluFn0qYyN 4wOzsRRPbfZ0dXLdebRBW4veAcup2QZpMsOcaBJtfxoxS+ksIA3BSC+R4gfCHut+LgntMdS1HhES z+1fY5i8exuFUqrHELRAj4VL/rOB23VCyg0k+wq0CvssgAlzHwqFYkLforVtbFeai/xtkvqDGrAz 9HWms/z9lmcM8a8FzGHDqKNiWDveQdzEnQ02eW5kSOX6FU6q1RXjLCbt4cBEWsDrZ3oZmlw+VmLa Ku2x+QzJECU9+Eeg/Bn5g7uza7xL8cJySeTo3W/DWy4sPVnvzdIUtJe5Ju3i8PDBRm1ALr2O2Jos U5m1hr5HnCVN3Wn+Y3DOGUzXJA8oBdkylHEVk5bI2DJD6K3bvCW4WfSpjI3jA7OxFE9t9nR1WTl4 CiK7e/x3DcVEf2Pms5eE1qdqDvDWLs+U4lKnhuU3r97ufC5oyX9CByZ4hL95OTF+acWsu4MIRVfb l+aTZiiR56yJwiBIYXMVYWm9Trz0m2m9gNjdGCTP035MOaJLTa1ri4K96jppxvKbeA728+AyQRAf X8w0K7QPyxEsueOqn388gRkcBiDRQUw4g7SF6TRhbQ6hxHD3UNxRJkyP/wBVA6kXTtqH6yZxscqM lhoG3dp5kHDBPEVchLhqDm3K";
        try {
            System.out.println(new String(decryptAESAndBase64(s.getBytes(), "WinuponTestTicketKey")));
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 初始化
    static private void init() {

        // 初始化keyGen
        try {
            keyGen = KeyGenerator.getInstance("AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);

        // 初始化cipher
        try {
            cipher = Cipher.getInstance(algorithmStr);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        isInited = true;
    }

    public static byte[] GenKey() {
        if (!isInited)// 如果没有初始化过,则初始化
        {
            init();
        }
        return keyGen.generateKey().getEncoded();
    }

    /**
     * 自身混淆加密，最多只能加密 30 个字节长度的字符串。
     * 
     * <p>
     * <b>对同一个字符串，加密后的密文可能是不相同的，所以在判断密码是否相等时，不能采用密文进行比对，必须采用明文比对。</b>
     * </p>
     * 
     * @param source
     *            源字符串
     * @return 加密后字符串
     * @see {@link #decodeBySelf(String)}
     */
    public static String encodeBySelf(String source) {
        if (source == null) {
            throw new NullPointerException("source can't be null");
        }

        if (source.length() > 30) {
            throw new IllegalArgumentException("the length of source must be less than 31, actual was "
                    + source.length());
        }

        String plainText = source;
        byte[] plainTextBytes = plainText.getBytes();

        byte[] encodedBytes1 = new byte[30];
        byte[] encodedBytes2 = new byte[30];

        int n1 = 0, n2 = 0;
        for (int i = 0; i < plainTextBytes.length; i++) {
            if ((i + 1) % 2 != 0) { // 奇数位
                encodedBytes1[n1++] = (byte) get32Hi(plainTextBytes[i] * 4);
                encodedBytes1[n1++] = (byte) get32Low(plainTextBytes[i] * 4);
            }
            else { // 偶数位
                encodedBytes2[n2++] = (byte) get32Hi(plainTextBytes[i] * 4);
                encodedBytes2[n2++] = (byte) get32Low(plainTextBytes[i] * 4);
            }
        }

        while (n1 < 30) {
            encodedBytes1[n1++] = (byte) getRandom(32);
        }

        while (n2 < 30) {
            encodedBytes2[n2++] = (byte) getRandom(32);
        }

        int pos1 = getRandom(plainTextBytes.length);
        int pos2 = getRandom(plainTextBytes.length);
        sort(encodedBytes1, pos1);
        sort(encodedBytes2, pos2);
        int check = (sumSqual(encodedBytes1) + sumSqual(encodedBytes2)) % 32;

        byte[] encodedArray = new byte[64];
        encodedArray[0] = (byte) pos1;
        encodedArray[1] = (byte) pos2;
        System.arraycopy(encodedBytes1, 0, encodedArray, 2, encodedBytes1.length);
        System.arraycopy(encodedBytes2, 0, encodedArray, 2 + encodedBytes1.length, encodedBytes2.length);
        encodedArray[encodedArray.length - 2] = (byte) plainText.length();
        encodedArray[encodedArray.length - 1] = (byte) check;
        byte[] ps = new byte[encodedArray.length];

        for (int i = 0; i < encodedArray.length; i++) {
            ps[i] = (byte) chs[encodedArray[i]];
        }

        return new String(ps);
    }

    /**
     * 自身混淆解密。如果不是合法的加密串（长度不是64个字节），会直接返回原字符串。
     * 
     * @param str
     *            加密的字符串
     * @return 解密后字符串
     * @see {@link #encodeBySelf(String)}
     */
    public static String decodeBySelf(String str) {
        // 如果不是合法的加密串，则直接返回
        if (str == null || str.length() != 64) {
            return str;
        }

        byte[] bb = new byte[str.length()];
        byte[] sb = str.getBytes();

        for (int i = 0; i < sb.length; i++) {
            for (int j = 0; j < 32; j++) {
                if (((byte) chs[j]) == sb[i]) {
                    bb[i] = (byte) j;
                    break;
                }
            }
        }

        int sl = bb[bb.length - 2];
        int p1 = bb[0];
        int p2 = bb[1];

        byte[] bb1 = new byte[30];
        byte[] bb2 = new byte[30];

        int bb2l;
        if (sl % 2 == 0) {
            bb2l = sl;
        }
        else {
            bb2l = sl - 1;
        }

        System.arraycopy(bb, 2, bb1, 0, bb1.length);
        System.arraycopy(bb, 2 + bb1.length, bb2, 0, bb2.length);

        unsort(bb1, p1);
        unsort(bb2, p2);
        byte[] oldb = new byte[sl];
        for (int i = 0; i < sl; i += 2) {
            oldb[i] = (byte) (getIntFrom32(bb1[i], bb1[i + 1]) / 4);
            if (i + 1 < bb2l) {
                oldb[i + 1] = (byte) (getIntFrom32(bb2[i], bb2[i + 1]) / 4);
            }
        }

        return new String(oldb);
    }

    private static int sumSqual(byte[] b) {
        int sum = 0;
        for (int i = 0; i < b.length; i++) {
            sum += (int) Math.pow(b[i], 2);
        }
        return sum;
    }

    private static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    private static void sort(byte[] b, int pos) {
        byte[] tmp = new byte[pos];
        System.arraycopy(b, 0, tmp, 0, pos);
        System.arraycopy(b, pos, b, 0, b.length - pos);
        System.arraycopy(tmp, 0, b, b.length - pos, pos);
    }

    private static void unsort(byte[] b, int pos) {
        byte[] tmp = new byte[pos];
        System.arraycopy(b, b.length - pos, tmp, 0, pos);
        System.arraycopy(b, 0, b, pos, b.length - pos);
        System.arraycopy(tmp, 0, b, 0, pos);
    }

    private static int get32Low(int num) {
        return num % 32;
    }

    private static int get32Hi(int num) {
        return num / 32;
    }

    private static int getIntFrom32(int hi, int low) {
        return hi * 32 + low;
    }

    public static byte[] Encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;

        if (!isInited)// 为初始化
        {
            init();
        }

        Key key = new SecretKeySpec(keyBytes, "AES");

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            encryptedText = cipher.doFinal(content);
        }
        catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encryptedText;
    }

    // 解密为byte[]
    public static byte[] decryptToBytes(byte[] content, byte[] keyBytes) {
        byte[] originBytes = null;
        if (!isInited) {
            init();
        }

        Key key = new SecretKeySpec(keyBytes, "AES");

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        // 解密
        try {
            originBytes = cipher.doFinal(Base64.decodeBase64(content));
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return originBytes;
    }

    public static String decodeBy3DESAndBase64(String str, String key) throws Exception {
        String decoded = null;

        // try {
        byte[] rawkey = key.getBytes();
        DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        SecretKey deskey = keyfactory.generateSecret(keyspec);

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] clearText = cipher.doFinal(Base64.decodeBase64(str.getBytes()));
        decoded = new String(clearText);
        // }
        // catch (Exception e) {
        // throw new RuntimeException("Could not decodeBy3DESAndBase64", e);
        // }

        return decoded;
    }
    
    public static byte[] decodeBase64(byte[] content){
    	return Base64.decodeBase64(content);
    }

    public static byte[] decryptAESAndBase64(byte[] content, String password) throws Exception {
        byte[] bs = Base64.decodeBase64(content);
        return decryptAES(bs, password);
    }

    public static byte[] decryptAES(byte[] content, String password) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        return cipher.doFinal(content);
    }

    public static String encryptAESAndBase64(String content, String password) {
        byte[] bs = encryptAES(content, password);
        return Base64.encodeBase64String(bs);
    }

    public static String encryptAESAndBase64URLSafe(String content, String password) {
        byte[] bs = encryptAES(content, password);
        return Base64.encodeBase64URLSafeString(bs);
    }

    public static byte[] encryptAES(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用MD5对字符串加密.
     *
     * @param str
     *            源字符串
     * @return 加密后字符串
     */
    public static String encodeByMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return StringUtils.toHexString(md.digest());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not encodeByMD5", e);
        }
    }

    /**
     * 使用MD5对字节数组加密.
     *
     * @param bytes
     *            源字符byte数组
     * @return 加密后字符串
     */
    public static String encodeByMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return StringUtils.toHexString(md.digest());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not encodeByMD5", e);
        }
    }
}
