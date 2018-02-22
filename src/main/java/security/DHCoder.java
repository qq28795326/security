package security;
import java.security.Key;  
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.PublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  
import java.util.HashMap;  
import java.util.Map;  
  
import javax.crypto.Cipher;  
import javax.crypto.KeyAgreement;  
import javax.crypto.SecretKey;  
import javax.crypto.interfaces.DHPrivateKey;  
import javax.crypto.interfaces.DHPublicKey;  
import javax.crypto.spec.DHParameterSpec;  
  
/** 
 * DH��ȫ������� 
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public abstract class DHCoder extends Coder {  
    public static final String ALGORITHM = "DH";  
  
    /** 
     * Ĭ����Կ�ֽ��� 
     *  
     * <pre> 
     * DH 
     * Default Keysize 1024   
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive). 
     * </pre> 
     */  
    private static final int KEY_SIZE = 1024;  
  
    /** 
     * DH��������Ҫһ�ֶԳƼ����㷨�����ݼ��ܣ���������ʹ��DES��Ҳ����ʹ�������ԳƼ����㷨�� 
     */  
    public static final String SECRET_ALGORITHM = "DES";  
    private static final String PUBLIC_KEY = "DHPublicKey";  
    private static final String PRIVATE_KEY = "DHPrivateKey";  
  
    /** 
     * ��ʼ���׷���Կ 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey() throws Exception {  
        KeyPairGenerator keyPairGenerator = KeyPairGenerator  
                .getInstance(ALGORITHM);  
        keyPairGenerator.initialize(KEY_SIZE);  
  
        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
  
        // �׷���Կ  
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();  
  
        // �׷�˽Կ  
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();  
  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }  
  
    /** 
     * ��ʼ���ҷ���Կ 
     *  
     * @param key 
     *            �׷���Կ 
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey(String key) throws Exception {  
        // �����׷���Կ  
        byte[] keyBytes = decryptBASE64(key);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);  
  
        // �ɼ׷���Կ�����ҷ���Կ  
        DHParameterSpec dhParamSpec = ((DHPublicKey) pubKey).getParams();  
  
        KeyPairGenerator keyPairGenerator = KeyPairGenerator  
                .getInstance(keyFactory.getAlgorithm());  
        keyPairGenerator.initialize(dhParamSpec);  
  
        KeyPair keyPair = keyPairGenerator.generateKeyPair();  
  
        // �ҷ���Կ  
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();  
  
        // �ҷ�˽Կ  
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();  
  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
  
        return keyMap;  
    }  
  
    /** 
     * ����<br> 
     *  
     * @param data 
     *            ���������� 
     * @param publicKey 
     *            �׷���Կ 
     * @param privateKey 
     *            �ҷ�˽Կ 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encrypt(byte[] data, String publicKey,  
            String privateKey) throws Exception {  
  
        // ���ɱ�����Կ  
        SecretKey secretKey = getSecretKey(publicKey, privateKey);  
  
        // ���ݼ���  
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
  
        return cipher.doFinal(data);  
    }  
  
    /** 
     * ����<br> 
     *  
     * @param data 
     *            ���������� 
     * @param publicKey 
     *            �ҷ���Կ 
     * @param privateKey 
     *            �ҷ�˽Կ 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decrypt(byte[] data, String publicKey,  
            String privateKey) throws Exception {  
  
        // ���ɱ�����Կ  
        SecretKey secretKey = getSecretKey(publicKey, privateKey);  
        // ���ݽ���  
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, secretKey);  
  
        return cipher.doFinal(data);  
    }  
  
    /** 
     * ������Կ 
     *  
     * @param publicKey 
     *            ��Կ 
     * @param privateKey 
     *            ˽Կ 
     * @return 
     * @throws Exception 
     */  
    private static SecretKey getSecretKey(String publicKey, String privateKey)  
            throws Exception {  
        // ��ʼ����Կ  
        byte[] pubKeyBytes = decryptBASE64(publicKey);  
  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyBytes);  
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);  
  
        // ��ʼ��˽Կ  
        byte[] priKeyBytes = decryptBASE64(privateKey);  
  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKeyBytes);  
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory  
                .getAlgorithm());  
        keyAgree.init(priKey);  
        keyAgree.doPhase(pubKey, true);  
  
        // ���ɱ�����Կ  
        SecretKey secretKey = keyAgree.generateSecret(SECRET_ALGORITHM);  
  
        return secretKey;  
    }  
  
    /** 
     * ȡ��˽Կ 
     *  
     * @param keyMap 
     * @return 
     * @throws Exception 
     */  
    public static String getPrivateKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
  
        return encryptBASE64(key.getEncoded());  
    }  
  
    /** 
     * ȡ�ù�Կ 
     *  
     * @param keyMap 
     * @return 
     * @throws Exception 
     */  
    public static String getPublicKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
  
        return encryptBASE64(key.getEncoded());  
    }  
}  
