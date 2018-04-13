package com.senyoboss.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.senyoboss.tool.StringUtils;

public class SearchCondition {

	// 表名
	private String tableName;
	// 过滤条件
	private List<ConditionFilter> filters = new ArrayList<ConditionFilter>();
	// 当前页
	private Integer page;
	// 每页显示条数
	private Integer pageSize;
	// 查询语句
	private String searchSql;

	public SearchCondition(String tableName) {

		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ConditionFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ConditionFilter> filters) {
		this.filters = filters;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearchSql() throws UnsupportedEncodingException {

		if (StringUtils.isNullOrEmpty(this.searchSql)) {
			String sql = "from (SELECT @rownum:=0) r," + this.tableName + " where 0=0";
			if (this.filters.size() != 0) {
				for (int i = 0; i < this.filters.size(); i++) {
					ConditionFilter cf = this.filters.get(i);
					if (!StringUtils.isNullOrEmpty(cf.getFiledName())
							&& !StringUtils.isNullOrEmpty(cf.getOperatorName())
							&& !StringUtils.isNullOrEmpty(cf.getSearchValue())
							&& cf.getFlg() != null) {
						if (cf.getFlg() == 1) {
							if (ConditionOperator.AND.equals(cf.getLogicName())) {
								if (ConditionOperator.LIKE.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'%" + cf.getSearchValue()
											+ "%' ";
								} else if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");
									if (cf.getSearchValue().indexOf("|") == 0) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= date_add('" + s[0]
												+ "',INTERVAL 1 DAY)";
									} else if (cf.getSearchValue().lastIndexOf(
											"|") == cf.getSearchValue()
											.length() - 1) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= '" + s[0] + "'";
									} else {
										sql = sql + " and " + cf.getFiledName()
												+ " between '" + s[0]
												+ "' and date_add('" + s[1]
												+ "',INTERVAL 1 DAY)";
									}
								} else {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'" + cf.getSearchValue() + "' ";
								}
							} else if (ConditionOperator.OR.equals(cf
									.getLogicName())) {
								if (ConditionOperator.LIKE.equals(cf
										.getOperatorName())) {
									sql = sql + " or " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'%" + cf.getSearchValue()
											+ "%' ";
								} else if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " or " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");
									if (cf.getSearchValue().indexOf("|") == 0) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= date_add('" + s[0]
												+ "',INTERVAL 1 DAY)";
									} else if (cf.getSearchValue().lastIndexOf(
											"|") == cf.getSearchValue()
											.length() - 1) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= '" + s[0] + "'";
									} else {
										sql = sql + " or " + cf.getFiledName()
												+ " between '" + s[0]
												+ "' and date_add('" + s[1]
												+ "',INTERVAL 1 DAY)";
									}
								} else {
									sql = sql + " or " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'" + cf.getSearchValue() + "' ";
								}
							} else {
								if (ConditionOperator.LIKE.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'%" + cf.getSearchValue()
											+ "%' ";
								} else if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");

									if (cf.getSearchValue().indexOf("|") == 0) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= date_add('" + s[0]
												+ "',INTERVAL 1 DAY)";
									} else if (cf.getSearchValue().lastIndexOf(
											"|") == cf.getSearchValue()
											.length() - 1) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= '" + s[0] + "'";
									} else {
										sql = sql + " and " + cf.getFiledName()
												+ " between '" + s[0]
												+ "' and date_add('" + s[1]
												+ "',INTERVAL 1 DAY)";
									}
								} else {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ "'" + cf.getSearchValue() + "' ";
								}
							}
						} else {
							if (ConditionOperator.AND.equals(cf.getLogicName())) {
								if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");
									if ("".equals(s[0])) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= " + s[1];
									} else if ("".equals(s[1])) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= " + s[0];
									} else {
										sql = sql + " and " + cf.getFiledName()
												+ " between " + s[0] + " and "
												+ s[1];
									}
								} else {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ cf.getSearchValue() + " ";
								}
							} else if (ConditionOperator.OR.equals(cf
									.getLogicName())) {
								if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " or " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");
									if ("".equals(s[0])) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= " + s[1];
									} else if ("".equals(s[1])) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= " + s[0];
									} else {
										sql = sql + " or " + cf.getFiledName()
												+ " between " + s[0] + " and "
												+ s[1];
									}
								} else {
									sql = sql + " or " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ cf.getSearchValue() + " ";
								}
							} else {
								if (ConditionOperator.IN.equals(cf
										.getOperatorName())) {
									sql = sql + " and " + cf.getFiledName()
											+ " in (" + cf.getSearchValue()
											+ ") ";
								} else if (ConditionOperator.BETWEEN.equals(cf
										.getOperatorName())) {
									String[] s = cf.getSearchValue().split(
											"\\|");
									if (cf.getSearchValue().indexOf("|") == 0) {// 第一个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " <= " + s[1];
									} else if (cf.getSearchValue().lastIndexOf(
											"|") == cf.getSearchValue()
											.length() - 1) {// 第二个参数为空
										sql = sql + " and " + cf.getFiledName()
												+ " >= " + s[0];
									} else {
										sql = sql + " and " + cf.getFiledName()
												+ " between " + s[0] + " and "
												+ s[1];
									}
								} else {
									sql = sql + " and " + cf.getFiledName()
											+ " " + cf.getOperatorName() + " "
											+ cf.getSearchValue() + " ";
								}
							}
						}
					} else {
						continue;
					}
				}

			}
			this.searchSql = sql;
		}
		return searchSql;
	}

	public void setSearchSql(String searchSql) {
		this.searchSql = searchSql;
	}

	public void addFilter(ConditionFilter f) {
		this.filters.add(f);
	}
}
