package com.app.cloud.Activity;

import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 体质测试之后
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

		initToolbar("我的体质");
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
		cha.setText("身体倍儿棒，吃嘛嘛香，再加上睡眠好、性格开朗，社会和自然适应能力强，典型的平和体质。此体质的人不爱得病。");
		adj.setText("吃得不要过饱，也不能过饥，不吃冷也不吃得过热。多吃五谷杂粮、蔬菜瓜果，少食过于油腻及辛辣之物。运动上，年轻人可选择跑步、打球，老年人则适当散步、打太极拳。");
	}

	private void typeB() {
		cha.setText("说话没劲，经常出虚汗，容易呼吸短促，经常疲乏无力，这就是气虚体质。这种人容易感冒，生病后抗病能力弱且难以痊愈，还易患内脏下垂比如胃下垂等。");
		adj.setText("多吃具有益气健脾的食物，如黄豆、白扁豆、香菇、大枣、桂圆、蜂蜜等。以柔缓运动，散步、打太极拳等为主，平时可按摩足三里穴。常自汗、感冒者可服玉屏风散预防。");
	}

	private void typeC() {
		cha.setText("总是手脚发凉，不敢吃凉的东西。性格多沉静、内向。这些属阳虚体质。");
		adj.setText("可多吃甘温益气的食物，比如葱、姜、蒜、花椒、韭菜、辣椒、胡椒等。少食生冷寒凉食物如黄瓜、藕、梨、西瓜等。自行按摩气海、足三里、涌泉等穴位，或经常灸足三里、关元。可服金匮肾气丸。");
	}

	private void typeD() {
		cha.setText("如果怕热，经常感到手脚心发热，面颊潮红或偏红，皮肤干燥，口干舌燥，容易失眠，经常大便干结，那就是阴虚体质。");
		adj.setText("多吃甘凉滋润的食物，比如绿豆、冬瓜、芝麻、百合等。少食性温燥烈的食物。中午保持一定的午休时间。避免熬夜、剧烈运动，锻炼时要控制出汗量，及时补充水分。，可酌情服用六味地黄丸、杞菊地黄丸。");
	}

	private void typeE() {
		cha.setText("心宽体胖是最大特点，腹部松软肥胖，皮肤出油，汗多，眼睛浮肿，容易困倦。");
		adj.setText("饮食清淡，多食葱、蒜、海藻、海带、冬瓜、萝卜、金橘、芥末等食物，少食肥肉及甜、黏、油腻食物。可服用化痰祛湿方");
	}

	private void typeF() {
		cha.setText("脸部和鼻尖总是油光发亮，还容易生粉刺、疮疖，一开口就能闻到异味，属于湿热体质。这种人还容易大便黏滞不爽，小便发黄。");
		adj.setText(
				"饮食清淡，多吃甘寒、甘平的食物如绿豆、空心菜、苋菜、芹菜、黄瓜、冬瓜、藕、西瓜等。少食辛温助热的食物。戒除烟酒。不要熬夜、过于劳累。适合中长跑、游泳、爬山、各种球类、武术等运动。日常可服六一散、清胃散、甘露消毒丹。");
	}

	private void typeG() {
		cha.setText("刷牙时牙龈易出血，眼睛常有红丝，皮肤常干燥、粗糙，常常出现疼痛，容易烦躁，健忘，性情急躁。");
		adj.setText("可多食黑豆、海带、紫菜、萝卜、胡萝卜、山楂、醋、绿茶等具有活血、散结、行气、疏肝解郁作用的食物，少食肥猪肉等，并保持足够的睡眠。可服用桂枝茯苓丸等。");
	}

	private void typeH() {
		cha.setText("多愁善感、忧郁脆弱的气郁体质，一般比较瘦，经常闷闷不乐，无缘无故地叹气，容易心慌失眠。");
		adj.setText("多吃小麦、葱、蒜、海带、海藻、萝卜、金橘、山楂等具有行气、解郁、消食、醒神的食物。睡前避免饮茶、咖啡等提神醒脑的饮料。可以服用逍遥散、舒肝和胃丸、开胸顺气丸、柴胡疏肝散、越鞠丸调节。");
	}

	private void typeI() {
		cha.setText("（过敏体质）对花粉或某食物过敏等，在中医上这就被称为特禀体质。");
		adj.setText("饮食清淡、均衡，粗细搭配适当，荤素配伍合理。少食荞麦、蚕豆、白扁豆、牛肉、鹅肉、茄子、浓茶等辛辣之品、腥膻发物及含致敏物质的食物。可服玉屏风散、消风散、过敏煎等。");
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
