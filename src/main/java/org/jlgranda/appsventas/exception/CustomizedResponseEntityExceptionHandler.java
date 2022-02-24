package org.jlgranda.appsventas.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(),
				new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<Object> handleNoSuchElementException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), new Date(), messageSource
						.getMessage("error.entity_not_exist_find_array.message", null, LocaleContextHolder.getLocale()),
				request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.value(), new Date(),
				messageSource.getMessage("error.constraint_validation.message", null, LocaleContextHolder.getLocale()),
				request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(StackOverflowError.class)
	public final ResponseEntity<Object> handleStackOverFlowError(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleCatalogoNotFoundException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(),
				new Date(), ex.getMessage(), request.getDescription(true));
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}
}
