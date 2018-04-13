package com.senyoboss.common;

public class ConditionFilter {

	// 字段名
	private String filedName;
	// 运算符
	private String operatorName;
	// 逻辑运算
	private String logicName;
	// 字段值
	private String searchValue;
	// 值类型区分  1:字符串 0:非字符串
	private Integer flg;
	
	public String getFiledName() {
		return filedName;
	}
	
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	
	public String getOperatorName() {
		return operatorName;
	}
	
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	public String getLogicName() {
		return logicName;
	}
	
	public void setLogicName(String logicName) {
		this.logicName = logicName;
	}
	
	public String getSearchValue() {
		return searchValue;
	}
	
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public Integer getFlg() {
		return flg;
	}
	
	public void setFlg(Integer flg) {
		this.flg = flg;
	}
}
