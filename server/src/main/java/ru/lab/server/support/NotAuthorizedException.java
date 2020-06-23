package ru.lab.server.support;

public class NotAuthorizedException extends Exception {
  public NotAuthorizedException(String message) {
    super(message);
  }
}
