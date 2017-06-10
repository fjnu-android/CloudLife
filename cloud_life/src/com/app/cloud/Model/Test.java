package com.app.cloud.Model;

/**
 * 体质测试 模型
 *
 */
public class Test {

	// 问题
	private String question;
	// 得分
	private int score = 0;

	public Test(String text) {
		this.question = text;
	}

	public void setQuestion(String test) {
		this.question = test;
	}

	public String getQuestion() {
		return question;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

}
