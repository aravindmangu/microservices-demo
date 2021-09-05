package au.com.demo.clientservice.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.demo.clientservice.client.v1.model.ErrorResponse;
import au.com.demo.clientservice.client.v1.model.ValidationError;
import au.com.demo.clientservice.client.v1.model.ValidationErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    private final Tracer tracer;

    private String ERROR = "error";
    private String VALIDATION_FAILED = "validation_failed";
    private String VALUE_NOT_ALLOWED = "value_not_allowed";
    private String MANDATORY_VALUE_MISSING = "mandatory_value_missing";

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> defaultErrorHandler(Exception ex, WebRequest request) throws Exception {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(ExceptionUtils.getStackTrace(ex));
        return error(status, ex, "Unexpected Server Error");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handle(ValidationException ex, WebRequest request) throws Exception {
        return new ResponseEntity<>(ex.getResponse(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handle(ConstraintViolationException ex, WebRequest request) throws Exception {
        ValidationErrorResponse resp = new ValidationErrorResponse();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            ValidationError errorsItem = new ValidationError();
            errorsItem.code(VALUE_NOT_ALLOWED)
                .category(VALIDATION_FAILED)
                .severity(ERROR)
                .description(error.getMessage())
                .field(error.getPropertyPath().toString());
            resp.addErrorsItem(errorsItem);
        }
        return new ResponseEntity<>(resp, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(RecordNotFoundException ex, WebRequest request) throws Exception {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        return error(status, ex, ex.getMessage());
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handle(HttpStatusCodeException ex, WebRequest request) throws Exception {
        final HttpStatus status = HttpStatus.valueOf(ex.getRawStatusCode());
        return error(status, ex, ex.getResponseBodyAsString());
    }

    public ResponseEntity<ErrorResponse> error(HttpStatus status, Exception ex, String message) {
        log.error("error: " + ex.getMessage());

        ErrorResponse error = new ErrorResponse()
            .errorCode(status.value())
            .message(message != null ? message : ExceptionUtils.getRootCauseMessage(ex))
            .traceId(tracer.currentSpan().context().traceId());

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(error);
        //return new ResponseEntity<>(error, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationErrorResponse resp = new ValidationErrorResponse();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            ValidationError errorsItem = new ValidationError();
            errorsItem.code(VALUE_NOT_ALLOWED).
                severity(ERROR).
                category(VALIDATION_FAILED).
                description(error.getDefaultMessage()).
                field(error instanceof FieldError ? ((FieldError)error).getField() : error.getObjectName());
            resp.addErrorsItem(errorsItem);
        }
        return new ResponseEntity<>(resp, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
