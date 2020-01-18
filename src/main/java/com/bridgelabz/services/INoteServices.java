package com.bridgelabz.services;

import java.util.List;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.NoteDto;
import com.bridgelabz.model.NoteUpdate;
import com.bridgelabz.model.ReminderDto;

public interface INoteServices {
	public boolean createNote(NoteDto noteDto, String token);

	public boolean updateNote(NoteUpdate updateNote, String token);

	public boolean deleteNote(long id, String token);

	List<Note> getAllNotes(String token);

	List<Note> getTrashedNotes(String token);

	public boolean archievNote(long id, String token);

	List<Note> getArchiveNote(String token);

	public boolean addColour(Long noteId, String token, String colour);

	public boolean addReminder(Long noteId, String token, ReminderDto reminder);

	public boolean removeReminder(Long noteId, String token, ReminderDto reminder);

	public boolean pinNote(long id, String token);

	List<Note> getAllPinnedNotes(String token);

	public boolean restored(String token, Long noteId);

//
	public boolean trashed(String token, Long noteId);
}