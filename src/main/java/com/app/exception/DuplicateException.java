package com.app.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Key is duplicated")
public class DuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
}

