package org.sena.helper.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class WarriorException extends RuntimeException {

    private final Response.Status errorCode;

    public WarriorException(Response.Status errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }
}
