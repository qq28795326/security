package security;
import static org.junit.Assert.*;  
  
import org.junit.Test;  
  
/** 
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public class CertificateCoderTest {  
    private String password = "123456";  
    private String alias = "www.zlex.org";  
    private String certificatePath = "d:/zlex.cer";  
    private String keyStorePath = "d:/zlex.keystore";  
  
    @Test  
    public void test() throws Exception {  
        System.err.println("��Կ���ܡ���˽Կ����");  
        String inputStr = "Ceritifcate";  
        byte[] data = inputStr.getBytes();  
  
        byte[] encrypt = CertificateCoder.encryptByPublicKey(data,  
                certificatePath);  
  
        byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt,  
                keyStorePath, alias, password);  
        String outputStr = new String(decrypt);  
  
        System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);  
  
        // ��֤����һ��  
        assertArrayEquals(data, decrypt);  
  
        // ��֤֤����Ч  
        assertTrue(CertificateCoder.verifyCertificate(certificatePath));  
  
    }  
  
    @Test  
    public void testSign() throws Exception {  
        System.err.println("˽Կ���ܡ�����Կ����");  
  
        String inputStr = "sign";  
        byte[] data = inputStr.getBytes();  
  
        byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,  
                keyStorePath, alias, password);  
  
        byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData,  
                certificatePath);  
  
        String outputStr = new String(decodedData);  
        System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);  
        assertEquals(inputStr, outputStr);  
  
        System.err.println("˽Կǩ��������Կ��֤ǩ��");  
        // ����ǩ��  
        String sign = CertificateCoder.sign(encodedData, keyStorePath, alias,  
                password);  
        System.err.println("ǩ��:\r" + sign);  
  
        // ��֤ǩ��  
        boolean status = CertificateCoder.verify(encodedData, sign,  
                certificatePath);  
        System.err.println("״̬:\r" + status);  
        assertTrue(status);  
  
    }  
}  
