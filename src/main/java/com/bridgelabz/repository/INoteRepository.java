package com.bridgelabz.repository;

import java.util.List;

import com.bridgelabz.model.Note;

public interface INoteRepository {
	public Note saveNote(Note note);

	public Note findById(long id);

	public boolean deleteNote(long id, long userId);

	public List<Note> getNotes(long userId);

	public List<Note> getTrashedNotes(long userId);

	public List<Note> getArchiveNotes(long userId);

	public boolean updateColor(long userId, long id, String color);
}
