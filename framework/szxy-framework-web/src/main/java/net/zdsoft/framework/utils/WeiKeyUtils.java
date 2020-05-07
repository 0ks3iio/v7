package net.zdsoft.framework.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class WeiKeyUtils {

    private final static String DES = "DES";// 加密方式

    private final static String CRYPT_KEY = "abcd1234ABCD1234";// 密钥

    private static final char[] chs = { 'L', 'K', 'J', '4', 'D', 'G', 'F', 'V', 'R', 'T', 'Y', 'B',
            'N', 'U', 'P', 'W', '3', 'E', '5', 'H', 'M', '7', 'Q', '9', 'S', 'A', 'Z', 'X', '8',
            'C', '6', '2' };

    /**
     * 验证请求合法性
     * @param key 微课传递的key
     * @param salt 时间戳
     * @param token 令牌
     * @param skey 约定字符串
     * @return
     */
    public static boolean validate(String key, long salt, String token, String skey) {
        String key_ = DigestUtils.md5Hex(token + salt + skey);
        System.out.println(key_);
        if(StringUtils.equals(key, key_))
            return true;
        else
            return false;
    }
    
    /**
     * 自身混淆加密，最多只能加密 30 个字节长度的字符串。
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
            throw new IllegalArgumentException(
                                               "the length of source must be less than 31, actual was "
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
        System.arraycopy(encodedBytes2, 0, encodedArray, 2 + encodedBytes1.length,
                encodedBytes2.length);
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        // System.out.println(encodeBySelf("123456987"));
        // System.out.println(decodeBySelf("GJMVDF8Q88F2578CANPZMWTRYYVFDFNFF3FSVLV978H6L4Q5RWLYRWH5XEWUFRTE"));

        try {
        	long salt = 1422259677890L;
        	validate("ee785e687b6b75e19d02d9210feb2da8", salt, "jBTzanmFCVVKJChhObJRRkpotIh4qdufLbAWc1fdecEtGcSzSu6TSQ==", "weike_eis");
//            String text = "8A85BA394507F7A4014521FC9B9F0096";
//            System.out.println("des加密解密");
//            String encodeStr = encodeByDes(text, CRYPT_KEY);
//            System.out.println(encodeStr);
//            System.out.println(encodeByDes("402880924911735F0149118C663C0009", CRYPT_KEY));
//        	System.out.println(decodeByDes("ibUx+9UWhjgoJZuMljMROG+WqjYbUoWRaPP9GDJQ//stGcSzSu6TSQ=="));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据加密
     */
    public static String encodeByDes(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * 数据解密
     */
    public static String decodeByDes(String data, String key) throws IOException, Exception {
        if (data == null) {
            return null;
        }
        sun.misc.BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    public static String decodeByDes(String data) throws IOException, Exception {
        if (data == null) {
            return null;
        }
        sun.misc.BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, CRYPT_KEY.getBytes());
        return new String(bt);
    }

    /**
     * 加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}
