package com.bridgelabz.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Login;
import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserDto;
import com.bridgelabz.repository.UserRepositoryImplementation;
import com.bridgelabz.responses.MailObject;
import com.bridgelabz.responses.MailResponse;
import com.bridgelabz.util.JwtGenerator;
import com.bridgelabz.util.MailServiceProvider;

@Service
public class UserServicesImplementation implements IUserServices {
	private User user = new User();
	@Autowired
	private UserRepositoryImplementation repository;
	@Autowired
	private BCryptPasswordEncoder encryption;
	@Autowired
	private JwtGenerator generate;
	@Autowired
	private MailResponse response;
	@Autowired
	private MailObject mailObject;
	@Autowired
	private MailServiceProvider mailServiceProvider;

	@Override
	public boolean register(UserDto information) {
		System.out.println("in regis service, before ....");
		User user = repository.getUser(information.getEmail());
		System.out.println(user);

		if (user == null) {
			System.out.println("in regis service , above mapping");

			BeanUtils.copyProperties(information, User.class);
			user.setCreatedDate(LocalDateTime.now());
			String epassword = encryption.encode(information.getPassword());
			user.setPassword(epassword);
			user.setVerified(false);

			try {
				System.out.println("id" + " " + user.getUserId());
				System.out.println("token" + " " + generate.jwtToken(user.getUserId()));
				String mailResponse = response.formMessage("http://localhost:9050/user/verify",
						generate.jwtToken(user.getUserId()));

				System.out.println(mailResponse);
				mailObject.setEmail(information.getEmail());
				mailObject.setMessage(mailResponse);
				mailObject.setSubject("verification");

				mailServiceProvider.sendEmail(information.getEmail(), "Verification", mailResponse);
				user = repository.save(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;

		} else {
			throw new UserException("user already exist with the same mail id");
		}
	}

	@Override
	public User login(Login information) {
		User user = repository.getUser(information.getUsername());
		if (user != null) {

			if ((user.isVerified() == true) && encryption.matches(information.getPassword(), user.getPassword())) {
				System.out.println(generate.jwtToken(user.getUserId()));
				return user;
			} else {
				String mailResponse = response.formMessage("http://localhost:9050/user/verify",
						generate.jwtToken(user.getUserId()));

				MailServiceProvider.sendEmail(information.getUsername(), "verification", mailResponse);
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public boolean update(PasswordUpdate information, String token) {
		if (information.getNewPassword().equals(information.getConfirmPassword())) {

			Long id = null;
			try {
				System.out.println("in update method" + "   " + generate.parseJWT(token));
				id = (long) generate.parseJWT(token);
				String epassword = encryption.encode(information.getConfirmPassword());
				information.setConfirmPassword(epassword);
				return repository.upDate(information, id);
			} catch (Exception e) {
				throw new UserException("invalid credentials");
			}
		}

		else {
			throw new UserException("invalid password");
		}

	}

	public String generateToken(Long id) {
		return generate.jwtToken(id);

	}

	@Override
	public boolean verify(String token) throws Exception {
		System.out.println("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		repository.verify(id);
		return true;
	}

	@Override
	public boolean isUserExist(String email) {
		try {
			User user = repository.getUser(email);
			if (user.isVerified() == true) {
				String mailResponse = response.formMessage("http://localhost:9050/user/update/",
						generate.jwtToken(user.getUserId()));
				MailServiceProvider.sendEmail(user.getEmail(), "verification", mailResponse);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new UserException("User doesn't exist");
		}
	}

	@Override
	public List<User> getUsers() {
		List<User> users = repository.getUsers();
		User user = users.get(0);
		return users;
	}

	@Override
	public User getSingleUser(String token) {
		Long id;
		try {
			id = (long) generate.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("User doesn't exist");
		}
		User user = repository.getUserById(id);
		return user;
	}
}