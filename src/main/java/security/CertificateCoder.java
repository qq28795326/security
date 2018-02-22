package security;
import java.io.FileInputStream;  
import java.security.KeyStore;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.Signature;  
import java.security.cert.Certificate;  
import java.security.cert.CertificateFactory;  
import java.security.cert.X509Certificate;  
import java.util.Date;  
  
import javax.crypto.Cipher;  
  
/** 
 * ����֤��   ֤�����ɣ�http://snowolf.iteye.com/blog/391931
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public abstract class CertificateCoder extends Coder {  
  
  
    /** 
     * Java��Կ��(Java Key Store��JKS)KEY_STORE 
     */  
    public static final String KEY_STORE = "JKS";  
  
    public static final String X509 = "X.509";  
  
    /** 
     * ��KeyStore���˽Կ 
     *  
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     * @throws Exception 
     */  
    private static PrivateKey getPrivateKey(String keyStorePath, String alias,  
            String password) throws Exception {  
        KeyStore ks = getKeyStore(keyStorePath, password);  
        PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());  
        return key;  
    }  
  
    /** 
     * ��Certificate��ù�Կ 
     *  
     * @param certificatePath 
     * @return 
     * @throws Exception 
     */  
    private static PublicKey getPublicKey(String certificatePath)  
            throws Exception {  
        Certificate certificate = getCertificate(certificatePath);  
        PublicKey key = certificate.getPublicKey();  
        return key;  
    }  
  
    /** 
     * ���Certificate 
     *  
     * @param certificatePath 
     * @return 
     * @throws Exception 
     */  
    private static Certificate getCertificate(String certificatePath)  
            throws Exception {  
        CertificateFactory certificateFactory = CertificateFactory  
                .getInstance(X509);  
        FileInputStream in = new FileInputStream(certificatePath);  
  
        Certificate certificate = certificateFactory.generateCertificate(in);  
        in.close();  
  
        return certificate;  
    }  
  
    /** 
     * ���Certificate 
     *  
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     * @throws Exception 
     */  
    private static Certificate getCertificate(String keyStorePath,  
            String alias, String password) throws Exception {  
        KeyStore ks = getKeyStore(keyStorePath, password);  
        Certificate certificate = ks.getCertificate(alias);  
  
        return certificate;  
    }  
  
    /** 
     * ���KeyStore 
     *  
     * @param keyStorePath 
     * @param password 
     * @return 
     * @throws Exception 
     */  
    private static KeyStore getKeyStore(String keyStorePath, String password)  
            throws Exception {  
        FileInputStream is = new FileInputStream(keyStorePath);  
        KeyStore ks = KeyStore.getInstance(KEY_STORE);  
        ks.load(is, password.toCharArray());  
        is.close();  
        return ks;  
    }  
  
    /** 
     * ˽Կ���� 
     *  
     * @param data 
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,  
            String alias, String password) throws Exception {  
        // ȡ��˽Կ  
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);  
  
        // �����ݼ���  
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
  
        return cipher.doFinal(data);  
  
    }  
  
    /** 
     * ˽Կ���� 
     *  
     * @param data 
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,  
            String alias, String password) throws Exception {  
        // ȡ��˽Կ  
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);  
  
        // �����ݼ���  
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
  
        return cipher.doFinal(data);  
  
    }  
  
    /** 
     * ��Կ���� 
     *  
     * @param data 
     * @param certificatePath 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String certificatePath)  
            throws Exception {  
  
        // ȡ�ù�Կ  
        PublicKey publicKey = getPublicKey(certificatePath);  
        // �����ݼ���  
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
  
        return cipher.doFinal(data);  
  
    }  
  
    /** 
     * ��Կ���� 
     *  
     * @param data 
     * @param certificatePath 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] data, String certificatePath)  
            throws Exception {  
        // ȡ�ù�Կ  
        PublicKey publicKey = getPublicKey(certificatePath);  
  
        // �����ݼ���  
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);  
  
        return cipher.doFinal(data);  
  
    }  
  
    /** 
     * ��֤Certificate 
     *  
     * @param certificatePath 
     * @return 
     */  
    public static boolean verifyCertificate(String certificatePath) {  
        return verifyCertificate(new Date(), certificatePath);  
    }  
  
    /** 
     * ��֤Certificate�Ƿ���ڻ���Ч 
     *  
     * @param date 
     * @param certificatePath 
     * @return 
     */  
    public static boolean verifyCertificate(Date date, String certificatePath) {  
        boolean status = true;  
        try {  
            // ȡ��֤��  
            Certificate certificate = getCertificate(certificatePath);  
            // ��֤֤���Ƿ���ڻ���Ч  
            status = verifyCertificate(date, certificate);  
        } catch (Exception e) {  
            status = false;  
        }  
        return status;  
    }  
  
    /** 
     * ��֤֤���Ƿ���ڻ���Ч 
     *  
     * @param date 
     * @param certificate 
     * @return 
     */  
    private static boolean verifyCertificate(Date date, Certificate certificate) {  
        boolean status = true;  
        try {  
            X509Certificate x509Certificate = (X509Certificate) certificate;  
            x509Certificate.checkValidity(date);  
        } catch (Exception e) {  
            status = false;  
        }  
        return status;  
    }  
  
    /** 
     * ǩ�� 
     *  
     * @param keyStorePath 
     * @param alias 
     * @param password 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] sign, String keyStorePath, String alias,  
            String password) throws Exception {  
        // ���֤��  
        X509Certificate x509Certificate = (X509Certificate) getCertificate(  
                keyStorePath, alias, password);  
        // ��ȡ˽Կ  
        KeyStore ks = getKeyStore(keyStorePath, password);  
        // ȡ��˽Կ  
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password  
                .toCharArray());  
  
        // ����ǩ��  
        Signature signature = Signature.getInstance(x509Certificate  
                .getSigAlgName());  
        signature.initSign(privateKey);  
        signature.update(sign);  
        return encryptBASE64(signature.sign());  
    }  
  
    /** 
     * ��֤ǩ�� 
     *  
     * @param data 
     * @param sign 
     * @param certificatePath 
     * @return 
     * @throws Exception 
     */  
    public static boolean verify(byte[] data, String sign,  
            String certificatePath) throws Exception {  
        // ���֤��  
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);  
        // ��ù�Կ  
        PublicKey publicKey = x509Certificate.getPublicKey();  
        // ����ǩ��  
        Signature signature = Signature.getInstance(x509Certificate  
                .getSigAlgName());  
        signature.initVerify(publicKey);  
        signature.update(data);  
  
        return signature.verify(decryptBASE64(sign));  
  
    }  
  
    /** 
     * ��֤Certificate 
     *  
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     */  
    public static boolean verifyCertificate(Date date, String keyStorePath,  
            String alias, String password) {  
        boolean status = true;  
        try {  
            Certificate certificate = getCertificate(keyStorePath, alias,  
                    password);  
            status = verifyCertificate(date, certificate);  
        } catch (Exception e) {  
            status = false;  
        }  
        return status;  
    }  
  
    /** 
     * ��֤Certificate 
     *  
     * @param keyStorePath 
     * @param alias 
     * @param password 
     * @return 
     */  
    public static boolean verifyCertificate(String keyStorePath, String alias,  
            String password) {  
        return verifyCertificate(new Date(), keyStorePath, alias, password);  
    }  
}  
