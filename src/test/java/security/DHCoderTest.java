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
public class DHCoderTest {  
  
    @Test  
    public void test() throws Exception {  
        // ���ɼ׷���Կ�Զ�  
        Map<String, Object> aKeyMap = DHCoder.initKey();  
        String aPublicKey = DHCoder.getPublicKey(aKeyMap);  
        String aPrivateKey = DHCoder.getPrivateKey(aKeyMap);  
  
        System.err.println("�׷���Կ:\r" + aPublicKey);  
        System.err.println("�׷�˽Կ:\r" + aPrivateKey);  
          
        // �ɼ׷���Կ����������Կ�Զ�  
        Map<String, Object> bKeyMap = DHCoder.initKey(aPublicKey);  
        String bPublicKey = DHCoder.getPublicKey(bKeyMap);  
        String bPrivateKey = DHCoder.getPrivateKey(bKeyMap);  
          
        System.err.println("�ҷ���Կ:\r" + bPublicKey);  
        System.err.println("�ҷ�˽Կ:\r" + bPrivateKey);  
          
        String aInput = "abc ";  
        System.err.println("ԭ��: " + aInput);  
  
        // �ɼ׷���Կ���ҷ�˽Կ��������  
        byte[] aCode = DHCoder.encrypt(aInput.getBytes(), aPublicKey,  
                bPrivateKey);  
  
        // ���ҷ���Կ���׷�˽Կ����  
        byte[] aDecode = DHCoder.decrypt(aCode, bPublicKey, aPrivateKey);  
        String aOutput = (new String(aDecode));  
  
        System.err.println("����: " + aOutput);  
  
        assertEquals(aInput, aOutput);  
  
        System.err.println(" ===============���������ܽ���================== ");  
        String bInput = "def ";  
        System.err.println("ԭ��: " + bInput);  
  
        // ���ҷ���Կ���׷�˽Կ��������  
        byte[] bCode = DHCoder.encrypt(bInput.getBytes(), bPublicKey,  
                aPrivateKey);  
  
        // �ɼ׷���Կ���ҷ�˽Կ����  
        byte[] bDecode = DHCoder.decrypt(bCode, aPublicKey, bPrivateKey);  
        String bOutput = (new String(bDecode));  
  
        System.err.println("����: " + bOutput);  
  
        assertEquals(bInput, bOutput);  
    }  
  
}  
