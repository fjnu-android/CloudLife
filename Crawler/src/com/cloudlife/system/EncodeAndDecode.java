package com.cloudlife.system;

import java.security.MessageDigest;

/**
 * ʵ�����ݵĸ��ּ��ܽ��ܷ�ʽ
 * 
 * @author wuyi
 *
 */
public class EncodeAndDecode {

	// md5����
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

	// 16����ת������
	public static String byteArrayToHex(byte[] byteArray) {

		// ���ȳ�ʼ��һ���ַ����飬�������ÿ��16�����ַ�

		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
		// newһ���ַ����飬�������������ɽ���ַ����ģ�����һ�£�һ��byte�ǰ�λ�����ƣ�Ҳ����2λʮ�������ַ���2��8�η�����16��2�η�����
		char[] resultCharArray =new char[byteArray.length * 2];
		// �����ֽ����飬ͨ��λ���㣨λ����Ч�ʸߣ���ת�����ַ��ŵ��ַ�������ȥ
		int index = 0;
		for (byte b : byteArray) {

			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b& 0xf];
		}
		// �ַ�������ϳ��ַ�������
		return new String(resultCharArray);
	}
}
