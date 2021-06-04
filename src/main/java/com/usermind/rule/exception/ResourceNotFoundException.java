package com.usermind.rule.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -1266176142238147750L;

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
