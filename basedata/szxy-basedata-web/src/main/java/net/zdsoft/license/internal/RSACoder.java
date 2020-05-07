package net.zdsoft.license.internal;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     * 
     *             public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
     *             throws Exception { // 对密钥解密 byte[] keyBytes = Base64.decode(key);
     * 
     *             // 取得私钥 PKCS8EncodedKeySpec pkcs8KeySpec = new
     *             PKCS8EncodedKeySpec(keyBytes); KeyFactory keyFactory =
     *             KeyFactory.getInstance(KEY_ALGORITHM); Key privateKey =
     *             keyFactory.generatePrivate(pkcs8KeySpec);
     * 
     *             // 对数据解密 Cipher cipher =
     *             Cipher.getInstance(keyFactory.getAlgorithm());
     *             cipher.init(Cipher.DECRYPT_MODE, privateKey);
     * 
     *             return cipher.doFinal(data); }
     */

    /**
     * 解密<br>
     * 用公钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedBytes, byte[] key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decode(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // RSA need 128 bytes for output
        int encryptedBytesChunkLength = 128;
        int numberOfEncryptedChunks = encryptedBytes.length / encryptedBytesChunkLength;

        // The limit per chunk is 117 bytes for RSA
        int decryptedBytesChunkLength = 100;
        int decryptedBytesLength = numberOfEncryptedChunks * encryptedBytesChunkLength;
        // It looks like we must create the decrypted file as long as the
        // encrypted since RSA need 128 for output

        // Create the decoded byte array
        byte[] decryptedFileBytes = new byte[decryptedBytesLength];

        // Counters
        int decryptedByteIndex = 0;
        int encryptedByteIndex = 0;

        for (int i = 0; i < numberOfEncryptedChunks; i++) {
            if (i < numberOfEncryptedChunks - 1) {
                decryptedByteIndex = decryptedByteIndex
                        + cipher.doFinal(encryptedBytes, encryptedByteIndex, encryptedBytesChunkLength,
                                decryptedFileBytes, decryptedByteIndex);
                encryptedByteIndex = encryptedByteIndex + encryptedBytesChunkLength;
            } else {
                decryptedByteIndex = decryptedByteIndex
                        + cipher.doFinal(encryptedBytes, encryptedByteIndex,
                                encryptedBytes.length - encryptedByteIndex, decryptedFileBytes, decryptedByteIndex);
            }
        }
        
        byte[] enBytes = new byte[decryptedByteIndex];
        System.arraycopy(decryptedFileBytes, 0, enBytes, 0, decryptedByteIndex);
        
        return enBytes;
    }

    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     * 
     *             public static byte[] encryptByPublicKey(byte[] data, byte[] key)
     *             throws Exception { // 对公钥解密 byte[] keyBytes = Base64.decode(key);
     * 
     *             // 取得公钥 X509EncodedKeySpec x509KeySpec = new
     *             X509EncodedKeySpec(keyBytes); KeyFactory keyFactory =
     *             KeyFactory.getInstance(KEY_ALGORITHM); Key publicKey =
     *             keyFactory.generatePublic(x509KeySpec);
     * 
     *             // 对数据加密 Cipher cipher =
     *             Cipher.getInstance(keyFactory.getAlgorithm());
     *             cipher.init(Cipher.ENCRYPT_MODE, publicKey);
     * 
     *             return cipher.doFinal(data); }
     */

    /**
     * 加密<br>
     * 用私钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] decryptedBytes, byte[] key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decode(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // The limit per chunk is 117 bytes for RSA
        int decryptedBytesChunkLength = 100;
        int numberenOfDecryptedChunks = (decryptedBytes.length - 1) / decryptedBytesChunkLength + 1;

        // RSA need 128 bytes for output
        int encryptedBytesChunkLength = 128;
        int encryptedBytesLength = numberenOfDecryptedChunks * encryptedBytesChunkLength;

        // Create the encoded byte array
        byte[] encryptedBytes = new byte[encryptedBytesLength];

        // Counters
        int decryptedByteIndex = 0;
        int encryptedByteIndex = 0;

        for (int i = 0; i < numberenOfDecryptedChunks; i++) {
            if (i < numberenOfDecryptedChunks - 1) {
                encryptedByteIndex = encryptedByteIndex
                        + cipher.doFinal(decryptedBytes, decryptedByteIndex, decryptedBytesChunkLength, encryptedBytes,
                                encryptedByteIndex);
                decryptedByteIndex = decryptedByteIndex + decryptedBytesChunkLength;
            } else {
                encryptedByteIndex = encryptedByteIndex
                        + cipher.doFinal(decryptedBytes, decryptedByteIndex,
                                decryptedBytes.length - decryptedByteIndex, encryptedBytes, encryptedByteIndex);
            }
        }
        return encryptedBytes;
    }
}
