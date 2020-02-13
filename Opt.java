package com.company;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a Result from an operation with an optional value.
 *
 * @param <T> the type of the value this class wraps.
 */

public class Opt<T> extends OpResult {
  // the value this class wraps
  private final T value;

  // Initializes a new instance of the Opt type with the specified result code
  private Opt(Code code) {
    super(code);
    this.value = null;
  }

  // Initializes a new instance of the Opt type with the specified result code and message
  private Opt(Code code, String message) {
    super(code, message);
    this.value = null;
  }

  // Initializes a new instance of the Opt type with the specified result code and message
  private Opt(Code code, String message, Exception exception) {
    super(code, message, exception);
    this.value = null;
  }

  // Initializes a new instance of the Opt type with the specified value.
  private Opt(T value) {
    super(Code.Ok);
    this.value = value;
  }

  /**
   * Gets the value this instance holds.
   *
   * @return the value of this instance
   */
  public T get() {
    return value;
  }

  /**
   * Gets the value if it is not null; otherwise the specified default value is returned.
   *
   * @param defaultValue a default value
   * @return the value of this instance if it is not null; otherwise the specified default value
   */
  public T getOrDefault(T defaultValue) {
    return isPresent() ? value : defaultValue;
  }

  /**
   * Determines whether this instance holds a value.
   *
   * @return true if this instance holds a value; otherwise false
   */
  public boolean isPresent() {
    return value != null;
  }

  /**
   * Determines whether this instance has no value.
   *
   * @return true if this instance has no value; otherwise false
   */
  public boolean isEmpty() {
    return value == null;
  }

  /**
   * If this instance has no value, then execute the specified function.
   *
   * @return either this function if is has a value; otherwise the result of the specified function
   */
  public <R> Opt<R> or(R value) {
    if (isEmpty()) {
      return new Opt<R>(value);
    }

    return (Opt<R>) this;
  }

  /**
   * Determines if this instances value matches the specified value.
   *
   * @param value the value to match
   * @return true if the values match
   */
  public boolean is(T value) {
    if (this.value == value) return true;
    if (this.value == null) return false;
    return this.value.equals(value);
  }

  /**
   * If this instance is successful then the specified function is called.
   *
   * @param func a function that accepts a value and returns an Opt of T
   * @return the result of calling the specified function; otherwise this instance
   */
  public <R> Opt<R> onOk(Function<Opt<T>, Opt<R>> func) {
    return isOnOk() ? func.apply(this) : (Opt<R>) this;
  }

  /**
   * If this instance is a failure then the specified function is called.
   *
   * @param func a function that accepts an Opt of T and returns an Opt of T
   * @return the result of calling the specified function; otherwise this instance
   */
  public <R> Opt<R> onError(Function<Opt<T>, Opt<R>> func) {
    return isError() ? func.apply(this) : (Opt<R>) this;
  }

  /**
   * If the last comparison failed, then invoke the specified function.
   *
   * @param func the function to execute
   * @param <R>  the type of the Opt value
   * @return an Opt containing the result of the
   */
  public <R> Opt<R> orElse(Function<Opt<T>, Opt<R>> func) {
    return isElseCondition() ? func.apply(this) : (Opt<R>) this;
  }

  /**
   * Creates a new instance of an Optional containing the value of this instance.
   *
   * @return an Optional of T
   */
  public Optional<T> toOptional() {
    return value == null ? Optional.empty() : Optional.of(value);
  }

  /**
   * Creates an Opt of T from an Optional of T.
   *
   * @param optional the optional
   * @param <T>      the type of the value
   * @return an Opt of T initialized with the value of the specified Optional
   */
  public static <T> Opt<T> fromOptional(Optional<T> optional) {
    return optional.map(Opt::new).orElseGet(Opt::error);
  }

  /**
   * Initializes a new instance of Opt of T with a successful status
   *
   * @param <T>   the type of the value
   * @return      an Opt of T representing success
   */
  public static <T> Opt<T> ok() {
    return new Opt<T>(Code.Ok);
  }

  /**
   * Creates an Opt of T initialized with the specified value.
   * Note: this is a successful result.
   *
   * @param value the value
   * @param <T>   the type of the value
   * @return an Opt of T initialized with the specified value
   */
  public static <T> Opt<T> ok(T value) {
    return new Opt<T>(value);
  }

  /**
   * Infers an Opt result from a value, whether it is initialized or not.
   *
   * @param value the value
   * @param <T>   the type of the value
   * @return Opt of T initialized with value if successful; otherwise a failed Opt of T
   */
  public static <T> Opt<T> of(T value) {
    return value == null ? error(Code.CustomError) : ok(value);
  }

  /**
   * Creates a result from a boolean success indicator.
   *
   * @param isSuccess a value indicating whether the operation succeeded
   *
   * @return          Result.success if the indicator is true; otherwise Result.fail
   */
  public static <T> Opt<T> of(boolean isSuccess) {
    return isSuccess ? ok() : error();
  }

  /**
   * Creates a result from an error indicating string.
   *
   * @param error     a string initialized with an error message, or empty if no error occurred
   *
   * @return          Result.success if the the error is empty; otherwise a failure initialized with the error
   */
  public static <T> Opt<T> of(String error) {
    return error == null || error.trim().length() == 0 ? ok() : error(error);
  }

  /**
   * Creates a result from an exception.
   *
   * @param e       the exception
   * @param <T>     the type of value
   * @return        an Opt of T initialized with an exception
   */
  public static <T> Opt<T> of(Exception e) {
    return error(e);
  }

  /**
   * Creates an Opt of T representing a failure.
   *
   * @param <T> the type of value
   * @return an Opt of T representing a failure
   */
  public static <T> Opt<T> error() {
    return new Opt<T>(Code.Error);
  }

  /**
   * Creates an Opt of T representing a failure with the specified result code.
   *
   * @param code the result code
   * @param <T>  the type of value
   * @return an Opt of T representing the specified failure
   */
  public static <T> Opt<T> error(Code code) {
    return new Opt<T>(code);
  }

  /**
   * Creates an Opt of T representing a failure with the specified message.
   *
   * @param message an error related message
   * @param <T>     the type of value
   * @return an Opt of T representing a failure initialized with the specified message
   */
  public static <T> Opt<T> error(String message) {
    return new Opt<T>(Code.CustomError, message);
  }

  /**
   * Creates an Opt of T representing a failure initialized with the specified message and result code
   *
   * @param code    a result code representing an error
   * @param message an error related message
   * @param <T>     the type of value
   * @return an Opt of T representing a failure with the specified result code and message
   */
  public static <T> Opt<T> error(Code code, String message) {
    return new Opt<T>(code, message);
  }

  /**
   * Creates an error Opt of T with the specified exception.
   *
   * @param exception the exception
   * @return          an error result initialized with the specified exception
   */
  public static <T> Opt<T> error(Exception exception) {
    return new Opt<T>(Code.Error, "", exception);
  }

  /**
   * Creates an error Opt of T with the specified arguments
   *
   * @param code      the error code
   * @param message   the optional error message
   * @param exception the exception
   * @return          an error result initialized with the specified arguments
   */
  public static <T> Opt<T> error(Code code, String message, Exception exception) {
    return new Opt<T>(code, message, exception);
  }

  /**
   * Creates an error Opt of T with the specified code and exception.
   *
   * @param code      an error code
   * @param exception an exception
   * @return          an error result initialized with the specified exception
   */
  public static <T> Opt<T> error(Code code, Exception exception) {
    return new Opt<T>(code, "", exception);
  }
}