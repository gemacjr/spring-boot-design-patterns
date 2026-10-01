package com.example.designpatterns.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps showcase exceptions to RFC 7807 problem details.
 *
 * <p>{@code @RestControllerAdvice} is itself an application of the patterns on
 * display: Spring collects advice beans and consults them around every handler
 * invocation, which is Chain of Responsibility layered over a Proxy.
 */
@RestControllerAdvice
public class CatalogExceptionHandler {

    @ExceptionHandler(UnknownPatternException.class)
    public ProblemDetail onUnknownPattern(UnknownPatternException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("Unknown pattern");
        detail.setProperty("knownIds", ex.knownIds());
        return detail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail onIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setTitle("Invalid input");
        return detail;
    }
}
