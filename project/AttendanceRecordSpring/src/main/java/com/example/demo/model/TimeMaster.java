package com.example.demo.model;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class TimeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Time workStart;
	private Time workEnd;
	private Time breakTime;
	private Time workTime;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

}
