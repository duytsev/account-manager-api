package ru.duytsev.account.manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
		return new ResponseEntity<>(new ErrorResponse(1, e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {NotEnoughMoneyException.class})
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(NotEnoughMoneyException e) {
		return new ResponseEntity<>(new ErrorResponse(2, e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(value = {ResourceNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		return new ResponseEntity<>(new ErrorResponse(3, e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
