package com.example.demo.form;

import org.hibernate.validator.constraints.NotEmpty;

public class SignUpForm {

	@NotEmpty(message = "・ログインIDを入力してください")
	private String loginId;
	@NotEmpty(message = "・パスワードを入力してください")
	private String password;
	@NotEmpty(message = "・パスワード(確認)を入力してください")
	private String passwordConf;
	@NotEmpty(message = "・ユーザー名を入力してください")
	private String name;
	@NotEmpty(message = "・生年月日を入力してください")
	private String birthDate;
	private String position;

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConf() {
		return passwordConf;
	}
	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}


}
