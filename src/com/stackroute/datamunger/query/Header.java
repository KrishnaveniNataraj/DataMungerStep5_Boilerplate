package com.stackroute.datamunger.query;

import java.util.HashMap;
import java.util.Map;

//Header class containing a Collection containing the headers
public class Header extends HashMap<String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Integer> headers;

	public Header(Map<String, Integer> headers) {
		super();
		this.headers = headers;
	}

	public Map<String, Integer> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Integer> headers) {
		this.headers = headers;
	}

}
