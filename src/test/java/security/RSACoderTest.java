package security;

import static org.junit.Assert.*;  
  
import org.junit.Before;  
import org.junit.Test;  
  
import java.util.Map;  
  
/** 
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public class RSACoderTest {  
    private String publicKey;  
    private String privateKey;  
  
    @Before  
    public void setUp() throws Exception {  
        Map<String, Object> keyMap = RSACoder.initKey();  
  
        publicKey = RSACoder.getPublicKey(keyMap);  
        privateKey = RSACoder.getPrivateKey(keyMap);  
        System.err.println("��Կ: \n\r" + publicKey);  
        System.err.println("˽Կ�� \n\r" + privateKey);  
    }  
  
    @Test  
    public void test() throws Exception {  
        System.err.println("��Կ���ܡ���˽Կ����");  
        String inputStr = "abc";  
        byte[] data = inputStr.getBytes();  
  
        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);  
  
        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,  
                privateKey);  
  
        String outputStr = new String(decodedData);  
        System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);  
        assertEquals(inputStr, outputStr);  
  
    }  
  
    @Test  
    public void testSign() throws Exception {  
        System.err.println("˽Կ���ܡ�����Կ����");  
        String inputStr = "sign";  
        byte[] data = inputStr.getBytes();  
  
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);  
  
        byte[] decodedData = RSACoder  
                .decryptByPublicKey(encodedData, publicKey);  
  
        String outputStr = new String(decodedData);  
        System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);  
        assertEquals(inputStr, outputStr);  
  
        System.err.println("˽Կǩ��������Կ��֤ǩ��");  
        // ����ǩ��  
        String sign = RSACoder.sign(encodedData, privateKey);  
        System.err.println("ǩ��:\r" + sign);  
  
        // ��֤ǩ��  
        boolean status = RSACoder.verify(encodedData, publicKey, sign);  
        System.err.println("״̬:\r" + status);  
        assertTrue(status);  
  
    }  
  
}  
