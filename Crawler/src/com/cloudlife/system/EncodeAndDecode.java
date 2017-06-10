package com.cloudlife.system;

import java.security.MessageDigest;

/**
 * 实现数据的各种加密解密方式
 * 
 * @author wuyi
 *
 */
public class EncodeAndDecode {

	// md5加密
	public static String md5(String pre) {
		try {
			byte[] inputByte = pre.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputByte);
			byte[] resultByteArray = md.digest();
			return byteArrayToHex(resultByteArray);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 16进制转换函数
	public static String byteArrayToHex(byte[] byteArray) {

		// 首先初始化一个字符数组，用来存放每个16进制字符

		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray =new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {

			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b& 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}
}
