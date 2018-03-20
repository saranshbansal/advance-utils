package com.advanceutils.util.poibuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class RequestPojo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6512944558046595899L;
	private Integer param1;
	private String param2;
	private Date param3;
	private String param4;
	private String param5;
	
	private Map<String, String> errorMap;

	public Integer getParam1() {
		return param1;
	}

	public void setParam1(Integer param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public Date getParam3() {
		return param3;
	}

	public void setParam3(Date param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}

}
