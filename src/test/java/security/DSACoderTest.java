package security;
import static org.junit.Assert.*;  
  
import java.util.Map;  
  
import org.junit.Test;  
  
/** 
 *  
 * @author ���� 
 * @version 1.0 
 * @since 1.0 
 */  
public class DSACoderTest {  
  
    @Test  
    public void test() throws Exception {  
        String inputStr = "abc";  
        byte[] data = inputStr.getBytes();  
  
        // ������Կ  
        Map<String, Object> keyMap = DSACoder.initKey();  
  
        // �����Կ  
        String publicKey = DSACoder.getPublicKey(keyMap);  
        String privateKey = DSACoder.getPrivateKey(keyMap);  
  
        System.err.println("��Կ:\r" + publicKey);  
        System.err.println("˽Կ:\r" + privateKey);  
  
        // ����ǩ��  
        String sign = DSACoder.sign(data, privateKey);  
        System.err.println("ǩ��:\r" + sign);  
  
        // ��֤ǩ��  
        boolean status = DSACoder.verify(data, publicKey, sign);  
        System.err.println("״̬:\r" + status);  
        assertTrue(status);  
  
    }  
  
}  
