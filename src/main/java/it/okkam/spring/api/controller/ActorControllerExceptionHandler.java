package it.okkam.spring.api.controller;

import it.okkam.spring.api.error.ApiError;
import it.okkam.spring.api.exceptions.ActorNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ActorControllerExceptionHandler {
  /**
   * Common logger for use in subclasses.
   */
  protected final Log logger = LogFactory.getLog(getClass());

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ ActorNotFoundException.class })
  public ApiError handleActoprNotFound(final ActorNotFoundException ex,
      final WebRequest request) {
    logger.info(ex.getClass().getName());
    return new ApiError(1, ex.getLocalizedMessage());
  }
}
