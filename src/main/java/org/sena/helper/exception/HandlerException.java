package org.sena.helper.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.logging.Logger;

@Provider
public class HandlerException implements ExceptionMapper<WarriorException> {

    private static final Logger LOG = Logger.getLogger(HandlerException.class);

    @Override
    public Response toResponse(WarriorException ex) {

        LOG.errorf(ex, "@toResponse EXCEPTION > Inicia manejo de exception presentada en micro servicio Warriors");

        return Response.status(ex.getErrorCode())
                .entity(ResponseError.builder()
                        .errorCode(ex.getErrorCode().getStatusCode())
                        .title(ex.getErrorCode())
                        .detail(ex.getMessage())
                        .build())
                .build();
    }

    @Data
    @Builder
    public static class ResponseError {

        @Schema(examples = "404")
        private int errorCode;

        @Schema(examples = "NOT_FOUND")
        private Response.Status title;

        @Schema(examples = "Internal Server Error")
        private String detail;
    }
}
