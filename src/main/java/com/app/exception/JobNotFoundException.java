package com.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Job position not found")
public class JobNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
}
