package com.bridgelabz.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoteUpdate {
	private long noteId;

	private String title;

	private String description;

	private boolean isArchieved;

	private boolean isPinned;

	private boolean isTrashed;

	private LocalDateTime createdDateAndTime;

	private LocalDateTime upDateAndTime;
}