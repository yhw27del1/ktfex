package com.kmfex.model;

import java.util.Date;

public class PayMGroup {
	private Date date;
	private int group;

	public PayMGroup(Date date, int group) {
		this.date = date;
		this.group = group;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}