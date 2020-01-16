package com.bridgelabz.model;

import lombok.Data;

@Data
public class PasswordUpdate {

	String email;

	String newPassword;

	String confirmPassword;
}