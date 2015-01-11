package com.chenjw.changlong;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DbColumns {
	private List<DbColumn> columns = new ArrayList<DbColumn>();

	public DbColumns(List<DbColumn> columns) {
		this.columns.addAll(columns);
	}

	public DbColumns() {
	}

	public void add(DbColumn column) {
		columns.add(column);
	}

	public List<DbColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DbColumn> columns) {
		this.columns = columns;
	}

	public boolean containsName(String name) {
		for (DbColumn column : columns) {
			if (StringUtils.equals(column.getColumnName(), name)) {
				return true;
			}
		}
		return false;
	}

	public String[] toNameArray() {
		String[] r = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			r[i] = columns.get(i).getColumnName();
		}
		return r;
	}

	public void remove(String name) {
		List<DbColumn> newList = new ArrayList<DbColumn>();
		for (DbColumn column : columns) {
			if (!StringUtils.equals(column.getColumnName(), name)) {
				newList.add(column);
			}
		}
		this.columns = newList;
	}

	public DbColumns subList(int fromIndex, int toIndex) {
		return new DbColumns(columns.subList(fromIndex, toIndex));
	}

	public String joinAndFormat(String sep) {
		String[] ss = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			DbColumn c = columns.get(i);
			if ("DATE".equals(c.getColumnType())) {
				ss[i] = "to_char(" + c.getColumnName() + ",'yyyy-MM-dd HH24:mi:ss')";
			} else {
				ss[i] = c.getColumnName();
			}
		}
		return StringUtils.join(ss, sep);
	}

	public String join(String sep) {
		return StringUtils.join(columns, sep);
	}

}
