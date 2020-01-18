package com.bridgelabz.repository;

import java.util.List;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;

public interface ILabelRepository {
	public Label saveLabel(Label label);

	public Note saveNote(Note note);

	public Label fetchLabelById(long id);

	public Label fetchLabel(Long userid, String labelname);

	// public int deleteLabel(long i);

	public List<Label> getAllLabel(long id);

	public Label updateLabel(Integer labelId, Label label, String token);

	public Label getLabel(Long id);

	// public Object findByNoteIdAndLabelId(Long labelId, Long noteId);
}