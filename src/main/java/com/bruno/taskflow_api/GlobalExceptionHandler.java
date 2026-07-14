package com.bruno.taskflow_api;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleTaskNotFound(NotFoundException e) {
    logger.warn(e.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        e.getMessage());
    problemDetail.setTitle("Resource Not Found");
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidations(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Error de validación");
    problemDetail.setTitle("Datos inválidos");
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleMalformedJson(HttpMessageNotReadableException ex) {
    logger.warn("JSON mal formado en la petición: {}", ex.getMessage());

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "El cuerpo de la petición contiene datos con formato inválido (revisa tipos de datos como UUID, fechas, números)");
    problem.setTitle("Solicitud mal formada");
    return problem;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    logger.warn("Tipo de dato inválido en parámetro '{}': {}", ex.getName(), ex.getMessage());

    String detail = "El parámetro '%s' tiene un formato inválido, se esperaba %s".formatted(
        ex.getName(),
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "otro tipo");

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
    problem.setTitle("Parámetro inválido");
    return problem;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
    logger.warn("Violación de integridad de datos: {}", ex.getMessage());

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
        "El recurso ya existe o viola una restricción de datos");
    problem.setTitle("Conflicto de datos");
    return problem;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGeneric(Exception e) {
    logger.error("Error inesperado: ", e);
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
        "Ocurrio un error inesperado");
    problemDetail.setTitle("Error Interno");
    return problemDetail;
  }
}
