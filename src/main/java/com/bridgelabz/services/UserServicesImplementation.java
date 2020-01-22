package com.bridgelabz.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Login;
import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserDto;
import com.bridgelabz.repository.IUserRepository;
import com.bridgelabz.responses.MailObject;
import com.bridgelabz.responses.MailResponse;
import com.bridgelabz.util.JwtGenerator;
import com.bridgelabz.util.MailServiceProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServicesImplementation implements IUserServices {
	@Autowired
	private User userInformation;
	@Autowired
	private IUserRepository repository;
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
	@Autowired
	ModelMapper modelMapper;

	@Transactional
	@Override
	public boolean register(UserDto information) {
		log.info("in regis service, before ....");
		User user = repository.getUser(information.getEmail());
		log.info("User Is " + user);

		if (user == null) {
			log.info("in regis service , above mapping");
			// userInformation = modelMapper.map(information, User.class);
			BeanUtils.copyProperties(information, response);
			BeanUtils.copyProperties(information, userInformation);

			userInformation.setCreatedDate(LocalDateTime.now());
			String epassword = encryption.encode(information.getPassword());
			userInformation.setPassword(epassword);
			userInformation.setVerified(false);
			user = repository.save(userInformation);
			log.info("id" + " " + userInformation.getUserId());
			log.info("token" + " " + generate.jwtToken(userInformation.getUserId()));

			String mailResponse = response.formMessage("http://192.168.1.26:9050/user/verify",
					generate.jwtToken(userInformation.getUserId()));

			log.info(mailResponse);
			mailObject.setEmail(information.getEmail());
			mailObject.setMessage(mailResponse);
			mailObject.setSubject("verification");

			mailServiceProvider.sendEmail(information.getEmail(), "Verification", mailResponse);

			return true;
		} else {
			throw new UserException("user already exist with the same mail id");
		}

	}

	@Transactional
	@Override
	public User login(Login information) {
		User user = repository.getUser(information.getUsername());
		if (user != null) {

			if ((user.isVerified() == true) && encryption.matches(information.getPassword(), user.getPassword())) {
				log.info(generate.jwtToken(user.getUserId()));
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

	@Transactional
	@Override
	public boolean update(PasswordUpdate information, String token) {
		if (information.getNewPassword().equals(information.getConfirmPassword())) {

			Long id = null;
			try {
				log.info("in update method" + "   " + generate.parseJWT(token));
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

	@Transactional
	@Override
	public boolean verify(String token) throws Exception {
		log.info("id in verification" + (long) generate.parseJWT(token));
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

	@Transactional
	@Override
	public List<User> getUsers() {
		List<User> users = repository.getUsers();
		User user = users.get(0);
		return users;
	}

	@Transactional
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