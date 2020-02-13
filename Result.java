package com.company;

import java.util.function.Function;

/**
 * Represents a result from an operation.
 */
public class Result extends OpResult {
  /**
   * Used for chaining result returning functions.
   */
  interface ResultFunction {
    Result invoke();
  }

  /**
   * A generic error with no extra details.
   */
  public static final Result error = new Result(Code.Error);

  /**
   * A success result.
   */
  public static final Result ok = new Result(Code.Ok);

  // Initializes a new instance of the Result type with the specified code.
  private Result(Code code) {
    super(code);
  }

  // Initializes a new instance of the Result type with the specified code and message.
  private Result(Code code, String message) {
    super(code, message);
  }

  // Initializes a new instance of the Result type with the specified arguments.
  private Result(Code code, String message, Exception exception) {
    super(code, message, exception);
  }

  /**
   * If this result is successful, then invoke the specified action.
   *
   * @param func    an function to invoke, which returns an initialized Result.
   * @return        the result of the action if this result is successful; otherwise this result
   */
  public Result onOk(ResultFunction func) {
    return isOnOk() ? func.invoke() : this;
  }

  /**
   * If this result is unsuccessful, then invoke the specified function.
   *
   * @param func    an function to invoke, which returns an initialized Result.
   * @return        the result of the action if this result is unsuccessful; otherwise this result
   */
  public Result onError(Function<Result, Result> func) {
    return isOnError() ? func.apply(this) : this;
  }

  /**
   * If the last comparison failed, then invoke the specified function.
   *
   * @param func  a function to invoke, which returns an initialized Result.
   * @return      the result of the function if executed; otherwise this
   */
  public Result orElse(Function<Result, Result> func) {
    return isElseCondition() ? func.apply(this) : this;
  }

  /**
   * Performs a logical and with the other result.
   *
   * @param other another initialized Result
   * @return      the first error encountered or this instance
   */
  public Result and(Result other) {
    if (isError()) return this;

    if (other == null)
      return error(Code.InvalidOperation, "logical and with null");

    return other.isError() ? other : this;
  }

  /**
   * Performs a logical and with all the other results
   *
   * @param results   an array of initialized results
   * @return          the first error encountered or this instance
   */
  public Result andAll(Result... results) {
    for (Result result: results) {
      Result r = and(result);
      if (r.isError()) return r;
    }
    return this;
  }

  /**
   * Performs a logical or with the other result.
   *
   * @param other another initialized Result
   * @return      the first success encountered or this instance
   */
  public Result or(Result other) {
    if (isOk()) return this;

    if (other == null)
      return error(Code.InvalidOperation, "logical or with null");

    return other.isOk() ? other : this;
  }

  /**
   * Performs a logical or with an array of results
   *
   * @param results   other initialized results
   * @return          the first success encountered or this instance
   */
  public Result orAll(Result... results) {
    for (Result result: results) {
      Result r = or(result);
      if (r.isOk()) return r;
    }

    return this;
  }

  /**
   * Creates a result from a boolean success indicator.
   *
   * @param isSuccess a value indicating whether the operation succeeded
   *
   * @return          Result.success if the indicator is true; otherwise Result.fail
   */
  public static Result of(boolean isSuccess) {
    return isSuccess ? ok : error;
  }

  /**
   * Creates a result from an error indicating string.
   *
   * @param error     a string initialized with an error message, or empty if no error occurred
   *
   * @return          Result.success if the the error is empty; otherwise a failure initialized with the error
   */
  public static Result of(String error) {
    return error == null || error.trim().length() == 0 ? ok : error(error);
  }

  /**
   * Creates an error with the specified code.
   *
   * @param code  the error result code
   * @return      an error result
   */
  public static Result error(Code code) {
    return new Result(code);
  }

  /**
   * Creates a custom error with the specified message.
   *
   * @param message   an error related message
   * @return          a custom error result
   */
  public static Result error(String message) {
    return new Result(Code.CustomError, message);
  }

  /**
   * Creates an error result with the specified code and message.
   *
   * @param code      the error result code
   * @param message   an error related message
   * @return          an error result with the specified message
   */
  public static Result error(Code code, String message) {
    return new Result(code, message);
  }

  /**
   * Creates an error result with the specified exception.
   *
   * @param exception the exception
   * @return          an error result initialized with the specified exception
   */
  public static Result error(Exception exception) {
    return new Result(Code.Error, "", exception);
  }

  /**
   * Creates an error result with the specified arguments
   *
   * @param code      the error code
   * @param message   the optional error message
   * @param exception the exception
   * @return          an error result initialized with the specified arguments
   */
  public static Result error(Code code, String message, Exception exception) {
    return new Result(code, message, exception);
  }

  /**
   * Creates an error result with the specified code and exception.
   *
   * @param code      an error code
   * @param exception an exception
   * @return          an error result initialized with the specified exception
   */
  public static Result error(Code code, Exception exception) {
    return new Result(code, "", exception);
  }
}