package com.bridgelabz.services;

import java.util.List;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.NoteDto;
import com.bridgelabz.model.NoteUpdate;
import com.bridgelabz.model.ReminderDto;

public interface INoteServices {
	void createNote(NoteDto noteDto, String token);

	void updateNote(NoteUpdate updateNote, String token);

	void deleteNote(long id, String token);

	List<Note> getAllNotes(String token);

	List<Note> getTrashedNotes(String token);

	boolean deleteNotePemenetly(long id, String token);

	void archievNote(long id, String token);

	List<Note> getArchiveNote(String token);

	void addColour(Long noteId, String token, String colour);

	void addReminder(Long noteId, String token, ReminderDto reminder);

	void removeReminder(Long noteId, String token, ReminderDto reminder);

	void pin(long id, String token);

	List<Note> getAllPinnedNotes(String token);

}