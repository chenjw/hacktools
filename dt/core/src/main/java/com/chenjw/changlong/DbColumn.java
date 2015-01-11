package com.chenjw.changlong;

public class DbColumn {
	private String columnName;

	private String columnType;

	@Override
	public String toString() {
		return columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

}
