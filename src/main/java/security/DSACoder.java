package security;
import java.security.Key;  
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.SecureRandom;  
import java.security.Signature;  
import java.security.interfaces.DSAPrivateKey;  
import java.security.interfaces.DSAPublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  
import java.util.HashMap;  
import java.util.Map;  
  
/** 
 * DSA��ȫ������� 
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public abstract class DSACoder extends Coder {  
  
    public static final String ALGORITHM = "DSA";  
  
    /** 
     * Ĭ����Կ�ֽ��� 
     *  
     * <pre> 
     * DSA  
     * Default Keysize 1024   
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive). 
     * </pre> 
     */  
    private static final int KEY_SIZE = 1024;  
  
    /** 
     * Ĭ������ 
     */  
    private static final String DEFAULT_SEED = "0f22507a10bbddd07d8a3082122966e3";  
  
    private static final String PUBLIC_KEY = "DSAPublicKey";  
    private static final String PRIVATE_KEY = "DSAPrivateKey";  
  
    /** 
     * ��˽Կ����Ϣ��������ǩ�� 
     *  
     * @param data 
     *            �������� 
     * @param privateKey 
     *            ˽Կ 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] data, String privateKey) throws Exception {  
        // ������base64�����˽Կ  
        byte[] keyBytes = decryptBASE64(privateKey);  
  
        // ����PKCS8EncodedKeySpec����  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
  
        // KEY_ALGORITHM ָ���ļ����㷨  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
  
        // ȡ˽Կ�׶���  
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        // ��˽Կ����Ϣ��������ǩ��  
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());  
        signature.initSign(priKey);  
        signature.update(data);  
  
        return encryptBASE64(signature.sign());  
    }  
  
    /** 
     * У������ǩ�� 
     *  
     * @param data 
     *            �������� 
     * @param publicKey 
     *            ��Կ 
     * @param sign 
     *            ����ǩ�� 
     *  
     * @return У��ɹ�����true ʧ�ܷ���false 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
  
        // ������base64����Ĺ�Կ  
        byte[] keyBytes = decryptBASE64(publicKey);  
  
        // ����X509EncodedKeySpec����  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
  
        // ALGORITHM ָ���ļ����㷨  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
  
        // ȡ��Կ�׶���  
        PublicKey pubKey = keyFactory.generatePublic(keySpec);  
  
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());  
        signature.initVerify(pubKey);  
        signature.update(data);  
  
        // ��֤ǩ���Ƿ�����  
        return signature.verify(decryptBASE64(sign));  
    }  
  
    /** 
     * ������Կ 
     *  
     * @param seed 
     *            ���� 
     * @return ��Կ���� 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey(String seed) throws Exception {  
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);  
        // ��ʼ�����������  
        SecureRandom secureRandom = new SecureRandom();  
        secureRandom.setSeed(seed.getBytes());  
        keygen.initialize(KEY_SIZE, secureRandom);  
  
        KeyPair keys = keygen.genKeyPair();  
  
        DSAPublicKey publicKey = (DSAPublicKey) keys.getPublic();  
        DSAPrivateKey privateKey = (DSAPrivateKey) keys.getPrivate();  
  
        Map<String, Object> map = new HashMap<String, Object>(2);  
        map.put(PUBLIC_KEY, publicKey);  
        map.put(PRIVATE_KEY, privateKey);  
  
        return map;  
    }  
  
    /** 
     * Ĭ��������Կ 
     *  
     * @return ��Կ���� 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey() throws Exception {  
        return initKey(DEFAULT_SEED);  
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
