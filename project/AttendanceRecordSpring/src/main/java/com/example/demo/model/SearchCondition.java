package com.example.demo.model;

public class SearchCondition {
	/** 検索キー */
	private String searchKey;
	/** 検索値 */
	private String searchValue;
	/** 検索条件 **/
	private int searchCondition;
	/** And条件かどうか(true:and false:or) **/
	private boolean isAnd;

	/**
 	 * 引数3つのコンストラクタの場合は、自動的にAnd条件として作成
 	 * @param key
 	 * @param val
 	 * @param condition
 	 */
 	public SearchCondition(String key, String val, int condition) {
 		this.searchKey = key;
 		this.searchValue = val;
 		this.searchCondition = condition;
 		this.isAnd = true;
 	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public int getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(int searchCondition) {
		this.searchCondition = searchCondition;
	}

	public boolean getisAnd() {
		return isAnd;
	}

	public void setisAnd(boolean isAnd) {
		this.isAnd = isAnd;
	}

	public boolean isAnd() {
		return isAnd;
	}

	public void setAnd(boolean isAnd) {
		this.isAnd = isAnd;
	}

}
