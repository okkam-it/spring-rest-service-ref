package it.okkam.spring.api.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({ "code", "message", "errors" })
public class ApiError {

  @JsonIgnore
  private HttpStatus httpStatus;
  @JsonProperty(value = "code")
  private int errorCode = 0;
  private String message;
  private List<String> errors;

  public ApiError() {
  }

  /**
   * ApiError constructor.
   * 
   * @param httpStatus
   *          the HTTP status
   * @param message
   *          the error message
   */
  public ApiError(final HttpStatus httpStatus, final String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  /**
   * ApiError constructor.
   * 
   * @param httpStatus
   *          the HTTP status
   * @param message
   *          the error message
   * @param errors
   *          the detail of the errors
   */
  public ApiError(final HttpStatus httpStatus, final String message,
      final List<String> errors) {
    this.httpStatus = httpStatus;
    this.message = message;
    this.errors = errors;
  }

  /**
   * ApiError constructor.
   * 
   * @param httpStatus
   *          the HTTP status
   * @param message
   *          the error message
   * @param error
   *          the error detail
   */
  public ApiError(final HttpStatus httpStatus, final String message,
      final String error) {
    this.httpStatus = httpStatus;
    this.message = message;
    errors = Arrays.asList(error);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(final HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(final List<String> errors) {
    this.errors = errors;
  }

  public void setError(final String error) {
    errors = Arrays.asList(error);
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }
}
