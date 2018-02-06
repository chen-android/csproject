package com.cs.bbyz.http.encrypt;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Encryption<T> {
	
	public static final String KEY_ALGORITHM = "AES";
	
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
	
	public static byte[] initkey() throws Exception {
		
		// 实例化密钥生成器
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(256);
		kg.init(128);
		
		SecretKey secretKey = kg.generateKey();
		
		return secretKey.getEncoded();
	}
	
	public static byte[] initRootKey() throws Exception {
		
		return new byte[]{0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
				0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
				0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09,
				0x06, 0x07, 0x09, 0x0d};
		
	}
	
	public static Key toKey(byte[] key) throws Exception {
		
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		
		return secretKey;
	}
	
	//	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
//
//		Key k = toKey(key);
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, k);
//		return cipher.doFinal(data);
//	}
	//返回加密格式
	public static String encrypt(byte[] data, byte[] key) throws Exception {
		
		Key k = toKey(key);
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return bytesToHexString(cipher.doFinal(data));
	}
	
	public static byte[] iv = {0x4f, 0x5a, 0x46, 0x34, 0x36, 0x4a, 0x45, 0x34, 0x52, 0x4c, 0x39, 0x5a, 0x53, 0x35, 0x46, 0x50};
	
	public static String decrypt(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
		byte[] result = cipher.doFinal(data);
		return new String(result);
	}
	
	// 返回是map数组
	public static Map decryptMap(byte[] data, byte[] key) throws Exception {
		
		Map map = new HashMap();
		
		Key k = toKey(key);
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		String decryptString = new String(cipher.doFinal(data));
		
		String[] strs = null;
		if (decryptString != null && decryptString.length() > 0) {
			strs = decryptString.split("&");
		}
		if (strs != null && strs.length > 0) {
			
			for (int i = 0; i < strs.length; i++) {
				
				String[] tempStr = strs[i].split("=");
				if (tempStr != null && tempStr.length == 2) {
					map.put(tempStr[0], tempStr[1]);
				}
			}
		}
		
		return map;
	}
	
	// 返回是map数组
	public static <T> T decryptString(String str, byte[] key, Class className)
			throws Exception {
		
		byte[] data = hexStringToBytes(str);
		
		Map map = new HashMap();
		
		Key k = toKey(key);
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		String decryptString = new String(cipher.doFinal(data));
		// JsonObject obj = new JsonObject(decryptString);
		
		return null;
		// return map;
	}
	
	public static String MD5(String key) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer bufResult = new StringBuffer("");
			for (int j = 0; j < b.length; j++) {
				i = b[j];
				if (i < 0) i += 256;
				if (i < 16) bufResult.append("0");
				bufResult.append(Integer.toHexString(i));
			}
			result = bufResult.toString();
//			System.out.println("MD5(" + key + ",32) = " + result);
//			System.out.println("MD5(" + key + ",16) = "	+ bufResult.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}
	
	private static byte[] MD52(String sourceStr) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			return b;
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return null;
	}
	
	//转换为16进制字符串
	public static String bytesToHexString(byte[] source) {
		StringBuilder enSource = new StringBuilder("");
		if (source == null || source.length <= 0) {
			return null;
		}
		for (int i = 0; i < source.length; i++) {
			int v = source[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				enSource.append(0);
			}
			enSource.append(hv);
		}
		return enSource.toString();
	}
	
	//转换为字节数组
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] bResult = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			bResult[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return bResult;
	}
	
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

//	public static void main(String[] args) throws Exception {
//		
//		//-------------------------原文准备区-----------------------------
//		// 原文  account=a115&brokerNo=0012&salesChannel=0011
//		//PushInfoReJson content =new PushInfoReJson("a115","0012","0010");
//		String content="";
//		String contentJson =JsonUtil.getJson(content);
//		String strSource = contentJson;
//		System.out.println("原文:		" + strSource);
//		//-------------------------密钥-----------------------------
//		String strKey="hlgc.com";
//		System.out.println("key：		" + strKey);
//		byte[] key;
//		key = MD5(strKey).getBytes();
//		System.out.println("key的MD5值:	" + new String(key));
//		
//		//-------------------------加密-----------------------------
//		// 加密
//		byte[] enSource1 = "".getBytes();//AES256Encryption.encrypt(strSource.getBytes(), key);
//
//		// 转换为16进制字符串
//		String enSource2 = AES256Encryption.bytesToHexString(enSource1);
//		System.out.println("加密后:		" + enSource2);
//
//
//		//-------------------------解密----------------------------
//		// 转换为字节数组
//		byte[] deSource2 = hexStringToBytes(enSource2);
//		// 解密
//		byte[] deSource1 = AES256Encryption.decrypt(deSource2, key);
//		System.out.println("解密后：		" + new String(deSource1));
//
//
//	}
}