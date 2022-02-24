package org.jlgranda.appsventas.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
	@JsonInclude(Include.NON_NULL)
	private HttpStatus status;
	@JsonInclude(Include.NON_NULL)
	private Integer codeStatus;
	@JsonInclude(Include.NON_NULL)
	private Date timestamp;
	@JsonInclude(Include.NON_NULL)
	private String message;
	@JsonInclude(Include.NON_NULL)
	private String details;
}
