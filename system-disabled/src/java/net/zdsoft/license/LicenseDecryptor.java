package net.zdsoft.license;

import net.zdsoft.license.exception.InvalidLicenseException;
import net.zdsoft.license.internal.Base64;
import net.zdsoft.license.internal.RSACoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


/**
 * @author zhangza
 * @date 2011-5-24
 */
public class LicenseDecryptor {

    private static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChs/yBJZLLapZrfzS9CEGg6opaeF3pupc0dq2I+YiqkI8i87"
            + "MgA0Cini6V3RLn/AjDXwrSHhfFKBfaBLfcHuqF+NDTFKuzOu1xnj+1WR5JYPaVPiC2Fbt2PxsWb9/H5ArEhvYLQzIHIlVL"
            + "8MimzV7g3RhpQm/woyu7OWDe3MBd9QIDAQAB";

    public static LicenseInfo decode(String data) throws InvalidLicenseException {
        try {
            byte[] deData = RSACoder.decryptByPublicKey(Base64.decode(data.getBytes()), pubKey.getBytes());
            //StringReader sr=new StringReader(new String(deData));
            ByteArrayInputStream sr = new ByteArrayInputStream(deData);
            Properties prop = new Properties();
            prop.load(sr);
            LicenseInfo info = LicenseInfo.fromProperties(prop);
            return info;
        } catch (InvalidLicenseException e) {
            throw e;
        } catch (IOException e) {
            throw new InvalidLicenseException("序列号解析错误,原因为:"+e.getMessage());
        } catch (Exception e) {
            throw new InvalidLicenseException("序列号解析错误,原因为:"+e.getMessage());
        }
    }

    public static LicenseInfo decodeFromFile(String path) throws InvalidLicenseException {

        File file = new File(path);
        if (!file.exists()) {
            throw new InvalidLicenseException("文件不存在");
        }
        try {
            byte[] buffer = new byte[(int)file.length()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                if ( fis.read(buffer) == -1 ) {
                    throw new IOException("EOF reached while trying to read the whole file");
                }        
            } finally { 
                try { if ( fis != null ) fis.close(); } catch ( IOException e) { }
            }
            return decode(new String(buffer));
        } catch (Exception e) {
            throw new InvalidLicenseException("非法License文件,错误原因:" + e.getMessage());
        }
    }

    public static void main(String[] args) throws InvalidLicenseException {

        LicenseInfo info = decode("D/J+Nn4IzDMI9ML4B4qN3P4vA6VJpb1VMn0vT3xtrkkr428MFjQZ5fegL7NTmMcdc1GKlDoAv/X1/pM7SMzcvjgwLzm+rHCTlZfywvQsrixRtGy35VTWE4oV+Ba5O7kzKn5t9+IDK8XZL5H0Mq89OQ40LNwBz9hilbH6iiOEfMuRt+NnAd0egbKDPDdiod/HwB+5fDJJjCtk10dTAhN7BGiwQJ0QEUfjYDwRW5FRnPJzgJ3GfvcgxEaVnY8bOcgEdBr2yMU553/BTllNt7WeixOU144T87+OQrSjSdHthVPlwYK/jiF6eNNSwFWhDP8RZfk77sWBlSECG+Xi5aJG1kkQc8CcW2gzQw70e9LIT97JPQcej2D3usEqvrROvBb23yc4Iu5qNIXixBAiPwILBPMQ3uDPZHecM7H6VTEeEzBev6R4lParhqTXPY30t0WiaEI5Z7U+1z79/oFtUWpn+KEv9uQ4sUwToz+AuLfJ5RMz41I1jHj75fF5XdJpDxAI");
        System.out.println(Arrays.toString(info.getAvailableSubsystems().toArray(new String[0])));
    }
}
