package com.bridgelabz.services;

import java.util.List;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;

public interface ILabelService {
	void createLabel(LabelDto label, String token);

	void editLabel(LabelUpdate label, String userid);

	void deleteLabel(LabelUpdate info, String token);

	void addLabel(Long labelId, Long noteId, String token);

	List<Label> getLabel(String token);

	List<Note> getAllNotes(String token, Long labelId);

	void removeLabel(Long labelId, Long noteId, String token);

	void createLabelAndMap(LabelDto label, String token, Long noteId);
}