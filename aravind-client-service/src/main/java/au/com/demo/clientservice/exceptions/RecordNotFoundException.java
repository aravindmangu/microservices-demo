package au.com.demo.clientservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5458680283639137789L;

	public RecordNotFoundException(String exception) {
		super(exception);
	}
}
