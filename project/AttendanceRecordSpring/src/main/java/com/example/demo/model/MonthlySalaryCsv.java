package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"年月", "ユーザー名", "月給"})
public class MonthlySalaryCsv {
	@JsonProperty("年月")
	private String yearAndMonth;
	@JsonProperty("ユーザー名")
	private String name;
	@JsonProperty("月給")
	private String monthlrSalary;

	public MonthlySalaryCsv() {}

	public MonthlySalaryCsv(String yearAndMonth, String name, String monthlrSalary) {
		this.yearAndMonth = yearAndMonth;
		this.name = name;
		this.monthlrSalary = monthlrSalary;
	}

	public MonthlySalaryCsv(String name, String monthlrSalary) {
		this.name = name;
		this.monthlrSalary = monthlrSalary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMonthlrSalary() {
		return monthlrSalary;
	}

	public void setMonthlrSalary(String monthlrSalary) {
		this.monthlrSalary = monthlrSalary;
	}

	public String getYearAndMonth() {
		return yearAndMonth;
	}

	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}



}
