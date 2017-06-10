package com.cloudlife.resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.system.EncodeAndDecode;
import com.cloudlife.util.GetPostUtil;

/**
 *  ʵ����ȡָ����ҳ������ �����
 * 
 * @author wuyi
 *
 */

public class GetResource {

	private class Data {
		String url;
		String type;

		public Data(String url, String type) {
			this.url = url;
			this.type = type;
		}
	
		@Override
		public boolean equals(Object obj) {
			Data cmp = (Data) obj;
			if (cmp.url.equals(url)== true &&
					cmp.type.equals(type) == true)
				return true;
			return false;
		}
	}

	static private GetResource m_GetResource;
	// �ö��д�ŵ�ǰ��Ҫ�������ַ
	private ArrayDeque<Data> m_DataQueue = new ArrayDeque<Data>();

	private ArrayList<Data> m_ExistData = new ArrayList<Data>();

	private int m_nHasDeal = 0;
	
	private String[] bodyTypes = new String[]{
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qixuzhi.html\">������</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qixuzhi.html\">������</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yangxuzhi.html\">������</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yangxuzhi.html\">������</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yinxuzhi.html\">������</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yinxuzhi.html\">������</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/pinghezhi.html\">ƽ����</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/pinghezhi.html\">ƽ����</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/shirezhi.html\">ʪ����</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/shirezhi.html\">ʪ����</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tanshizhi.html\">̵ʪ��</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tanshizhi.html\">̵ʪ��</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tebingzhi.html\">�ر���</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tebingzhi.html\">�ر���</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qiyuzhi.html\">������</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qiyuzhi.html\">������</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/xueyuzhi.html\">Ѫ����</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/xueyuzhi.html\">Ѫ����</a>"};

	private String[]  bodyTypeAsw = new String[]{"yi_B", "ji_B","yi_D","ji_D","yi_C","ji_C","yi_A","ji_A",
							"yi_F", "ji_F","yi_E", "ji_E","yi_I", "ji_I","yi_G", "ji_G","yi_H", "ji_H"};
	
	static public GetResource getInstance() {

		if (m_GetResource == null)
			m_GetResource = new GetResource();
		return m_GetResource;
	}

