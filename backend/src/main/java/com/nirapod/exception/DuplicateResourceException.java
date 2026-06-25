package com.nirapod.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

  private final String field;
  private final String value;

  public DuplicateResourceException(String field, String value, String message) {
    super(message);
    this.field = field;
    this.value = value;
  }

  public DuplicateResourceException(String message) {
    super(message);
    this.field = null;
    this.value = null;
  }

  public String getField() {
    return field;
  }

  public String getValue() {
    return value;
  }
}
