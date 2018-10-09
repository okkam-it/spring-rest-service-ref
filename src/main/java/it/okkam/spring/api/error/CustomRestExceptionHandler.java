package it.okkam.spring.api.error;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import it.okkam.spring.api.exceptions.ActorNotFoundException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  // 400 (BAD_REQUEST)

  @Override
  // This ex is thrown when argument annotated with @Valid failed validation
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(final BindException ex,
      final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      final TypeMismatchException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = ex.getValue() + " value for " + ex.getPropertyName()
        + " should be of type " + ex.getRequiredType();
    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      final MissingServletRequestPartException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = ex.getRequestPartName() + " part is missing";
    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.BAD_REQUEST, request);
  }

  @Override
  // This exception is thrown when request missing parameter
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      final MissingServletRequestParameterException ex,
      final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = ex.getParameterName() + " parameter is missing";
    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.BAD_REQUEST, request);
  }

  // This exception is thrown when try to set bean property with wrong
  // type.@ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ApiError handleMethodArgumentTypeMismatch(
      final MethodArgumentTypeMismatchException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = ex.getName() + " should be of type "
        + ex.getRequiredType().getName();

    return new ApiError(ex.getLocalizedMessage(), error);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ ConstraintViolationException.class })
  public ApiError handleConstraintViolation(
      final ConstraintViolationException ex, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final List<String> errors = new ArrayList<>();
    for (final ConstraintViolation<?> violation : ex
        .getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " "
          + violation.getPropertyPath() + ": " + violation.getMessage());
    }
    return new ApiError(ex.getLocalizedMessage(), errors);
  }

  // 404 (NOT_FOUND)

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      final NoHandlerFoundException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final String error = "No handler found for " + ex.getHttpMethod() + " "
        + ex.getRequestURL();

    final ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
    return handleExceptionInternal(ex, apiError, headers, HttpStatus.NOT_FOUND,
        request);
  }

  // 405 (METHOD_NOT_ALLOWED)

  @Override
  // occurs when you send a requested with an unsupported HTTP method:
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException ex,
      final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(
        " method is not supported for this request. Supported methods are ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    final ApiError apiError = new ApiError(ex.getLocalizedMessage(),
        builder.toString());
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.METHOD_NOT_ALLOWED, request);
  }

  // 415 (UNSUPPORTED_MEDIA_TYPE)

  @Override
  // occurs when the client send a request with unsupported media type
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers,
      final HttpStatus status, final WebRequest request) {
    logger.info(ex.getClass().getName());
    //
    final StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

    final ApiError apiError = new ApiError(ex.getLocalizedMessage(),
        builder.substring(0, builder.length() - 2));
    return handleExceptionInternal(ex, apiError, headers,
        HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
  }

  // 500 (INTERNAL_SERVER_ERROR) - default handler

  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ Exception.class })
  public ApiError handleAll(final Exception ex, final WebRequest request) {
    logger.error(ex.getClass().getName());
    return new ApiError(ex.getLocalizedMessage(), "error occurred");
  }

  // Application specific errors
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ ActorNotFoundException.class })
  public ApiError handleActoprNotFound(final ActorNotFoundException ex,
      final WebRequest request) {
    logger.info(ex.getClass().getName());
    return new ApiError(1, ex.getLocalizedMessage());
  }

}
