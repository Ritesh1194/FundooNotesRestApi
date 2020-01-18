package com.bridgelabz.repository;

import java.util.List;

import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;

public interface IUserRepository {

	public User save(User userInformation);

	public User getUser(String email);

	boolean verify(Long id);

	boolean upDate(PasswordUpdate information, Long token);

	User getUserById(Long id);

	List<User> getUsers();
}