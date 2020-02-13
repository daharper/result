package com.company;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the result of an operation.
 */
public class OpResult {
  /**
   * Contains a list of error codes used within Result.
   */
  public enum Code {
    Ok,
    Error,
    CustomError,
    InvalidOperation,
    MissingValue,
    InternalServiceError
  }

  // initialize the error code to message map
  private static final Map<Code, String> errors;

  static {
    errors = new HashMap<Code, String>() {{
      put(Code.Ok, "");
      put(Code.Error, "An error has occurred.");
      put(Code.CustomError, "");
      put(Code.InvalidOperation, "Invalid operation error.");
      put(Code.MissingValue, "Expected value was missing.");
      put(Code.InternalServiceError, "An internal server error occurred.");
    }};
  }

  // the result code
  private Code code;

  // an optional error message providing more details
  private String message;

  // an optional exception
  private Exception exception;

  // keeps track of last branch test operation
  private enum Op { None, isOk, isError };
  private Op lastOp = Op.None;

  // initializes a new instance of Status with the specified code
  protected OpResult(Code code) {
    this.code = code;
    this.message = "";
    this.exception = exception();
  }

  // initializes a new instance of Status with the specified code and message
  protected OpResult(Code code, String message) {
    assert(code != Code.Ok);
    this.code = code;
    this.message = message;
    this.exception = null;
  }

  // initializes a new instance of a status with the specified arguments
  protected OpResult(Code code, String message, Exception exception) {
    assert(code != Code.Ok);
    this.code = code;
    this.message = message;
    this.exception = exception;
  }

  /**
   * Gets the error message for this instance.
   *
   * @return  the error message for this instance if found; otherwise 'unspecified error'
   */
  public String err() {
    return errors.get(code);
  }

  /**
   * Gets the status code of this instance
   *
   * @return  the status code
   */
  public Code code() {
    return code;
  }

  /**
   * Set the status code of this instance with the specified code
   *
   * @param code  the status code
   */
  public void setCode(Code code) {
    this.code = code;
  }

  /**
   * Gets the optional error message
   *
   * @return  the error message
   */
  public String msg() {
    return message;
  }

  /**
   * Sets an optional error message associated
   *
   * @param message the error message
   */
  public void setMsg(String message) {
    this.message = message;
  }

  /**
   * Gets the exception associated with this instance
   *
   * @return  an exception if present; otherwise null
   */
  public Exception exception() {
    return exception;
  }

  /**
   * Sets an exception associated with this instance
   *
   * @param exception the exception
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }

  /**
   * Gets the error and optional message separated by a line break.
   *
   * @return an error string
   */
  public String details() {
    StringBuilder sb = new StringBuilder();

    sb.append(err());

    if (message.length() > 0) {
      sb.append(System.lineSeparator());
      sb.append(message);
    }

    if (exception != null) {
      sb.append(System.lineSeparator());
      sb.append(exception.getMessage());
    }

    return sb.toString();
  }

  /**
   * Gets a value indicating whether this instance is a success.
   *
   * @return  true if the instance is a success
   */
  public boolean isOk() {
    return code == Code.Ok;
  }

  /**
   * Gets a value indicating whether this instance is a failure.
   *
   * @return  true if this instance is a failure
   */
  public boolean isError() {
    return code != Code.Ok;
  }

  /**
   * Gets a value indicating whether this instance has an exception.
   *
   * @return  true if this instance has an exception; otherwise false
   */
  public boolean isException() {
    return exception != null;
  }

  /**
   * Gets a value indicating whether the status is the same as the specified code.
   *
   * @param code  a status code
   * @return      true if the status matches the specified statys code
   */
  public boolean is(Code code) {
    return this.code == code;
  }

  // can we perform an on ok action?
  protected boolean isOnOk() {
    lastOp = Op.isOk;
    return isOk();
  }

  // can we perform an on error action?
  protected boolean isOnError() {
    lastOp = Op.isError;
    return isError();
  }

   // Determines if an else condition can be applied to the chained function.
  protected boolean isElseCondition() {
    if (lastOp == Op.None) return false;
    if (lastOp == Op.isError && isOk()) return true;
    if (lastOp == Op.isOk && isError()) return true;
    return false;
  }
}
