package com.fiap.fiapblog.controller;

import java.util.List;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<String> handleOptimistcLockingFailureException(
      OptimisticLockingFailureException optimisticLockingFailureException) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(optimisticLockingFailureException.getMessage() + " Erro de concorrência! Por favor, tente novamente");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<String>> handleValidationException(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    var errors = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage()).toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
