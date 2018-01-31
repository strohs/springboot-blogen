package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.ApiErrorsView;
import com.blogen.api.v1.model.ApiFieldError;
import com.blogen.api.v1.model.ApiGlobalError;
import com.blogen.exceptions.BadRequestException;
import com.blogen.exceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Cliff
 */
@ControllerAdvice("com.blogen.api.v1.controllers")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( {NotFoundException.class} )
    public ResponseEntity<Object> handleNotFoundException( Exception exception, WebRequest request ){
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler( {BadRequestException.class} )
    public ResponseEntity<Object> handleBadRequestException( Exception exception, WebRequest request ) {
        ApiGlobalError globalError = new ApiGlobalError( exception.getMessage() );
        List<ApiGlobalError> globalErrors = Arrays.asList( globalError );
        ApiErrorsView errorsView = new ApiErrorsView( null, globalErrors );
        return new ResponseEntity<>( errorsView, new HttpHeaders(), HttpStatus.BAD_REQUEST );
    }


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException exception,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request ) {

        BindingResult bindingResult = exception.getBindingResult();

        List<ApiFieldError> apiFieldErrors = bindingResult
                .getFieldErrors()
                .stream()
                .map( fieldError ->
                        new ApiFieldError( fieldError.getField(), fieldError.getDefaultMessage(),fieldError.getRejectedValue() ) )
                .collect( toList());

        List<ApiGlobalError> apiGlobalErrors = bindingResult
                .getGlobalErrors()
                .stream()
                .map(globalError -> new ApiGlobalError(
                        globalError.getCode())
                )
                .collect( toList() );

        ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);

        return new ResponseEntity<>(apiErrorsView, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
