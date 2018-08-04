package io.github.whimthen.script.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class EncryptUtils {

    private static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final static String DES = "DES";

    /**
     * 加密
     *
     * @param content
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, String strKey) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return encrypted;
    }

    /**
     * 解密
     *
     * @param strKey
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] content, String strKey) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(content);
        String originalString = new String(original);
        return originalString;
    }

    private static SecretKeySpec getKey(String strKey) {
        byte[] arrBTmp = strKey.getBytes();
        // 创建一个空的16位字节数组（默认值为0）
        byte[] arrB = new byte[16];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
        return skeySpec;
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) {
        return base64Code.isEmpty() ? null : Base64.getDecoder().decode(base64Code);
    }


    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(encrypt(content, encryptKey));
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return encryptStr.isEmpty() ? null : decrypt(base64Decode(encryptStr), decryptKey);
    }

    /**
     * 抑或加密
     * @param content
     * @param encryptKey
     * @return
     */
    private static String xorEncode(String content, String encryptKey){
        byte[] contentBytes = content.getBytes(DEFAULT_CHARSET);
        byte[] keyBytes= encryptKey.getBytes(DEFAULT_CHARSET);
        for(int i=0,size=contentBytes.length;i<size;i++){
            for(byte keyByte:keyBytes){
                contentBytes[i] = (byte) (contentBytes[i]^keyByte);
            }
        }
        return base64Encode(contentBytes);
    }

    /**
     * 抑或解密
     * @param content
     * @param encryptKey
     * @return
     */
    private static String xorDecode(String content, String encryptKey) {
        byte[] contentBytes = base64Decode(content);
        byte[] keyBytes= encryptKey.getBytes(DEFAULT_CHARSET);
        for(int i=0,size=contentBytes.length;i<size;i++){
            for(byte keyByte:keyBytes){
                contentBytes[i] = (byte) (contentBytes[i]^keyByte);
            }
        }
        return new String(contentBytes,DEFAULT_CHARSET);
    }

    /**
     * 数据库解密
     * @param encryptStr
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String dbDecrypt(String encryptStr, String encryptKey) throws Exception {
        return xorDecode(aesDecrypt(encryptStr,encryptKey),encryptKey);
    }

    /**
     * 数据库加密
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String dbEncrypt(String content, String encryptKey) throws Exception {
        return aesEncrypt(xorEncode(content,encryptKey),encryptKey);
    }


    /*********************************des start*********************************/

    public static byte[] desEncrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(src);
    }

    public static byte[] desDecrypt(byte[] src, byte[] key) throws Exception {
         SecureRandom sr = new SecureRandom();
         DESKeySpec dks = new DESKeySpec(key);
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
         SecretKey securekey = keyFactory.generateSecret(dks);
         Cipher cipher = Cipher.getInstance(DES);
         cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
         return cipher.doFinal(src);
     }

    public final static String desDecrypt(String data, String privatekey) {
        try {
            return new String(desDecrypt(hex2byte(data.getBytes()),privatekey.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String desEncrypt(String password, String privatekey) {
        try {
            return byte2hex(desEncrypt(password.getBytes(),privatekey.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("b length is error!");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }



    /********************************摘要-md5 start********************************/

    public static String md5(String content) {
        return md5(content,DEFAULT_CHARSET);
    }

    public static String md5(String content,Charset charset) {
        return md5(content.getBytes(charset));
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /********************************摘要-md5 end********************************/

    /********************************摘要-sha start********************************/

    public static String hmacSign(String aValue, String aKey) {
        return hmacSign(aValue, aKey , "MD5");
    }



    /**
     * 生成签名消息
     * @param aValue  要签名的字符串
     * @param aKey  签名密钥
     * @return
     */
    public static String hmacSign(String aValue, String aKey,String jiami) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try {
            keyb = aKey.getBytes(DEFAULT_CHARSET);
            value = aValue.getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(jiami);//"MD5"
        } catch (NoSuchAlgorithmException e) {

            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }

    public static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    /**
     * SHA加密
     * @param aValue
     * @return
     */
    public static String sha(String aValue) {
        aValue = aValue.trim();
        byte value[];
        try {
            value = aValue.getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            value = aValue.getBytes();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return toHex(md.digest(value));

    }

     

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    /********************************摘要********************************/

    public static void main(String[] args) throws Exception {
        String content="我的祖国的啊啊啊!@#${}{<>|`1";
        String key="123456789012";

        long start = new Date().getTime();
        for(int i = 0; i < 1000000;i++){
//            String enc =  dbEncrypt(content,key) ;
//            String dec = dbDecrypt(enc,key);
             md5(content);
        }

        System.out.println(new Date().getTime()-start);

        System.out.println( md5(content));

        String enc =  dbEncrypt(content,key) ;
        String dec = dbDecrypt(enc,key);
        System.out.println(enc);
        System.out.println(dec);


    }

}
