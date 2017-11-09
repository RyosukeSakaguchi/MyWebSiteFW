package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class WorkSituation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String loginId;
	private Date createDate;
	private String workSitu;
	private Time workStart;
	private Time workEnd;
	private Time breakTime;
	private Time workTime;
	private Time overtime;

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

	public Date getCreateDate() {
		return createDate;
	}

	public String getFormatCreateDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(this.createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getWorkSitu() {
		return workSitu;
	}

	public void setWorkSitu(String workSitu) {
		this.workSitu = workSitu;
	}

	public Time getWorkStart() {
		return workStart;
	}

	public void setWorkStart(Time workStart) {
		this.workStart = workStart;
	}

	public Time getWorkEnd() {
		return workEnd;
	}

	public void setWorkEnd(Time workEnd) {
		this.workEnd = workEnd;
	}

	public Time getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(Time breakTime) {
		this.breakTime = breakTime;
	}

	public Time getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Time workTime) {
		this.workTime = workTime;
	}

	public Time getOvertime() {
		return overtime;
	}

	public void setOvertime(Time overtime) {
		this.overtime = overtime;
	}

}
