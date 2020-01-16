package com.bridgelabz.responses;

import lombok.Data;

@Data
public class UsersDetail {

	private String token;

	private int statuscode;

	private Object obj;

	public UsersDetail(String token, int statuscode, Object obj) {
		this.token = token;

		this.statuscode = statuscode;

		this.obj = obj;
	}
}