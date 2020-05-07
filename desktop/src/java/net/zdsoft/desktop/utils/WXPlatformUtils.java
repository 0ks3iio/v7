package net.zdsoft.desktop.utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.zdsoft.desktop.constant.WenXunConstant;

public class WXPlatformUtils
{
  public static String decrypt(String info, String key)
    throws Exception
  {
    if (key == null) {
      throw new Exception("key 不能为空");
    }
    byte[] keyb = Arrays.copyOf(key.getBytes("UTF-8"), 16);
    
    SecretKeySpec sKeySpec = new SecretKeySpec(keyb, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    byte[] miwenBytes = hex2bytes(info);
    cipher.init(2, sKeySpec);
    byte[] bjiemihou = cipher.doFinal(miwenBytes);
    String result = new String(bjiemihou, "UTF-8");
    return result;
  }
  
  public static String encrypt(String info, String key)
    throws Exception
  {
    if (key == null) {
      throw new Exception("key 不能为空");
    }
    info = new String(info.getBytes("UTF-8"), "UTF-8");
    byte[] keyb = Arrays.copyOf(key.getBytes("UTF-8"), 16);
    
    SecretKeySpec sKeySpec = new SecretKeySpec(keyb, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(1, sKeySpec);
    byte[] miwen = cipher.doFinal(info.getBytes("UTF-8"));
    return bytes2Hex(miwen);
  }
  
  private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', 
    '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  
  private static final String bytes2Hex(byte[] bytes)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < bytes.length; i++)
    {
      buf.append(bcdLookup[(bytes[i] >>> 4 & 0xF)]);
      buf.append(bcdLookup[(bytes[i] & 0xF)]);
    }
    return buf.toString();
  }
  
  private static final byte[] hex2bytes(String s)
  {
    byte[] bytes = new byte[s.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = ((byte)Integer.parseInt(s.substring(2 * i, 2 * i + 2), 
        16));
    }
    return bytes;
  }
  
  public static void main(String[] arg)
    throws Exception
  {
    String n = encrypt("cc001", WenXunConstant.WENXUN_APP_KEY_VALUE);
    System.out.println(n);
    
  }
}

