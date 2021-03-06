package io.hexlet.hexletcorrection.controller.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    protected Map<String, String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return Map.of("account", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CorrectionNotFoundException.class)
    protected Map<String, String> handleCorrectionNotFoundException(CorrectionNotFoundException ex) {
        return Map.of("correction", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BiFunction<String, String, String> mergeFunction = (str1, str2) -> str1 + "; " + str2;

        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(error -> nonNull(error.getDefaultMessage()))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        ObjectError::getDefaultMessage,
                        mergeFunction::apply));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected Map<String, String> handleUniqueConstraintsException(DataIntegrityViolationException ex) {
        return Map.of("Error:", "Entity already exist", "Developer message:", ex.getMessage());
    }
}
