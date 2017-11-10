package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ユーザー名", "月給"})
public class MonthlySalary {
	@JsonProperty("ユーザー名")
	private String name;
	@JsonProperty("月給")
	private int monthlrSalary;

	public MonthlySalary() {}

	public MonthlySalary(String name, int monthlrSalary) {
		this.name = name;
		this.monthlrSalary = monthlrSalary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMonthlrSalary() {
		return monthlrSalary;
	}

	public void setMonthlrSalary(int monthlrSalary) {
		this.monthlrSalary = monthlrSalary;
	}



}
