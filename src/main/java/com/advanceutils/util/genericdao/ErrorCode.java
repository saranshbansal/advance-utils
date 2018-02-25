package com.advanceutils.util.genericdao;

/**
 * This class will take care of all the custom error code.
 */
public class ErrorCode {
    public static final String SERVICE_NOT_FOUND = "1000";
    public static final String CLIENT_PROTOCOL_EXCEPTION = "1001";
    public static final String IO_EXCEPTION = "1002";
    public static final String PARSING_EXCEPTION = "1003";

	public static final String GENERIC_ERROR_CODE = "1004";
	public static final String QUERY_TIMEOUT_ERROR_CODE = "1005";
	public static final String COMMON_DB_ERROR_CODE = "1006";
    public static final String UNSUPPORTED_ENCODING_EXCEPTION = "1007";
    public static final String REPORT_ID_NULL = "1008";
    public static final String RUN_ID_NULL = "1009";
    
    private ErrorCode(){}
}
