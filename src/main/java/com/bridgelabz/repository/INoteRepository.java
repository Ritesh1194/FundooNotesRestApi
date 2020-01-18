package com.bridgelabz.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.bridgelabz.model.Note;

public interface INoteRepository {
	public Note createNote(Note note);

	public Note findById(long noteId);

	public boolean deleteNote(long id, Note note);

	public List<Note> getNotes(long userId);

	public List<Note> getTrashedNotes(long userId);

	public List<Note> getArchiveNotes(long userId);

	public boolean updateColor(long userId, long id, String color);

	public List<Note> getNotesOfSameLabel(Long userId, Long labelId);

//	public boolean setRestored(Long userId, Long noteId);
//
//	public boolean setTrashed(Long userId, Long noteId);

	public boolean setRemaineder(Long userId, Long noteId, LocalDateTime time);
}
