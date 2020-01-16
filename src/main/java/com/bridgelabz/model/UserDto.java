package com.bridgelabz.model;

import lombok.Data;

@Data
public class UserDto {

	private String firstname;

	private String lastname;

	private String email;

	private String password;

	private Long mobileNumber;
}