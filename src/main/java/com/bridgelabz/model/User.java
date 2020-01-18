package com.bridgelabz.model;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Entity
@Table(name = "User")
@Component
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	private String firstname;

	private String lastname;

	private String email;

	private String password;

	private Long mobileNumber;

	private LocalDateTime createdDate;

	private boolean isVerified;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")

	private List<Note> note;
}