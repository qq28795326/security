package security;

import static org.junit.Assert.*;

import org.junit.Test;

public class PBECoderTest {

	@Test  
    public void test() throws Exception {  
        String inputStr = "abc";  
        System.err.println("ԭ��: " + inputStr);  
        byte[] input = inputStr.getBytes();  
  
        String pwd = "efg";  
        System.err.println("����: " + pwd);  
  
        byte[] salt = PBECoder.initSalt();  
  
        byte[] data = PBECoder.encrypt(input, pwd, salt);  
  
        System.err.println("���ܺ�: " + PBECoder.encryptBASE64(data));  
  
        byte[] output = PBECoder.decrypt(data, pwd, salt);  
        String outputStr = new String(output);  
  
        System.err.println("���ܺ�: " + outputStr);  
        assertEquals(inputStr, outputStr);  
    }  

}
