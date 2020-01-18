package com.bridgelabz.model;

import java.util.List;

import javax.persistence.CascadeType;
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

import lombok.Data;

@Data
@Entity
@Table(name = "Label")
@Component
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labelId;

	private String name;

	private long userId;

	@JoinColumn(name = "user_id")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "note_label", joinColumns = { @JoinColumn(name = "label_id") }, inverseJoinColumns = {
			@JoinColumn(name = "note_id") })

	private List<Note> list;
}