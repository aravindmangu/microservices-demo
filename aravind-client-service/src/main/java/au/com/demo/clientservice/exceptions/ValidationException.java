package au.com.demo.clientservice.exceptions;

import au.com.demo.clientservice.client.v1.model.ValidationErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 4117098427916966081L;

    private ValidationErrorResponse response;

    public ValidationException response(ValidationErrorResponse resp) {
        this.setResponse(resp);
        return this;
    }

}
