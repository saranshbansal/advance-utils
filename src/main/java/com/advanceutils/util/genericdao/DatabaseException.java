package com.advanceutils.util.genericdao;

public class DatabaseException extends BaseException {

    private static final long serialVersionUID = 4678463462488610394L;

    public DatabaseException(String errorCode,  Exception ex) {
        super(errorCode, ex, null);
    }
    
    public DatabaseException(String errorCode, Exception ex, String[] params) {
    	super(errorCode, ex, params);
    }

    public DatabaseException(String errorCode, String message) {
        super(errorCode, message, null);
    }

    public DatabaseException(String errorCode, String message, Exception ex) {
    	super(errorCode, message, ex);
    } 
    
    public DatabaseException(String errorCode, String[] params) {
        super(errorCode, null, params);
    }
}
