package com.flys.dao.service;

public class DaoException extends RuntimeException {

  // code d'erreur
  private int code;

  // constructeurs
  public DaoException() {
  }

  public DaoException(String detailMessage, int code) {
    super(detailMessage);
    this.code = code;
  }

  public DaoException(Throwable throwable, int code) {
    super(throwable);
    this.code = code;
  }

  // getters et setters

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
