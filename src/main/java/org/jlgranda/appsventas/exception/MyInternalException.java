package org.jlgranda.appsventas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MyInternalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MyInternalException(String message) {
		super(message);
	}
}
