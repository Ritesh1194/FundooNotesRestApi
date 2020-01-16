package com.bridgelabz.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Entity
@Table(name = "User")
@Component
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;

	@Column(nullable = false)
	private String firstname;

	@Column(nullable = false)
	private String lastname;

	@Column(nullable = false)
	private String email;

	private String password;

	@Column(nullable = false)
	private Long mobileNumber;

	private LocalDateTime createdDate;

	private boolean isVerified;

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "userId")
//
//	private List<Note> note;
//
//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "Collaborator_Note", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
//			@JoinColumn(name = "note_id") })
//
////	@JsonManagedReference
//	@JsonIgnore
//	private List<Note> colaborateNote;

}