	// �ӹ��캯���������������߳̽������洦��
	// ��ΪGetResouce����õ���ģʽ ���Ա����̵߳İ�ȫ��
	private GetResource() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_DataQueue.isEmpty() == false) {
						
						// �������� ��ȡ����   ������
						System.out.println("count:" + m_DataQueue.size());
						Data data = m_DataQueue.pollFirst();
						
						try {
							getMenuData(data.url, data.type);
						} catch (Exception e) {
							e.printStackTrace();
							addLog(e.getMessage());
						}
						++m_nHasDeal;
						PublicData.gHasDeal = m_nHasDeal;
						System.out.println("count:" + m_DataQueue.size());
					}
					
				}

			}
		}).start();
	}
	
	public boolean addResource(String url, String type, int page) {

		// �������������ȡ������Ʒ���� һ���Ի�ȡһ����Ŀ�Ĳ�Ʒ
		for (int i =0; i<= page; ++i) {
			// �������� ��������
			GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
			int pageCnt = i+1;
			utilParam.szUrl = url+ pageCnt;
			String strRet = GetPostUtil.getNet(utilParam);//cla_ing_i_list
			Pattern pat = Pattern.compile("<a target=\"_blank\" class=\"gray\" href='/food/[^']{1,50}'");
			Matcher mat = pat.matcher(strRet);//java.net.URLEncoder.encode(param.szUrl,   "utf-8");  

			while (mat.find()) {
				String curl = mat.group().replace("<a target=\"_blank\" class=\"gray\" href='", "").replace("'", "");

				Data data = new Data("http://www.ilinkee.com"+curl, type);
				
				m_ExistData.add(data);
				m_DataQueue.offerLast(data);
				PublicData.gNeedDeal = m_DataQueue.size();
			}
		}
		return true;
	}

	
	public boolean addResource3(String url, String type, int page) {

		// �������������ȡ������Ʒ���� һ���Ի�ȡһ����Ŀ�Ĳ�Ʒ
		for (int i =2; i<= page; ++i) {
			// �������� ��������
			GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
			int pageCnt = i+1;
			utilParam.szUrl = url+ pageCnt;
			String strRet = GetPostUtil.getNet(utilParam);//cla_ing_i_list
			Pattern pat = Pattern.compile("(?=cla_ing_i_list)([\\s\\S]*?)(?=pagebar)");
			Matcher mat = pat.matcher(strRet);//java.net.URLEncoder.encode(param.szUrl,   "utf-8");  
			if (mat.find()) {
				strRet = mat.group();
				pat = Pattern.compile("<a title=\"[^\"]{1,10}\"");
				mat = pat.matcher(strRet);
			} else {
				return false;
			}
			while (mat.find()) {
				String curl = mat.group().replace("<a title=\"", "").replace("\"", "");
				if (curl.equals("����")|| curl.equals("����"))
					continue;
				try {
					curl = java.net.URLEncoder.encode(curl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}  
				Data data = new Data("http://www.xiangha.com/shicai/"+curl, type);
				
				m_ExistData.add(data);
				m_DataQueue.offerLast(data);
				PublicData.gNeedDeal = m_DataQueue.size();
			}
		}
		return true;
	}

	public boolean addResource(String url) {

		// �������������ȡ������Ʒ���� һ���Ի�ȡһ����Ŀ�Ĳ�Ʒ
		
		// �������� ��������
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);
		// http://www.cnys.com/shicai/33275.html
		Pattern pat = Pattern.compile("http://www.cnys.com/shicai/[^.]{1,10}.html");
		Matcher mat = pat.matcher(strRet);
		while (mat.find()) {
			String curl = mat.group().replace(" ", "");
			Data data = new Data(curl, "A");
			m_ExistData.add(data);
			m_DataQueue.offerLast(data);
			PublicData.gNeedDeal = m_DataQueue.size();
		}
		return true;
	}

	public boolean addResouce(String url, String type) {
		SqlDeal sql = new SqlDeal();
		try{
			PreparedStatement stmt = sql.getConnection().prepareStatement("select bodyType from resource_food_dish where url=?;");
			stmt.setString(1, url);
			ResultSet res = stmt.executeQuery();
			String tmp = res.getString("bodyType");
			if (tmp != null) {
				if (tmp.contains(type))
					return false;
				else {
					tmp = tmp + " " + type;
					stmt = sql.getConnection().prepareStatement("update resource_food_dish set bodyType=? where url=?;");
					stmt.setString(1, tmp);
					stmt.setString(2, url);
					stmt.executeUpdate();
					++m_nHasDeal;
					PublicData.gHasDeal = m_nHasDeal;

					if (true) {
						stmt = sql.getConnection().prepareStatement("select bodyType from resource_food_menu where url=?;");
						stmt.setString(1, url);
						res = stmt.executeQuery();
						tmp = res.getString("bodyType");
						if (tmp != null) {
							if (tmp.contains(type))
								return false;
							else {
								tmp = tmp + " " + type;
								stmt = sql.getConnection().prepareStatement("update resource_food_menu set bodyType=? where url=?;");
								stmt.setString(1, tmp);
								stmt.setString(2, url);
								stmt.executeUpdate();
							}
						}
					}
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		Data data = new Data(url, type);
		m_ExistData.add(data);
		PublicData.gNeedDeal = m_DataQueue.size();
		return m_DataQueue.offerLast(data);
	}

	// ���������ݴ�С
	public int dealCount() {
		return m_DataQueue.size();
	}

	// �Ѵ������ݵĴ�С
	public int hasDealCount() {
		return m_nHasDeal;
	}

	 /** 
	    * ����ת��λ����ƴ����Ӣ���ַ����� 
	    * @param chines ���� 
	    * @return ƴ�� 
	    */  
	    public static String converterToSpell(String chines){          
	        String pinyinName = "";  
	        char[] nameChar = chines.toCharArray();  
	        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
	        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
	        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
	        for (int i = 0; i < nameChar.length; i++) {  
	            if (nameChar[i] > 128) {  
	                try {  
	                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];  
	                } catch (BadHanyuPinyinOutputFormatCombination e) {  
	                    e.printStackTrace();  
	                }  
	            }else{  
	                pinyinName += nameChar[i];  
	            }  
	        }  
	        return pinyinName;  
	    }  
	
	private void getNetData2(String url, String type) {
		System.out.println("url��"+ url);
		// �������� ��������
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// ��Ʒ�� ���ܽ��� Ӫ����ֵ
		String food_name = null, food_info = null, food_nutrition = null;
		// ��Ч ������Ⱥ ������Ⱥ
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;
		
		// �����ʳ�Ĳ�����Ӫ����ֵ�� ��������
		if (!strRet.contains("������"))
			return;
		
		// ����ƥ��"
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		} else 
			return;
		String dishType = type;
		pat = Pattern.compile("(?=��������)([\\s\\S]*?)(?=</p>)"); // ����ʺ� ��������ģʽ
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = Html2Text(food_info).replace("��������", "");
		}
		
		pat = Pattern.compile("(?=Ӫ����ֵ</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = Html2Text(food_nutrition).replace("Ӫ����ֵ", "");;
		}
		
		pat = Pattern.compile("(?=��Ч������</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = Html2Text(food_effect).replace("��Ч������", "");
		}	

		pat = Pattern.compile("������Ⱥ</strong>��[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("������Ⱥ</strong>��", "").replace("<", "");
		}		

		pat = Pattern.compile("������Ⱥ</strong>��[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("������Ⱥ</strong>��", "").replace("<", "");
		}	
		
		// �����ѡ��Ʒ
		String food_select = null;
		pat = Pattern.compile("(?=ѡ��</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_select= mat.group();
			food_select = Html2Text(food_select).replace("ѡ��", "");
		}
		
		////////////////////
		String food_tip = "";
		pat = Pattern.compile("(?=ʳ�÷���</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_tip = mat.group();
			food_tip = Html2Text(food_tip).replace("ʳ�÷���", "");;
		}
		
		String food_save = "";
		pat = Pattern.compile("(?=�洢</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_save= mat.group();
			food_save = Html2Text(food_save).replace("�洢", "");
			
		}
	
		// ����ʳ�ܻ�ȡʳ�ĵ���������
		String urlTmp = "http://www.meishij.net/";
		try {
			urlTmp = "http://www.meishij.net/"+  java.net.URLEncoder.encode(food_name,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 
		System.out.println("urlTmp:"+ urlTmp);
		GetPostUtil.UtilParam utilParam2 = new GetPostUtil().new UtilParam();
		utilParam2.szUrl = urlTmp;
		String strRet2 = GetPostUtil.getNet(utilParam2);
		// ��ȡ�������� д����yi_  ji_
		// ����ѭ��ƥ�䷨
		String bodyTypeTmp = "";
		pat = Pattern.compile("(?=sc_header_con2)([\\s\\S]*?)(?=</ul>)");
		mat = pat.matcher(strRet2);
		String bodyType = "";
		if (mat.find()) {
			bodyTypeTmp = mat.group();
			for (int i =0; i< bodyTypes.length; ++i) {
				if (strRet2.contains(bodyTypes[i])) {
					bodyType +=" "+bodyTypeAsw[i];
				}
			}
		}
		
		System.out.println("����:"+food_info);
		System.out.println("����:"+ food_name);
		System.out.println("�洢:" + food_save);
		System.out.println("����:" + bodyType);
		System.out.println("��ѡ:"+ food_select);
		System.out.println("��ʾ:"+ food_tip);
		System.out.println("Ӫ����ֵ:" + food_nutrition);
		System.out.println("�洢:"+ food_save);
		System.out.println("Ч��:" + food_effect);
		System.out.println("������Ⱥ��"+ food_man_unsuit);
		System.out.println("������Ⱥ:"+food_man_suit);
		
		
		// ����ͼƬ http://images.meishij.net/p
		// �ҵ�ʳ��ͼƬ���� 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://static.xiangha.com/shicai/[^\"]{1,100}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "").replace("150x150", "280x220");      

			// ��ʼ����ͼƬ ���浽���� �����浽���ݿ�
			// ͼƬ��Ϊ: md5(ʳ����)
			String dirPath = IndexServlet.getRealPath() + "/dish";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();		
			String imgName = EncodeAndDecode.md5(food_name);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServlet.getRealPath(), "");
		}
		
		
		System.out.println("��ʼ����Ӫ��ֵ====");
	
		// ƥ���ʳ��Ӫ����ֵ�� ʹ��Json���洢����
		JSONObject jsonValue = new JSONObject();
		// ����
		pat = Pattern.compile("(?=����</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("����</a>���󿨣�</span><em>", "").replace("</em>", "");
			jsonValue.put("����(��)", tmp);
		} else {
			jsonValue.put("����(��)", "0");
		}
		
		// ������
		pat = Pattern.compile("(?=������</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("������</a>���ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("������(��)", tmp);
		} else {
			jsonValue.put("������(��)", "0");
		}
		
		pat = Pattern.compile("(?=̼ˮ������</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("̼ˮ������</a>���ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("̼ˮ������(��)", tmp);
		} else {
			jsonValue.put("̼ˮ������(��)", "0");
		}
		
		pat = Pattern.compile("(?=��ʳ��ά</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��ʳ��ά</a>���ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��ʳ��ά(��)", tmp);
		} else {
			jsonValue.put("��ʳ��ά(��)", "0");
		}	
		
		pat = Pattern.compile("(?=���ܲ���</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("���ܲ���</a>��΢�ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("���ܲ���(΢��)", tmp);
		} else {
			jsonValue.put("���ܲ���(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����A</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����A</a>��΢�ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����A(΢��)", tmp);
		} else {
			jsonValue.put("ά����A(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����C</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����C</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����C", tmp);
		} else {
			jsonValue.put("ά����C", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=Ҷ��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("Ҷ��</a>��΢�ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("Ҷ��(΢��)", tmp);
		} else {
			jsonValue.put("Ҷ��(΢��)", "0");
		}

		pat = Pattern.compile("(?=ά����E</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����E</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����E(����)", tmp);
		} else {
			jsonValue.put("ά����E(����)", "0");
		}	
		
		pat = Pattern.compile("(?=֬��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("֬��</a>���ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("֬��", tmp);
		} else {
			jsonValue.put("֬��", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=þ</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("þ</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("þ(����)", tmp);
		} else {
			jsonValue.put("þ(����)", "0");
		}	
		
		pat = Pattern.compile("(?=����</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("����</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("����(����)", tmp);
		} else {
			jsonValue.put("����(����)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����B1</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����B1</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����B1(����)", tmp);
		} else {
			jsonValue.put("ά����B1(����)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����B12</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����B12</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����B12(����)", tmp);
		} else {
			jsonValue.put("ά����B12(����)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����B2</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����B2</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����B2(����)", tmp);
		} else {
			jsonValue.put("ά����B2(����)", "0");
		}	
			
		pat = Pattern.compile("(?=п</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("п</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("п(����)", tmp);
		} else {
			jsonValue.put("п(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>��΢�ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(΢��)", tmp);
		} else {
			jsonValue.put("��(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ͭ</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ͭ</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ͭ(����)", tmp);
		} else {
			jsonValue.put("ͭ(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>��΢�ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(΢��)", tmp);
		} else {
			jsonValue.put("��(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����B6</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����B6</a>�����ˣ�</span><em>", "").replace("</em>", "");
			jsonValue.put("ά����B6(����)", tmp);
		} else {
			jsonValue.put("ά����B6(����)", "0");
		}	
		
		System.out.println("Ӫ����ֵ��:"+jsonValue.toString());
		
		System.out.println("��ʼ����ʳ������====");
		pat = Pattern.compile("http://www.xiangha.com/xiangke/[^\"]{1,7}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			urlTmp = mat.group().replace("\"", "");
			utilParam2.szUrl = urlTmp;
			strRet = GetPostUtil.getNet(utilParam2);
		}

		// ����ʳ��������˱� �����˼� <strong
		String dapei = "";
		JSONObject jsonBadCollo = new JSONObject();
		JSONObject jsonGoodCollo = new JSONObject();
		if (strRet.contains("���</span>") && strRet.contains("�˴�")) {

			pat = Pattern.compile("(?=���</span>)([\\s\\S]*?)(?=<th)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("<th", "").replace("���", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("��"));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("��")+1);
					jsonBadCollo.put(name, bad_fun);
				}
			}
			
			pat = Pattern.compile("(?=�˴�)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("�˴�", "").replace("�鿴����&gt;&gt;", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("��"));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("��")+1);
					jsonGoodCollo.put(name, bad_fun);
				}
			}
		} else if (strRet.contains("���</span>") && !strRet.contains("�˴�")) {
			pat = Pattern.compile("(?=���</span>)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("���", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("��"));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("��")+1);
					jsonBadCollo.put(name, bad_fun);
				}
			}
		} else if (!strRet.contains("���</span>") && strRet.contains("�˴�")) {
			pat = Pattern.compile("(?=�˴�)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("�˴�", "").replace("�鿴����&gt;&gt;", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("��"));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("��")+1);
					jsonGoodCollo.put(name, bad_fun);
				}
			}
		}
		
		System.out.println("��Ʒ����:"+ dishType);
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// ����Ʒ�������ݽ������
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_dish2(name,bodyType,introduction,nutrition,effect,man_suit"+
					",man_unsuit,save,how_select,image,value,collocation_bad, collocation_good,dishType)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, bodyType);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, food_save);
			stmt.setString(9, food_select);
			stmt.setString(10, imageUrl);
			stmt.setString(11, jsonValue.toString());
			stmt.setString(12, jsonBadCollo.toString());
			stmt.setString(13, jsonGoodCollo.toString());
			stmt.setString(14, dishType);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}

	private void getNetData(String url, String type) {

		// �������� ��������
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);
		
		// ��Ʒ�� ���ܽ��� Ӫ����ֵ
		String food_name = null, food_info = null, food_nutrition = null;
		// ��Ч ������Ⱥ ������Ⱥ
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;
	

		// ����ƥ��
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		} else 
			return;
				
		String dishType = null;
		pat = Pattern.compile(">[^<]{1,10}</a> &gt;  ");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			dishType = mat.group().replace("</a> &gt;  ", "").replace(">", "");
		}

		pat = Pattern.compile("<dd>[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = food_info.replace("<dd>", "").replace("<", "");
		}

		pat = Pattern.compile("��ҪӪ����</b><p>[^<]{1,100}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = food_nutrition.replace("��ҪӪ����</b><p>", "").replace("<", "");
		}

		pat = Pattern.compile("ʳ�ƹ�Ч��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = food_effect.replace("ʳ�ƹ�Ч��</b><p>", "").replace("<", "");
		}	

		pat = Pattern.compile("������Ⱥ��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("������Ⱥ��</b><p>", "").replace("<", "");
		}		

		pat = Pattern.compile("������Ⱥ��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("������Ⱥ��</b><p>", "").replace("<", "");
		}	

		// Ӫ���͹�Ч ʳ��ע�� �����ѡ ��α���
		String food_fun = null, food_notice=null, food_select=null;
		String food_save = null;
		
		// ƥ���Ӫ���͹�Ч������
		int n = strRet.indexOf("<p class=\"icoli1\">");
		int m = strRet.indexOf("bgc4");
		if (n != -1 && m!= -1) {
			food_fun = strRet.substring(n, m);
			food_fun = food_fun.replace("<h2 class=", "");
			food_fun = Html2Text(food_fun);
		}

		// ƥ���ע������
		n = strRet.indexOf("<p class=\"icoli2\">");
		m = strRet.indexOf("bgc6");
		if (n != -1 && m!= -1) {
		
			food_notice = strRet.substring(n, m);
			food_notice = food_notice.replace("<h2 class=", "");
			food_notice = Html2Text(food_notice);
		}
		// �����ѡ��Ʒ
		pat = Pattern.compile("</h2></div><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_select= mat.group();
			food_select = food_select.replace("</h2></div><p>", "").replace("<", "");
			
		}
	
		// ��α�����Ʒ
		pat = Pattern.compile("</h2></div><p>[^<]{1,200}<");
		mat = pat.matcher(strRet.substring(strRet.indexOf("</h2></div><p>")+ 10));
		if (mat.find()) {
			food_save= mat.group();
			food_save = food_save.replace("</h2></div><p>", "").replace("<", "");
			
		}
		
		// �ҵ�ʳ��ͼƬ���� 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.cnys.com/upload/thumb/[^\"]{1,50}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");
			
			// ��ʼ����ͼƬ ���浽���� �����浽���ݿ�
			// ͼƬ��Ϊ: md5(ʳ����)
			String dirPath = IndexServletGroup.getRealPath() + "/dish";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();
			String imgName = photo_url.substring(photo_url.lastIndexOf("/")+1, photo_url.lastIndexOf(".")-1);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServletGroup.getRealPath(), "");
		}
		
		// ƥ���ʳ��Ӫ����ֵ�� ʹ��Json���洢����
		JSONObject json = new JSONObject();
		// ������
		pat = Pattern.compile("������</a> <i>([^��]{1,10}��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("������</a> <i>(", "").replace("��)", "");
			json.put("������", tmp);
		} else {
			json.put("������", "0");
		}
		
		pat = Pattern.compile("̼ˮ������</a> <i>([^��]{1,10}��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("̼ˮ������</a> <i>(", "").replace("��)", "");
			json.put("̼ˮ������", tmp);
		} else {
			json.put("̼ˮ������", "0");
		}
		
		pat = Pattern.compile("��ʳ��ά</a> <i>([^��]{1,10}��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��ʳ��ά</a> <i>(", "").replace("��)", "");
			json.put("��ʳ��ά", tmp);
		} else {
			json.put("��ʳ��ά", "0");
		}	
		
		pat = Pattern.compile("���ܲ���</a> <i>([^΢]{1,10}΢��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("���ܲ���</a> <i>(", "").replace("΢��)", "");
			json.put("���ܲ���", tmp);
		} else {
			json.put("���ܲ���", "0");
		}	
		
		pat = Pattern.compile("ά����A</a> <i>([^΢]{1,10}΢��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����A</a> <i>(", "").replace("΢��)", "");
			json.put("ά����A", tmp);
		} else {
			json.put("ά����A", "0");
		}	
		
		pat = Pattern.compile("ά����C</a> <i>([^��]{1,10}����)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����C</a> <i>(", "").replace("����)", "");
			json.put("ά����C", tmp);
		} else {
			json.put("ά����C", "0");
		}	
		
		pat = Pattern.compile("��</a> <i>([^��]{1,10}����)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a> <i>(", "").replace("����)", "");
			json.put("��", tmp);
		} else {
			json.put("��", "0");
		}	
		
		pat = Pattern.compile("��</a> <i>([^��]{1,10}����)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a> <i>(", "").replace("����)", "");
			json.put("��", tmp);
		} else {
			json.put("��", "0");
		}	
		
		pat = Pattern.compile("��</a> <i>([^��]{1,10}����)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("��</a> <i>(", "").replace("����)", "");
			json.put("��", tmp);
		} else {
			json.put("��", "0");
		}	
		
		pat = Pattern.compile("����</a> <i>([^ǧ]{1,10}ǧ��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("����</a> <i>(", "").replace("ǧ��)", "");
			json.put("����", tmp);
		} else {
			json.put("����", "0");
		}
		// ֬��
		pat = Pattern.compile("ά����E</a> <i>([^��]{1,10}����)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("ά����E</a> <i>(", "").replace("����)", "");
			json.put("ά����E", tmp);
		} else {
			json.put("ά����E", "0");
		}	
		
		pat = Pattern.compile("֬��</a> <i>([^��]{1,10}��)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("֬��</a> <i>(", "").replace("��)", "");
			json.put("֬��", tmp);
		} else {
			json.put("֬��", "0");
		}	
		
		// ��ȡʳ����˱� json���ݱ��� �����ʽ food---reson&imageUrl;
		JSONObject jsonColloGood = new JSONObject();
		n = strRet.indexOf("icoyes");
		m = strRet.indexOf("icono");
		if (m == -1)
			m = strRet.lastIndexOf("clear20");
		String strTmp = null;
		if (m != -1 && n!= -1)
			strTmp = strRet.substring(n, m);
		if (strTmp != null) {

			pat = Pattern.compile("/upload/baike/[^\"]{1,50}\"");
			mat = pat.matcher(strTmp);
			String tmp2 = strTmp, tmp3 = strTmp;
			Pattern pat2 = Pattern.compile("��[^һ]{1,20}һ���");
			Matcher mat2 = pat2.matcher(tmp2);
			Pattern pat3 = Pattern.compile("һ��ԣ�[^<]{1,100}</p>");
			Matcher mat3 = pat3.matcher(tmp3);
			
			String dirPath = IndexServletGroup.getRealPath() + "/minidish";
			File file = new File (dirPath);
			if (file.exists() == false)
				file.mkdir();
			
			while (mat.find() && mat2.find() && mat3.find()) {

				String imgUrl = "http://www.cnys.com"+mat.group().replace("\"", "");
				String foodNam = mat2.group().replace("��", "").replace("һ���", "");
				// ��������ͼƬ baike
				String imgName = imgUrl.substring(imgUrl.lastIndexOf("/")+1, imgUrl.lastIndexOf(".")-1);
				imgUrl =  GetPostUtil.downloadImage(imgUrl, imgName, dirPath + "/");
				imgUrl = imgUrl.replace(IndexServletGroup.getRealPath(), "");
	
				String fun = mat3.group().replace("һ��ԣ�", "").replace("</p>", "");
				fun = Html2Text(fun);
				jsonColloGood.put(foodNam,new String[]{imgUrl, fun});
			}
		}

		JSONObject jsonColloBad = new JSONObject();
		n = strRet.indexOf("icono");
		m = strRet.lastIndexOf("clear20");
		strTmp = null;
		if (n != -1 && m!= -1)
			strTmp = strRet.substring(n, m);
		if (strTmp != null) {

			pat = Pattern.compile("/upload/baike/[^\"]{1,50}\"");
			mat = pat.matcher(strTmp);
			String tmp2 = strTmp, tmp3 = strTmp;
			Pattern pat2 = Pattern.compile("��[^һ]{1,20}һ���");
			Matcher mat2 = pat2.matcher(tmp2);
			Pattern pat3 = Pattern.compile("һ��ԣ�[^<]{1,500}</p>");
			Matcher mat3 = pat3.matcher(tmp3);
			String dirPath = IndexServletGroup.getRealPath() + "/minidish";
			
			while (mat.find() && mat2.find() && mat3.find()) {
			
				String imgUrl = "http://www.cnys.com"+mat.group().replace("\"", "");
				String foodNam = mat2.group().replace("��", "").replace("һ���", "");
				// ��������ͼƬ baike
				String imgName = imgUrl.substring(imgUrl.lastIndexOf("/")+1, imgUrl.lastIndexOf(".")-1);
				imgUrl =  GetPostUtil.downloadImage(imgUrl, imgName, dirPath + "/");
				imgUrl = imgUrl.replace(IndexServletGroup.getRealPath(), "");
	
				String fun = mat3.group().replace("һ��ԣ�", "").replace("", "");
				fun = Html2Text(fun);
				jsonColloBad.put(foodNam,new String[]{imgUrl, fun});
			}
		}
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// ����Ʒ�������ݽ������
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_dish(name,bodyType,introduction,nutrition,effect,man_suit"+
					",man_unsuit,fun,notice,save,how_select,image,value,collocation_good,collocation_bad,dishType,url)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, type);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, food_fun);
			stmt.setString(9, food_notice);
			stmt.setString(10, food_save);
			stmt.setString(11, food_select);
			stmt.setString(12, imageUrl);
			stmt.setString(13, json.toString());
			stmt.setString(14, jsonColloGood.toString());
			stmt.setString(15, jsonColloBad.toString());
			stmt.setString(16, dishType);
			stmt.setString(17, url);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
		/*
		// ƥ����������� ����������
		n = strRet.indexOf("<ul class=\"cai700\">");
		m = strRet.indexOf("<div class=\"ban700 b700_09\"");
		if (n != -1 && m!= -1) {
			String tmp = strRet.substring(n, m);

			pat = Pattern.compile("http://www.cnys.com/caipu/[^\"]{1,100}\"");
			mat = pat.matcher(tmp);
			while (mat.find()) {
				String urlDes = mat.group().replace("\"", "");
				downloadMenu(urlDes, type, url);
			}
		}*/
	}

	public static void downloadMenu(String url, String type, String preUrl) {
	
		// ���ز���
		// �������� ��������
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// ��Ʒ�� ���ܽ��� Ӫ����ֵ
		String food_name = null, food_info = null, food_nutrition = null;
		// ��Ч ������Ⱥ ������Ⱥ
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;

		// ����ƥ��
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		}

		pat = Pattern.compile("<p>[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = food_info.replace("<p>", "").replace("<", "");
		}

		pat = Pattern.compile("Ӫ����ֵ��</b><p>[^<]{1,100}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = food_nutrition.replace("Ӫ����ֵ��</b><p>", "").replace("<", "");
		}

		pat = Pattern.compile("ʳ�ƹ�Ч��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = food_effect.replace("ʳ�ƹ�Ч��</b><p>", "").replace("<", "");
		}	

		pat = Pattern.compile("������Ⱥ��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("������Ⱥ��</b><p>", "").replace("<", "");
		}		

		pat = Pattern.compile("������Ⱥ��</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("������Ⱥ��</b><p>", "").replace("<", "");
		}	

		// ��Ч������
		int n = strRet.indexOf("<p class=\"icoli1\">");
		int m = strRet.indexOf("bgc2");
		String food_fun  = null;
		if (n != -1 && m!= -1) {
			food_fun = strRet.substring(n, m);
			food_fun = food_fun.replace("<h2 class=\"", "");
			food_fun = Html2Text(food_fun);
		}
		// ����
		n = strRet.indexOf("<p class=\"icoli2\">");
		m = strRet.indexOf("bgc3");
		String how_make  = null;
		if (n != -1 && m!= -1) {
			how_make = strRet.substring(n, m);
			how_make = how_make.replace("<h2 class=\"", "");
			how_make = Html2Text(how_make);
		}
		// ����Ҫ�Ĳ��� ��Ҫʳ�� ��������
		String material_main = null, material_assist = null;
		n = strRet.indexOf("��Ҫʳ��");
		m = strRet.indexOf("��������"); // bgc4
		if ( m== -1)
			m = strRet.indexOf("bgc4");
		material_main = strRet.substring(n, m).replace("<h2 class=\"", "");
		material_main = Html2Text(material_main).replace("��Ҫʳ�ġ�", "").replace(")", ")  ");
		
		if (-1 != strRet.indexOf("��������")) {
			
			n = m;
			m = strRet.indexOf("bgc4");
			material_assist = strRet.substring(n, m).replace("<h2 class=\"", "");;
			material_assist = Html2Text(material_assist).replace("�������ϡ�", "").replace(")", ")  ");
		}
		
		// ����ͼƬ
		// �ҵ�ʳ��ͼƬ���� 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.cnys.com/upload/thumb/[^\"]{1,50}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");
			
			// ��ʼ����ͼƬ ���浽���� �����浽���ݿ�
			// ͼƬ��Ϊ: ԭ������
			String dirPath = IndexServletGroup.getRealPath() + "/menu";
			File file = new File(dirPath);
			if (file.exists() == false)
				file.mkdir();
			
			String imgName = photo_url.substring(photo_url.lastIndexOf("/"), photo_url.lastIndexOf(".")-1);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServletGroup.getRealPath(), "");
		}
		// �������ݿ�
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into resource_food_menu(name,bodyType,introduction,nutrition,"
					+"effect,man_suit,man_unsuit,material_main,material_assist,how_make,"
					+"fun,image) values(?,?,?,?,?,?,?,?,?,?,?,?);");
			stmt.setString(1, food_name);
			stmt.setString(2, type);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, material_main);
			stmt.setString(9, material_assist);
			stmt.setString(10, how_make);
			stmt.setString(11, food_fun);
			stmt.setString(12, imageUrl);
			stmt.executeUpdate();
			sql.closeSql();
		} catch(SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}

	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // ��html��ǩ���ַ���
		htmlStr = htmlStr.replace("<br/>", "\n");
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // ����script��������ʽ{��<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // ����style��������ʽ{��<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // ����HTML��ǩ��������ʽ

			// ����ƥ�� <strong> </span>
			p_script = Pattern.compile("</strong>");
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("");

			p_script = Pattern.compile("</span>");
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("��");

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // ����script��ǩ

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // ����style��ǩ

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // ����html��ǩ

			textStr = htmlStr.replace("\t", "").replace("\n", "").replace("&nbsp;", " ");
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// �����ı��ַ���
	}
	
	static void addLog(String log) {
		
		PublicData.gLogError += log +"<br>";
	}
	
	
	private void getMenuData(String url, String type) {
		System.out.println("url��"+ url);
		// �������� ��������
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// ��Ʒ�� ���ܽ��� Ӫ����ֵ
		String food_name = null;
		// ��Ч ������Ⱥ ������Ⱥ
		String food_effect = null;
	
		// ����ƥ��"
		Pattern pat = Pattern.compile("foodName\">[^<]{1,30}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("foodName\">", "").replace("<", "");
		} else 
			return;
		String dishType = type;

		pat = Pattern.compile("(?=��Ч</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = Html2Text(food_effect).replace("��Ч", "");
			if(food_effect.contains("����"))
				food_effect= "";
		}	

		String main_food = "", assist_food="";
		pat = Pattern.compile("(?=����</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			main_food = mat.group();
			main_food = Html2Text(main_food).replace("����", "");
		}	

		pat = Pattern.compile("(?=����</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			assist_food = mat.group();
			assist_food = Html2Text(assist_food).replace("����", "");
		}	
		
		if (assist_food==""&&main_food == "") {
			pat = Pattern.compile("(?=ԭ��</h3>)([\\s\\S]*?)(?=</div>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				main_food = mat.group();
				main_food = Html2Text(main_food).replace("ԭ��", "");
			}	
			
		}
		
		String how_make = "";
		pat = Pattern.compile("(?=����</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			how_make = mat.group();
			how_make = Html2Text(how_make).replace("����", "");
		}	
		
		String flavor =""; // ��ζ
		pat = Pattern.compile("��ζ��[^<]{1,20}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			flavor = mat.group();
			flavor = flavor.replace("��ζ��", "").replace("<", "");
		}	
		
		System.out.println("����:"+ food_name);
		System.out.println("Ч��:" + food_effect);
		System.out.println("����:"+ main_food);
		System.out.println("����:" + assist_food);
		System.out.println("����:" + how_make);	
		System.out.println("��ζ:"+ flavor);
		
		// �ҵ�ʳ��ͼƬ���� 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.ilinkee.com/img/images/iLinkeeImage_mid/[^\"]{1,100}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");      

			// ��ʼ����ͼƬ ���浽���� �����浽���ݿ�
			// ͼƬ��Ϊ: md5(ʳ����)
			String dirPath = IndexServlet.getRealPath() + "/menu";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();		
			String imgName = EncodeAndDecode.md5(food_name);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServlet.getRealPath(), "");
		}
		
		
		System.out.println("��ʼ����Ӫ��ֵ====");
	
		// ƥ���ʳ��Ӫ����ֵ�� ʹ��Json���洢����
		JSONObject jsonValue = new JSONObject();
		// ����
		pat = Pattern.compile("(?=����</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("����</a>(��)", "")).replace(" ", "");
			jsonValue.put("����(��)", tmp);
		} else {
			jsonValue.put("����(��)", "0");
		}
		
		// ������
		pat = Pattern.compile("(?=������)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("������</a>(��)", "")).replace(" ", "");
			jsonValue.put("������(��)", tmp);
		} else {
			jsonValue.put("������(��)", "0");
		}
		
		pat = Pattern.compile("(?=̼ˮ������</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("̼ˮ������</a>(��)", "")).replace(" ", "");
			jsonValue.put("̼ˮ������(��)", tmp);
		} else {
			jsonValue.put("̼ˮ������(��)", "0");
		}
		
		pat = Pattern.compile("(?=��ʳ��ά</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��ʳ��ά</a>(��)", "")).replace(" ", "");
			jsonValue.put("��ʳ��ά(��)", tmp);
		} else {
			jsonValue.put("��ʳ��ά(��)", "0");
		}	
		
		pat = Pattern.compile("(?=���ܲ���</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("���ܲ���</a>(΢��)", "")).replace(" ", "");
			jsonValue.put("���ܲ���(΢��)", tmp);
		} else {
			jsonValue.put("���ܲ���(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����A</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("ά����A</a>(΢��)", "")).replace(" ", "");
			jsonValue.put("ά����A(΢��)", tmp);
		} else {
			jsonValue.put("ά����A(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ά����C</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("ά����C</a>(����)", "")).replace(" ", "");
			jsonValue.put("ά����C", tmp);
		} else {
			jsonValue.put("ά����C", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=Ҷ��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("Ҷ��</a>(����)", "")).replace(" ", "");
			jsonValue.put("Ҷ��(΢��)", tmp);
		} else {
			jsonValue.put("Ҷ��(΢��)", "0");
		}

		pat = Pattern.compile("(?=ά����E</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("ά����E</a>(����)", "")).replace(" ", "");
			jsonValue.put("ά����E(����)", tmp);
		} else {
			jsonValue.put("ά����E(����)", "0");
		}	
		
		pat = Pattern.compile("(?=֬��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("֬��</a>(��)", "")).replace(" ", "");
			jsonValue.put("֬��", tmp);
		} else {
			jsonValue.put("֬��", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=þ</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("þ</a>(����)", "")).replace(" ", "");
			jsonValue.put("þ(����)", tmp);
		} else {
			jsonValue.put("þ(����)", "0");
		}	
		
		pat = Pattern.compile("(?=����</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("����</a>(����)", "")).replace(" ", "");
			jsonValue.put("����(����)", tmp);
		} else {
			jsonValue.put("����(����)", "0");
		}			
			
		pat = Pattern.compile("(?=п</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("п</a>(����)", "")).replace(" ", "");
			jsonValue.put("п(����)", tmp);
		} else {
			jsonValue.put("п(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(΢��)", "")).replace(" ", "");
			jsonValue.put("��(΢��)", tmp);
		} else {
			jsonValue.put("��(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=ͭ</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("ͭ</a>(����)", "")).replace(" ", "");
			jsonValue.put("ͭ(����)", tmp);
		} else {
			jsonValue.put("ͭ(����)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(΢��)", "")).replace(" ", "");
			jsonValue.put("��(΢��)", tmp);
		} else {
			jsonValue.put("��(΢��)", "0");
		}	
		
		pat = Pattern.compile("(?=��</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("��</a>(����)", "")).replace(" ", "");
			jsonValue.put("��(����)", tmp);
		} else {
			jsonValue.put("��(����)", "0");
		}	
		
		System.out.println("Ӫ����ֵ��:"+jsonValue.toString());
		System.out.println("��Ʒ����:"+ dishType);
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// ����Ʒ�������ݽ������
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_menu2(name, material_main,material_assist, effect, value, how_make,image, flavor)"
					+ " values(?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, main_food);
			stmt.setString(3, assist_food);
			stmt.setString(4, food_effect);
			stmt.setString(5, jsonValue.toString());
			stmt.setString(6, how_make);
			stmt.setString(7, imageUrl);
			stmt.setString(8, flavor);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
}








