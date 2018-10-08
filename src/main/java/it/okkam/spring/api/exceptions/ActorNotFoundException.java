package it.okkam.spring.api.exceptions;

public class ActorNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public ActorNotFoundException(String errorMessage) {
    super(errorMessage);
  }

}
