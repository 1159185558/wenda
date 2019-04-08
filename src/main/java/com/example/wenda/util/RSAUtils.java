package com.example.wenda.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/20  22:44
 * @Description: This class is primaryly used to generate rsa public key and private key,
 * encrypt the message and decrypt message.
 */

@Service
public class RSAUtils {
    private static final String RSA_ALGORITHEM = "RSA";
    private static final Integer MAX_ENCRYPT_SIZE = 117;
    private static final Integer MAX_DECRYPT_SIZE = 128;
    private static Map<String, Object> keyMap = new HashMap<>();
    @Value("${publicKeyPath}")
    private static String publicKeyPAth;
    @Value(("${privateKeyPath"))
    private static String privateKeyPath;
    /*public static void main(String[] args) throws Exception {
        RSAUtils rsaUtils = new RSAUtils();
        rsaUtils.getKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey)keyMap.get("public_key");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyMap.get("private_key");
        System.out.println("Public Key:" + new String(Base64.encodeBase64(rsaPublicKey.getEncoded())));
        System.out.println("Private Key:" + new String((Base64.encodeBase64(rsaPrivateKey.getEncoded()))));
        String tmp = rsaUtils.encryptData("weilei");
        System.out.println("Encrytion:" + tmp);
        System.out.println("Decryption:" + rsaUtils.decryptData(tmp));
    }
*/

    /**
     * This method is used to get key pair, including public key and private key
     *
     * @return Map<String                                                                                                                               ,                                                                                                                                                                                                                                                               Object>
     * @throws NoSuchAlgorithmException
     */
    public void getKeyPair() throws NoSuchAlgorithmException {
        //Get key pair generator basing RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHEM);
        //Initialize key pair generator
        keyPairGenerator.initialize(1024);
        //Get key pair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        keyMap.put("public_key", rsaPublicKey);
        keyMap.put("private_key", rsaPrivateKey);
    }

    public void writeKeyPairToFile() {
        String publicKey = Base64.encodeBase64String(((RSAPublicKey) keyMap.get("public_key")).getEncoded());
        String privateKey = Base64.encodeBase64String(((RSAPrivateKey) keyMap.get("private_key")).getEncoded());
        //File file = new File();
    }

    /**
     * Encrypt String data
     *
     * @param s
     * @return String
     * @throws Exception
     */
    public String encryptData(String s) throws Exception {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyMap.get("public_key");
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHEM);
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] bytes = s.getBytes();
        //Get encrypted binary data
        byte[] cache;
        int length = bytes.length;
        int offset = 0;
        int i = 0;
        //Raw data must not be longer than 117 bytes,so we have to devide data into some blocks
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (length - offset > 0) {
            if (length - offset > MAX_ENCRYPT_SIZE) {
                cache = cipher.doFinal(bytes, offset, MAX_ENCRYPT_SIZE);
            } else {
                cache = cipher.doFinal(bytes, offset, length - offset);
            }
            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_SIZE;
        }
        byte[] encryptedData = outputStream.toByteArray();
        outputStream.close();
        //Convert binary data into String,if we use new String(decrytedData.getbytes()) to get string,
        // they will not be showed correctly,they will become messy code
        String encryptedDataString = new String(Base64.encodeBase64(encryptedData));
        return encryptedDataString;
    }

    /**
     * Encrypt String data
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public String decryptData(String encryptedData) throws Exception {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get("private_key");
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHEM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int i = 0;
        int offset = 0;
        byte[] cache;
        byte[] bytes = Base64.decodeBase64(encryptedData);
        int length = bytes.length;
        //Encrypted data must not be longer than 128 bytes,so we have to devide data into some blocks
        while (length - offset > 0) {
            if (length - offset > MAX_DECRYPT_SIZE) {
                cache = cipher.doFinal(bytes, offset, MAX_DECRYPT_SIZE);
            } else {
                cache = cipher.doFinal(bytes, offset, length - offset);
            }
            outputStream.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_SIZE;
        }
        byte[] decryptedData = outputStream.toByteArray();
        outputStream.close();
        String decryptedDataString = new String(decryptedData);
        return decryptedDataString;
    }

    public Map<String, Object> getKeyMap() {
        return keyMap;
    }
}



