package cn.hchaojie.snippets.java;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * InputStream与String, Byte之间互转
 * @author ciro
 *
 */
public class ByteArray2String2InputStream {
	private static final int BUF_SIZE = 2048;

	public static void main(String[] args) {
		
	}

	public static String byteArray2String(byte[] in) throws UnsupportedEncodingException {
		return new String(in, "utf-8");
	}
	
	public static byte[] string2ByteArray(String s) throws UnsupportedEncodingException {
		return s.getBytes("utf-8");
	}
	
	/* method1 */
	public static byte[] is2ByteArray(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[BUF_SIZE];
		
		int count = -1;  
        while((count = in.read(bytes, 0, BUF_SIZE)) != -1) {
        	out.write(bytes, 0, count);
        }
	          
        bytes = null;  
        
        return out.toByteArray();
	}
	
	/* method2 */
	public static String is2String(InputStream is) throws UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
		
		StringWriter writer = new StringWriter();
	    
		char[] buffer = new char[BUF_SIZE]; 
        try {  
        	int n;
        	while ((n = reader.read(buffer)) != -1) {  
        		writer.write(buffer, 0, n);
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                is.close();   
            } catch (IOException e) {   
            	e.printStackTrace();
            }   
        }   
    
        return writer.toString();   
	}
	
	public static InputStream byteArray2Is(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
}
