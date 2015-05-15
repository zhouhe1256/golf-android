package com.bjcathay.golf.model;

public class SortModel {

	private String name;   //显示的数据?
	private String sortLetters;  //显示数据拼音的首字母
    private boolean  status; //是否是注册用户

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
