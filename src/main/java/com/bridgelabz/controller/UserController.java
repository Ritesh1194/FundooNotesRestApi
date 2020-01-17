
package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Login;
import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserDto;
import com.bridgelabz.responses.Response;
import com.bridgelabz.responses.UsersDetail;
import com.bridgelabz.services.IUserServices;
import com.bridgelabz.util.JwtGenerator;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserServices userService;
	@Autowired
	private JwtGenerator generate;

	@PostMapping("/registration")
	@ResponseBody
	public ResponseEntity<Response> registration(@RequestBody UserDto information) {
		System.out.println("in regis cont...");
		boolean result = userService.register(information);
		if (result) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("registration successfull", 200, information));

		} else {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new Response("user already exist", 400, information));
		}
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) throws Exception {

		System.out.println("token for verification" + token);
		boolean update = userService.verify(token);
		if (update) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("verified", 200));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("not verified", 400));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<UsersDetail> login(@RequestBody Login information) {

		User userInformation = userService.login(information);
		System.out.println("inside login controler");
		if (userInformation != null) {
			String token = generate.jwtToken(userInformation.getUserId());
			System.out.println(token);
			return ResponseEntity.status(HttpStatus.ACCEPTED).header("login successfull", information.getUsername())
					.body(new UsersDetail(token, 200, information));
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new UsersDetail("Login failed", 400, information));
		}
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgogPassword(@RequestParam("email") String email) {
		System.out.println(email);

		boolean result = userService.isUserExist(email);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("user exist", 200));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Response("user does not exist with given email id", 400));
		}

	}

	@PutMapping("/update/{token}")
	public ResponseEntity<Response> update(@PathVariable("token") String token, @RequestBody PasswordUpdate update) {
		System.out.println("inside controller  " + update.getConfirmPassword());
		System.out.println("inside controller  " + token);
		boolean result = userService.update(update, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Response("password updated successfully", 200, update));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response("password doesn't match", 401, update));
		}
	}

	@GetMapping("/getusers")
	public ResponseEntity<Response> getUsers() {
		List<User> users = userService.getUsers();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("The user's are", 200, users));
	}

	@GetMapping("/getOneUser")
	public ResponseEntity<Response> getOneUsers(@RequestHeader("token") String token) {
		User user = userService.getSingleUser(token);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("user is", 200, user));
	}
}