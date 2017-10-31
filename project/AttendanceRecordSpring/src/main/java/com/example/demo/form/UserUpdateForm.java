package com.example.demo.form;

import org.hibernate.validator.constraints.NotEmpty;

public class UserUpdateForm {

	private int id;
	private String password;
	private String passwordConf;
	@NotEmpty(message = "・ユーザー名を入力してください")
	private String name;
	@NotEmpty(message = "・生年月日を入力してください")
	private String birthDate;
	private String position;

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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
