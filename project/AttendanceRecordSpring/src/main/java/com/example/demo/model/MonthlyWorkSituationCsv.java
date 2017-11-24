package com.example.demo.model;

import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"年", "月", "ユーザー名", "日付", "勤務状況", "勤務開始時間", "勤務終了時間", "休憩時間", "勤務時間", "残業時間", "総勤務時間", "総残業時間"})
public class MonthlyWorkSituationCsv {

	@JsonProperty("年")
	private int year;
	@JsonProperty("月")
	private int month;
	@JsonProperty("ユーザー名")
	private String name;
	@JsonProperty("日付")
	private int date;
	@JsonProperty("勤務状況")
	private String workSituation;
	@JsonProperty("勤務開始時間")
	private Time workStart;
	@JsonProperty("勤務終了時間")
	private Time workEnd;
	@JsonProperty("休憩時間")
	private Time breakTime;
	@JsonProperty("勤務時間")
	private Time workTime;
	@JsonProperty("残業時間")
	private Time overtime;
	@JsonProperty("総勤務時間")
	private String totalWorkTime;
	@JsonProperty("総残業時間")
	private String totalOvertime;

	public MonthlyWorkSituationCsv() {}

	public MonthlyWorkSituationCsv(int year, int month, int date, String name, String workSituation, Time workStart, Time workEnd, Time breakTime, Time workTime, Time overtime, String totalWorkTime, String totalOvertime) {
		this.year = year;
		this.month = month;
		this.date = date;
		this.name = name;
		this.workSituation = workSituation;
		this.workStart = workStart;
		this.workEnd = workEnd;
		this.breakTime = breakTime;
		this.workTime = workTime;
		this.overtime = overtime;
		this.totalWorkTime = totalWorkTime;
		this.totalOvertime = totalOvertime;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getWorkSituation() {
		return workSituation;
	}

	public void setWorkSituation(String workSituation) {
		this.workSituation = workSituation;
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

	public String getTotalWorkTime() {
		return totalWorkTime;
	}

	public void setTotalWorkTime(String totalWorkTime) {
		this.totalWorkTime = totalWorkTime;
	}

	public String getTotalOvertime() {
		return totalOvertime;
	}

	public void setTotalOvertime(String totalOvertime) {
		this.totalOvertime = totalOvertime;
	}

}
