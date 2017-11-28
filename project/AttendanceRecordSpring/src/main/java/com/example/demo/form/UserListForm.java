package com.example.demo.form;

public class UserListForm {

	private String loginId;
	private String name;
	private String position;
	private String birthDateFrom;
	private String birthDateTo;
	private String workSituation;
	private int pageNumber;
	private int[] delListId;

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getBirthDateFrom() {
		return birthDateFrom;
	}
	public void setBirthDateFrom(String birthDateFrom) {
		this.birthDateFrom = birthDateFrom;
	}
	public String getBirthDateTo() {
		return birthDateTo;
	}
	public void setBirthDateTo(String birthDateTo) {
		this.birthDateTo = birthDateTo;
	}
	public String getWorkSituation() {
		return workSituation;
	}
	public void setWorkSituation(String workSituation) {
		this.workSituation = workSituation;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int[] getDelListId() {
		return delListId;
	}
	public void setDelListId(int[] delListId) {
		this.delListId = delListId;
	}
}
