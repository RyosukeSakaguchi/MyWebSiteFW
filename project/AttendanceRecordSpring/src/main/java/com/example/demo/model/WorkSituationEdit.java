package com.example.demo.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class WorkSituationEdit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String loginId;
	private Timestamp editTime;
	private String editContent;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getEditContent() {
		return editContent;
	}
	public void setEditContent(String editContent) {
		this.editContent = editContent;
	}
	public Timestamp getEditTime() {
		return editTime;
	}
	public String getFormatEditTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
		return sdf.format(this.editTime);
	}
	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}

}
