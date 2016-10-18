package com.truthso.ip360.utils;


import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class SecurityUtil {
	  /*  加密  
	    public static String toBase64(String str) {  
	        byte[] b = null;  
	        String s = null;
	        try {
	            b = str.getBytes("utf-8");  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();
	        }  
	        if (b != null) {  
	            s = new BASE64Encoder().encode(b);  
	        }  
	        return s;  
	    }  
	  
	    解密  
	    public static String getFromBase64(String s) {  
	        byte[] b = null;  
	        String result = null;  
	        if (s != null) {  
	            BASE64Decoder decoder = new BASE64Decoder();  
	            try {  
	                b = decoder.decodeBuffer(s);  
	                result = new String(b, "utf-8");  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return result;  
	    }*/
	    
	    /**
	     * MD5加密
	     * @param plainText
	     * @return
	     */
	    public static String toMD5(String plainText) {
	    	 StringBuffer buf = new StringBuffer("");
	        try {
	           //生成实现指定摘要算法的 MessageDigest 对象。
	           MessageDigest md = MessageDigest.getInstance("MD5");  
	           //使用指定的字节数组更新摘要。
	           md.update(plainText.getBytes());
	           //通过执行诸如填充之类的最终操作完成哈希计算。
	           byte b[] = md.digest();
	           //生成具体的md5密码到buf数组
	           int i;
	           for (int offset = 0; offset < b.length; offset++) {
	             i = b[offset];
	             if (i < 0)
	               i += 256;
	             if (i < 16)
	               buf.append("0");
	             buf.append(Integer.toHexString(i));
	           }
//	           System.out.println("32位: " + buf.toString());// 32位的加密
//	           System.out.println("16位: " + buf.toString().substring(8, 24));// 16位的加密，其实就是32位加密后的截取
	        } 
	        catch (Exception e) {
	          e.printStackTrace();
	        }
	        return buf.toString();
	      }
	    
	    public static String SHA1(byte[] decript) {
			try {
				MessageDigest digest = java.security.MessageDigest
						.getInstance("SHA-1");
				digest.update(decript);
				byte messageDigest[] = digest.digest();
				// Create Hex String
				StringBuffer hexString = new StringBuffer();
				// 字节数组转换为 十六进制 数
				for (int i = 0; i < messageDigest.length; i++) {
					String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
					if (shaHex.length() < 2) {
						hexString.append(0);
					}
					hexString.append(shaHex);
				}
				return hexString.toString();

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return "";
		}

		public static String SHA512(byte[] decript) {
			if(null==decript)return "";
			try {
				MessageDigest digest = java.security.MessageDigest
						.getInstance("SHA-512");
				digest.update(decript);
				byte messageDigest[] = digest.digest();
				// Create Hex String
				StringBuffer hexString = new StringBuffer();
				// 字节数组转换为 十六进制 数
				for (int i = 0; i < messageDigest.length; i++) {
					String shaHex = Integer.toHexString(0xFF & messageDigest[i]);
					if (shaHex.length() == 1) {
						hexString.append(0);
					}
					hexString.append(shaHex);
				}
				return hexString.toString();

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return "";
		}
		
		public static byte[] loadData(String filePath) throws Exception {
			if(CheckUtil.isEmpty(filePath))return null;
			FileInputStream fis = new FileInputStream(filePath);
			byte[] dataInput = new byte[fis.available()];
			System.out.println("文件流长度="+dataInput.length);
			fis.read(dataInput);
			fis.close();
			return dataInput;
		}
		
		public static Map<String,Object> getFile(String filePath) throws Exception {
			if(CheckUtil.isEmpty(filePath))return null;
			Map<String,Object> m=new HashMap<String,Object>();
			FileInputStream fis = new FileInputStream(filePath);
			byte[] dataInput = new byte[fis.available()];
			fis.read(dataInput);
			fis.close();
			m.put("fileSize", dataInput.length);
			m.put("dataInput", dataInput);
			return m;
		}
		
		public static String getSignature_sha512(byte[] source,Long times){
			if(null==times)return "";
			String hash1=SHA512(source);
			String hash2=SHA512((hash1+times).getBytes());
			return hash2;
		}
	    
	    public static void main(String[] args) throws Exception {
//			String s="123456";
//			System.out.println(toMD5(s));
//	    	byte[] decript=SecurityUtil.loadData("C:\\领导力提升方法.jpg");
//	    	String sha1=SecurityUtil.SHA1(decript);
//	    	System.out.println(sha1);
	    	//System.out.println("154e720a4ed2a570f1199e5ddd21c0d69ab54ad8".equalsIgnoreCase("154e720a4ed2a570f1199e5ddd21c0d69ab54ad8"));
	    	// SHA-256
	      //  String result = DigestUtils.sha256Hex("www.what21.com");
	       // System.out.println("sha256Hex="+result);
	        // SHA-384
	      //  String result2 = DigestUtils.sha384Hex("www.what21.com");
	      // System.out.println("sha384Hex="+result2);
	        // SHA-512
//	    	long b=System.currentTimeMillis();
//	    	FileInputStream fis = new FileInputStream("C:/《疯狂Ajax讲义(第3版)》.(李刚).[PDF]@ckook.pdf");
//	        String result3 = DigestUtils.sha512Hex(fis);
//	        long e=System.currentTimeMillis();
//	        System.out.println("耗时：="+(e-b));
//	        System.out.println("sha512Hex="+result3);
	        // SHA-1
	      //  String result4 = DigestUtils.shaHex("www.what21.com".getBytes());
	      //  System.out.println("sha1="+result4);
//	    	FileInputStream fis = new FileInputStream("C:/a.pdf");
//	    	Date d=new Date();
//	    	System.out.println(SecurityUtil.getSignature_sha512(new byte[fis.available()], d.getTime()));
	    	// String result3 = DigestUtils.sha512Hex("www.what21.com");
	    	// System.out.println(result3);
	    	
	    	byte[] bytes=SecurityUtil.loadData("D:/tmp/snapshot/dcb9871618207fa26c051d6f5531f4ee.png");
	    	String sha512=SecurityUtil.getSignature_sha512(bytes, new Date().getTime());
			System.out.println(sha512);
	    	
		}
	    
}
