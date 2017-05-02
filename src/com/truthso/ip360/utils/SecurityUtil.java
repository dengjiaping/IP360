package com.truthso.ip360.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;

import com.lidroid.xutils.util.LogUtils;
import com.ta.utdid2.android.utils.Base64;

/*import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;*/

public class SecurityUtil {
//	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	/* 加密 */
	public static String toBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
//			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

/*	 解密 
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
	 * 
	 * @param plainText
	 * @return
	 */
	public static String toMD5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(plainText.getBytes());
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// System.out.println("32位: " + buf.toString());// 32位的加密
			// System.out.println("16位: " + buf.toString().substring(8, 24));//
			// 16位的加密，其实就是32位加密后的截取
		} catch (Exception e) {
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

	//added by zzl
	//控制大文件的计算效率和使用内存
		public static String SHA512(String filePath) {
			if (CheckUtil.isEmpty(filePath)) {
				return null;
			}

			File file = new File(filePath);
			if (!file.exists() || !file.isFile()) {
				return null;
			}

			FileInputStream fis = null;
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-512");
				fis = new FileInputStream(file);
				byte[] buffer = new byte[8192];// 8KB
				int length = -1;
				// 开始计算
				while ((length = fis.read(buffer)) != -1) {
					md.update(buffer, 0, length);
				}

				return bytesToString(md.digest());

			} catch (IOException ex) {
				LogUtils.e(ex.getMessage());
			} catch (NoSuchAlgorithmException ex) {
				LogUtils.e(ex.getMessage());
			} finally {
				try {
					fis.close();
				} catch (IOException ex) {
					LogUtils.e(ex.getMessage());
				}
			}
			
			return null;
		}
	
		private static String SHA512(byte[] decript) {
		if (null == decript) return "";
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-512");
			digest.update(decript);
			byte messageDigest[] = digest.digest();
			
			//amend by zzl. start
			
//			//Create Hex String
//			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
//			for (int i = 0; i < messageDigest.length; i++) {
//				String shaHex = Integer.toHexString(0xFF & messageDigest[i]);
//				if (shaHex.length() == 1) {
//					hexString.append(0);
//				}
//				hexString.append(shaHex);
//			}
//			return hexString.toString();
			
			return bytesToString(messageDigest);
			//end

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

/*	private static byte[] loadData(String filePath) throws Exception {
		if (StringUtils.isEmpty(filePath))
			return null;
		FileInputStream fis = new FileInputStream(filePath);
		byte[] dataInput = new byte[fis.available()];
		//System.out.println("文件流长度=" + dataInput.length);
		fis.read(dataInput);
		fis.close();
		return dataInput;
	}*/

/*	public static Map<String, Object> getFile(String filePath) throws Exception {
		if (StringUtils.isEmpty(filePath))
			return null;
		Map<String, Object> m = new HashMap<String, Object>();
		FileInputStream fis = new FileInputStream(filePath);
		byte[] dataInput = new byte[fis.available()];
		fis.read(dataInput);
		fis.close();
		m.put("fileSize", dataInput.length);
		m.put("dataInput", dataInput);
		return m;
	}
*/
	private static String getSignature_sha512(byte[] source, Long times) {
		if (null == times)
			return "";
		String hash1 = SHA512(source);
		String hash2 = SHA512((hash1 + times).getBytes());
		return hash2;
	}
	
	/**
	 * added by zzl on 20161108. 根据原始文件的路径与时间一起生成新的sha码
	 * @param filePath
	 * @param times
	 * @return
	 */
	/*public static String getSignatureSha512FromFile(String filePath, Long time) {
		if (null == time || StringUtils.isBlank(filePath)){
			return null;
		}
		File file = new File(filePath);
		if(!file.exists()){
			return null;
		}
		String hash1 = SHA512(filePath);
		String hash2 = SHA512((hash1 + time).getBytes());
		return hash2;
	}*/
	
	/**
	 * added by zzl on 20161108. 根据原始文件的sha码与时间一起生成新的sha码
	 * @param srcFileSha512
	 * @param time
	 * @return
	 */
	public static String getSignatureSha512FromCode(String srcFileSha512, Long time) {
		if (null == time || srcFileSha512 == null)
			return null;
		String hash2 = SHA512((srcFileSha512 + time).getBytes());
		return hash2;
	}

	/**
	 * added by zzl on 20161019. 根据原始文件的sha码与时间一起生成新的sha码
	 * 
	 * @param srcFileSha512
	 * @param time
	 * @return
	 */
	private static String getSignature_sha512(String srcFileSha512, Long time) {
		if (null == time || srcFileSha512 == null)
			return null;
		String hash2 = SHA512((srcFileSha512 + time).getBytes());
		return hash2;
	}
	
	private static String bytesToString(byte[] bytes) {
		// Create Hex String
		StringBuffer hexString = new StringBuffer();
		// 字节数组转换为 十六进制 数
		for (int i = 0; i < bytes.length; i++) {
			String shaHex = Integer.toHexString(0xFF & bytes[i]);
			if (shaHex.length() == 1) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}

		return hexString.toString();
	}

	public static void main(String[] args) throws Exception {
		// String s="123456";
		// System.out.println(toMD5(s));
		// byte[] decript=SecurityUtil.loadData("C:\\领导力提升方法.jpg");
		// String sha1=SecurityUtil.SHA1(decript);
		// System.out.println(sha1);
		// System.out.println("154e720a4ed2a570f1199e5ddd21c0d69ab54ad8".equalsIgnoreCase("154e720a4ed2a570f1199e5ddd21c0d69ab54ad8"));
		// SHA-256
		// String result = DigestUtils.sha256Hex("www.what21.com");
		// System.out.println("sha256Hex="+result);
		// SHA-384
		// String result2 = DigestUtils.sha384Hex("www.what21.com");
		// System.out.println("sha384Hex="+result2);
		// SHA-512
		// long b=System.currentTimeMillis();
		// FileInputStream fis = new
		// FileInputStream("C:/《疯狂Ajax讲义(第3版)》.(李刚).[PDF]@ckook.pdf");
		// String result3 = DigestUtils.sha512Hex(fis);
		// long e=System.currentTimeMillis();
		// System.out.println("耗时：="+(e-b));
		// System.out.println("sha512Hex="+result3);
		// SHA-1
		// String result4 = DigestUtils.shaHex("www.what21.com".getBytes());
		// System.out.println("sha1="+result4);
		// FileInputStream fis = new FileInputStream("C:/a.pdf");
		// Date d=new Date();
		// System.out.println(SecurityUtil.getSignature_sha512(new
		// byte[fis.available()], d.getTime()));
		// String result3 = DigestUtils.sha512Hex("www.what21.com");
		// System.out.println(result3);
//
//		byte[] bytes = SecurityUtil
//				.loadData("D:/tmp/snapshot/dcb9871618207fa26c051d6f5531f4ee.png");
//		String sha512 = SecurityUtil.getSignature_sha512(bytes,
//				new Date().getTime());
//		System.out.println(sha512);

	}
//	byte[] signbyte = rsaEncrypt.rsaSign(authInfo.toString(),rsaEncrypt.getPrivateKey());
//	oriSign=com.truthso.api.util.RsaEncrypt.ByteToHex(signbyte);
	/**
	 * rsa签名
	 *
	 * @param content
	 *            待签名的字符串
	 * @param privateKey
	 *            rsa私钥字符串
	 * @param charset
	 *            字符编码
	 * @return 签名结果
	 * @throws Exception
	 *             签名失败则抛出异常
	 */
	public byte[] rsaSign(String content, RSAPrivateKey priKey)
			throws SignatureException {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes("utf-8"));

			byte[] signed = signature.sign();
			return signed;
		} catch (Exception e) {
			throw new SignatureException("RSAcontent = " + content
					+ "; charset = ", e);
		}
	}
	// btye转换hex函数
	public static String ByteToHex(byte[] byteArray) {
//        StringBuffer StrBuff = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
//                StrBuff.append("0").append(
//                        Integer.toHexString(0xFF & byteArray[i]));
//            } else {
//                StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//            }
//        }
//        return StrBuff.toString();
		//return (new BASE64Encoder()).encodeBuffer(byteArray);
		//return  Base64.encodeBase64String(byteArray);
//		return Base64.encode(byteArray);
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}
}
