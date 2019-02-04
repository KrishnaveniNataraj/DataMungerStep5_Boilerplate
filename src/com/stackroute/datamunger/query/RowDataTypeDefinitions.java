package com.stackroute.datamunger.query;

import java.util.HashMap;
import java.util.Map;

//This class will be used to store the column data types as columnIndex/DataType
public class RowDataTypeDefinitions extends HashMap<Integer, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<Integer, String> rowDataType;

	public RowDataTypeDefinitions(Map<Integer, String> rowDataType) {
		super();
		this.rowDataType = rowDataType;
	}

	public Map<Integer, String> getRowDataType() {
		return rowDataType;
	}

	public void setRowDataType(Map<Integer, String> rowDataType) {
		this.rowDataType = rowDataType;
	}

}
