package com.kmfex;

public class TradeTimeVO {
	private String tip;
	private boolean hasTradeTime;//是否有设定交易时间参数
	private boolean inTradeTime;//设定交易时间参数了，当前时间是否在设定的交易时间范围内
	public void setHasTradeTime(boolean hasTradeTime) {
		this.hasTradeTime = hasTradeTime;
	}
	public boolean isHasTradeTime() {
		return hasTradeTime;
	}
	public void setInTradeTime(boolean inTradeTime) {
		this.inTradeTime = inTradeTime;
	}
	public boolean isInTradeTime() {
		return inTradeTime;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getTip() {
		return tip;
	}
}
