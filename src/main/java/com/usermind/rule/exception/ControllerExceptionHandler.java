package com.usermind.rule.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> handleMissingResources(final ResourceNotFoundException ex) {
    log.error("Resource not found", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested resource cannot be found, " + ex.getMessage());
  }

  @ExceptionHandler(FieldValidationException.class)
  protected ResponseEntity<Object> handleValidationException(final FieldValidationException ex) {
    log.error("Error during validation", ex);
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(AlreadyExistException.class)
  protected ResponseEntity<Object> alreadyExistException(final AlreadyExistException ex) {
    log.error("Error exists", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

//  @ExceptionHandler(ConstraintViolationException.class)
//  protected ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
//    log.error("Error constraint", ex);
//    String message = ex.getConstraintViolations().stream()
//        .map(violation -> String.format("%s value '%s' %s", StreamSupport.stream(violation.getPropertyPath().spliterator(),
//            false).reduce((first, second) -> second).orElse(null),
//            violation.getInvalidValue(), violation.getMessage())).findFirst().get();
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
//  }

  @ExceptionHandler( {InvocationTargetException.class, IllegalArgumentException.class, ClassCastException.class,
      ConversionFailedException.class})
  @ResponseBody
  public ResponseEntity handleMiscFailures(Throwable t) {
    log.error("Error validate", t);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(t.getMessage());
  }

//  @Override
//  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
//                                                       WebRequest request) {
//    log.error("Bind exception" + ex.getMessage());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }

//  @Override
//  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
//                                                                HttpStatus status, WebRequest request) {
//    log.error("Not readable exception" + ex.getMessage());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }

//  @Override
//  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
//                                                                HttpStatus status, WebRequest request) {
//    log.error("Rest validator error", ex);
//    return ResponseEntity.badRequest().body("Error during validation field:" + ex.getBindingResult().getFieldError());
//  }
}
