package com.example.demo.form;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginScreenForm {

	@NotEmpty(message = "ログインIDが入力されていません")
	private String loginId;

	@NotEmpty(message = "パスワードが入力されていません")
	private String password;

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

}
