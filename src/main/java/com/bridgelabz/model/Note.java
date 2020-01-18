package com.bridgelabz.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "note")
@Component
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long noteId;
	private String title;
	private String description;
	private String color;

	@Column(columnDefinition = "boolean default false")
	private boolean isArchieved;

	@Column(columnDefinition = "boolean default false")
	private boolean isPinned;

	@Column(columnDefinition = "boolean default false")
	private boolean isTrashed;

	private LocalDateTime createdDateAndTime;
	private LocalDateTime upDateAndTime;
	private LocalDateTime reminder;

	private Long userId;

	private Long labelId;

	@JoinColumn(name = "user_id")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "note_label", joinColumns = { @JoinColumn(name = "note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "label_id") })
	private List<Label> list;
}