package org.jlgranda.appsventas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class RestrictionException extends RuntimeException {
    public RestrictionException(String message) {
        super(message);
    }


}
