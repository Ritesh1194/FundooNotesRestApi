package com.bridgelabz.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.model.Login;
import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserDto;

@Service
public interface UserServices {

	public User login(Login information);

	public boolean register(UserDto ionformation);

	public boolean verify(String token) throws Exception;

	public boolean isUserExist(String email);

	public boolean update(PasswordUpdate information, String token);

	public List<User> getUsers();

	public User getSingleUser(String token);
}