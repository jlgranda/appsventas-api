package org.jlgranda.appsventas.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class FieldErrorResource {
    private String resource;
    private String field;
    private String type;
    private String message;

    public FieldErrorResource(String resource, String field, String type, String message) {

        this.resource = resource;
        this.field = field;
        this.type = type;
        this.message = message;
    }
}
