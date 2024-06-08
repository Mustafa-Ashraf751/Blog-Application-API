package com.blogApp.spring_blog_api.exception;

import org.springframework.http.HttpStatus;

public class BlogAppException extends RuntimeException {
  private HttpStatus httpStatus;
  private String message;

  public BlogAppException(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public BlogAppException(String message, HttpStatus httpStatus, String message1) {
    super(message);
    this.httpStatus = httpStatus;
    this.message = message1;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
