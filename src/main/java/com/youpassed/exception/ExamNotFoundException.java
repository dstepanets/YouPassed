package com.youpassed.exception;

public class ExamNotFoundException extends RuntimeException {
	public ExamNotFoundException(String message) {
		super(message);
	}
}
