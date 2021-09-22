package com.devsuperior.dsclient.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dsclient.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	private HttpStatus status = HttpStatus.NOT_FOUND;
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setError(e.getMessage());
		err.setPath(request.getRequestURI());
		err.setStatus(status.value());
		err.setMessage("Resource not found");
		err.setTimestamp(Instant.now());
		return ResponseEntity.status(status).body(err);
	}
}
