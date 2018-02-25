package com.advanceutils.util.genericdao;

public abstract class BaseException extends Exception {

  private static final long serialVersionUID = -7562185606641302357L;

  private String errorCode;

  private String defaultMessage;

  private String[] params;

  public BaseException(String errorCode, Exception ex, String[] params) {
    super(ex);
    this.errorCode = errorCode;
    this.params = params;
  }

  public BaseException(String errorCode, String message, Exception ex) {
    super(ex);
    this.errorCode = errorCode;
    this.defaultMessage = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    return defaultMessage;
  }

  public String[] getParams() {
    return params;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public void setMessage(String message) {
    this.defaultMessage = message;
  }

  public void setParams(String[] params) {
    this.params = params;
  }

}
