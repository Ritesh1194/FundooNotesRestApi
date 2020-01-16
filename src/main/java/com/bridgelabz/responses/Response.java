package com.bridgelabz.responses;

import java.util.List;

import lombok.Data;

@Data
public class Response {

	private String message;

	private int statusCode;

	private Object obj;

	List<String> details;

	public Response(String message, int statusCode) {

		this.message = message;

		this.statusCode = statusCode;
	}

	public Response(String message, int statusCode, Object obj) {
		this.message = message;
		this.statusCode = statusCode;
		this.obj = obj;
	}
}
