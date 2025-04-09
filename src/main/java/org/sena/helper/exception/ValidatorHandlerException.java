package org.sena.helper.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;

@Provider
public class ValidatorHandlerException implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        HandlerException.ResponseError responseError = HandlerException.ResponseError.builder()
                .errorCode(Response.Status.BAD_REQUEST.getStatusCode())
                .title(Response.Status.BAD_REQUEST)
                .detail(exception.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; ")))
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(responseError)
                .build();
    }
}
