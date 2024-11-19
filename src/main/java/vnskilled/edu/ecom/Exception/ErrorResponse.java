package vnskilled.edu.ecom.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vnskilled.edu.ecom.Model.Request.RequestContext;

import java.util.Map;

public class ErrorResponse {
	public static ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
		RequestContext requestContext = RequestContext.get ();
		return  ResponseEntity.status(status).body (
				Map.of("message",message,
				"status",status.value(),
				"requestUrl",requestContext.getRequestURL (),
				"requestId",requestContext.getRequestId (),
				"timestamp",requestContext.getTimestamp ()));
	}
}
