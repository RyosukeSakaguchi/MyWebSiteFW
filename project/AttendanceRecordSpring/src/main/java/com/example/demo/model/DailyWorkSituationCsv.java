package com.example.demo.model;

import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"年月日", "ユーザー名", "勤務状況", "勤務開始時間", "勤務終了時間", "休憩時間", "勤務時間", "残業時間",})
public class DailyWorkSituationCsv {
	@JsonProperty("年月日")
	private String yearAndMonthAndDate;
	@JsonProperty("ユーザー名")
	private String name;
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

	public DailyWorkSituationCsv() {}

	public DailyWorkSituationCsv(String yearAndMonthAndDate, String name, String workSituation, Time workStart, Time workEnd, Time breakTime, Time workTime, Time overtime) {
		this.yearAndMonthAndDate = yearAndMonthAndDate;
		this.name = name;
		this.workSituation = workSituation;
		this.workStart = workStart;
		this.workEnd = workEnd;
		this.breakTime = breakTime;
		this.workTime = workTime;
		this.overtime = overtime;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getYearAndMonthAndDate() {
		return yearAndMonthAndDate;
	}

	public void setYearAndMonthAndDate(String yearAndMonthAndDate) {
		this.yearAndMonthAndDate = yearAndMonthAndDate;
	}



}
