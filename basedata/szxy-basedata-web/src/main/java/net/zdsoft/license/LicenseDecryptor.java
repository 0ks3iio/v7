package net.zdsoft.license;

import net.zdsoft.license.entity.LicenseInfo;
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

        LicenseInfo info = decode("N1urE0CORo7769Isf54FaONblnE52HXrBORqfS2Xl1I2xUiUv1gLlvpJNRHHIoX4GHeRfUAfenTmQzYIc+MZqrlkHrlxN2dvOiuq8q7oUB5mZ7IU8PLI/7f3CeIm5W4at6gfNBozHYgWIFe9L9kgdZjhAqzp2HyszXXWCADw/XpTftmIOgThJMYm75FWuuvMW/HGlAQ5L0lcohcFPKN5LBgH18WQNLfb+egzUlN2mVHUNQ1r25HZQY4kX9tuvjQbX9SLaUsEX+i4eRrNeBhvk5wn4uJ+P+D7fTg6CQeoOws0f9A9F9NGb1yCgw48HOoc3yp26k7nIj3y41EEcLFCnh30s0rgRsNT7L3c9YVAWmgjMBMnbbRqBruiy3XeCV0oPub/pBPMAMLXUVfe+Zo2zVHc8nCW3Kj1/ft7u06SNUOQKDlA96oNI5nYATaj9QlD0dRFHh6nCHGH4Yexin3nLkrWyGvnEA/7ibg+1XTpRLBB0wH/n28LtarKIpZmzZx1eM8t/2AGsIHMSZwYBJDILg6tS4RowSzp6IOAQijEs8FQF6zl11Wq2K1i2E4jR2yw2ddzeE/+Qdvlq7pWy4Pk860+up9CRThEi4APBkMX7pUUmiIEU+MjQ9nyI4OYuMsTO6Vpzq9PRo0XhahrQ9I879Ft/AC+jr4wDQ5dEzBkjGicMU2eSb6F3Em0Wz0VO4TgeeovjYglWbZG5BYH1OFV9JrwQEBU42lT+29HeSpndxJMd5DzQbWqeGK4yUPilP+gKf20Tz7nbY1GI9WnWc+spodIB3GAncF41TaWRxvXElZjKmcSf8SCukGGJlMgjDCsd99i3lnN0jkXlAIVLcj+QlQfTfwWvedDL6QQ4LLdrkasMkec8yuweosvFu4Jx1wRpNlbwu2IoXvw87OYLDTA1KVQXuSybCJbZKjaRMLeyTFNa6M3+MejCkJ6DXH05kRN8S6IWfg+IlHKoQbLylAUEMTR1bZBzS5HYjZMXHaFN8nQeDZgwJ1PRXH3L9jebqwmKkyaQx1f3iBSwdC0TlWu8GqPL7x/qiTxm0C0sLQTMlLu67x4CMMbsVQX+x+hN+2L+geAZRwHWj6olR+ruvhm7IPD0CTBC5BJ5IePsF9ckklaP82kazb1leFawLj83Bjx1afS7tl0Ow0zq9JIHngPjwefwUScYZcZM26f9ehGN3Q=");
        System.out.println(Arrays.toString(info.getAvailableSubsystems().toArray(new String[0])));
    }
}
