package com.app.cloud.Activity;

import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ���ʲ���֮��
 *
 */
public class ATestActivity extends BaseUiAuth implements View.OnClickListener {

	TextView body;
	TextView cha;
	TextView adj;
	Button retest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atest);

		initToolbar("�ҵ�����");
		initView();
		initChange();
	}

	private void initView() {

		body = (TextView) findViewById(R.id.at_body);
		cha = (TextView) findViewById(R.id.at_cha);
		adj = (TextView) findViewById(R.id.at_adj);
		retest = (Button) findViewById(R.id.at_retest);
		retest.setOnClickListener(this);
	}

	private void initChange() {
		body.setText(customer.getBodytype());
		String type = customer.getBodyType();
		switch (type) {
		case "A":
			typeA();
			break;
		case "B":
			typeB();
			break;
		case "C":
			typeC();
			break;
		case "D":
			typeD();
			break;
		case "E":
			typeE();
			break;
		case "F":
			typeF();
			break;
		case "G":
			typeG();
			break;
		case "H":
			typeH();
			break;
		case "I":
			typeI();
			break;
		}
	}

	private void typeA() {
		cha.setText("���屶�������������㣬�ټ���˯�ߺá��Ը��ʣ�������Ȼ��Ӧ����ǿ�����͵�ƽ�����ʡ������ʵ��˲����ò���");
		adj.setText("�Եò�Ҫ������Ҳ���ܹ�����������Ҳ���Եù��ȡ��������������߲˹Ϲ�����ʳ�������弰����֮��˶��ϣ������˿�ѡ���ܲ����������������ʵ�ɢ������̫��ȭ��");
	}

	private void typeB() {
		cha.setText("˵��û�����������麹�����׺����̴٣�����ƣ��������������������ʡ����������׸�ð�������󿹲�������������Ȭ�������׻������´�����θ�´��ȡ�");
		adj.setText("��Ծ���������Ƣ��ʳ���ƶ����ױⶹ���㹽�����桢��Բ�����۵ȡ����Ỻ�˶���ɢ������̫��ȭ��Ϊ����ƽʱ�ɰ�Ħ������Ѩ�����Ժ�����ð�߿ɷ�������ɢԤ����");
	}

	private void typeC() {
		cha.setText("�����ֽŷ��������ҳ����Ķ������Ը�������������Щ���������ʡ�");
		adj.setText("�ɶ�Ը���������ʳ�����С������⡢�������²ˡ������������ȡ���ʳ���亮��ʳ����ƹϡ�ź���桢���ϵȡ����а�Ħ�����������ӿȪ��Ѩλ���򾭳����������Ԫ���ɷ����������衣");
	}

	private void typeD() {
		cha.setText("������ȣ������е��ֽ��ķ��ȣ���ճ����ƫ�죬Ƥ������ڸ��������ʧ�ߣ��������ɽᣬ�Ǿ����������ʡ�");
		adj.setText("��Ը��������ʳ������̶������ϡ�֥�顢�ٺϵȡ���ʳ�������ҵ�ʳ����籣��һ��������ʱ�䡣���ⰾҹ�������˶�������ʱҪ���Ƴ���������ʱ����ˮ�֡��������������ζ�ػ��衢轾յػ��衣");
	}

	private void typeE() {
		cha.setText("�Ŀ�����������ص㣬����������֣�Ƥ�����ͣ����࣬�۾����ף��������롣");
		adj.setText("��ʳ�嵭����ʳ�С��⡢���塢���������ϡ��ܲ������١���ĩ��ʳ���ʳ���⼰��𤡢����ʳ��ɷ��û�̵��ʪ��");
	}

	private void typeF() {
		cha.setText("�����ͱǼ������͹ⷢ�������������۴̡����ܣ�һ���ھ����ŵ���ζ������ʪ�����ʡ������˻����״����Ͳ�ˬ��С�㷢�ơ�");
		adj.setText(
				"��ʳ�嵭����Ըʺ�����ƽ��ʳ�����̶������Ĳˡ��Ȳˡ��۲ˡ��ƹϡ����ϡ�ź�����ϵȡ���ʳ�������ȵ�ʳ�����̾ơ���Ҫ��ҹ���������ۡ��ʺ��г��ܡ���Ӿ����ɽ���������ࡢ�������˶����ճ��ɷ���һɢ����θɢ����¶��������");
	}

	private void typeG() {
		cha.setText("ˢ��ʱ�����׳�Ѫ���۾����к�˿��Ƥ��������ֲڣ�����������ʹ�����׷��꣬���������鼱�ꡣ");
		adj.setText("�ɶ�ʳ�ڶ����������ϲˡ��ܲ������ܲ���ɽ髡��ס��̲�Ⱦ��л�Ѫ��ɢ�ᡢ��������ν������õ�ʳ���ʳ������ȣ��������㹻��˯�ߡ��ɷ��ù�֦������ȡ�");
	}

	private void typeH() {
		cha.setText("����ƸС������������������ʣ�һ��Ƚ��ݣ��������Ʋ��֣���Ե�޹ʵ�̾���������Ļ�ʧ�ߡ�");
		adj.setText("���С�󡢴С��⡢���������塢�ܲ������١�ɽ髵Ⱦ�����������������ʳ�������ʳ�˯ǰ�������衢���ȵ��������Ե����ϡ����Է�����ңɢ����κ�θ�衢����˳���衢������ɢ��Խ������ڡ�");
	}

	private void typeI() {
		cha.setText("���������ʣ��Ի��ۻ�ĳʳ������ȣ�����ҽ����ͱ���Ϊ�������ʡ�");
		adj.setText("��ʳ�嵭�����⣬��ϸ�����ʵ����������������ʳ���󡢲϶����ױⶹ��ţ�⡢���⡢���ӡ�Ũ�������֮Ʒ���������Ｐ���������ʵ�ʳ��ɷ�������ɢ������ɢ��������ȡ�");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.at_retest:
			this.forward(BTestActivity.class);
			break;
		}
	}

}
