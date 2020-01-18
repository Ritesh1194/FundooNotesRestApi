package com.bridgelabz.services;

import java.util.List;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;

public interface ILabelService {
	public boolean createLabel(LabelDto label, String token);

	public boolean editLabel(LabelUpdate label, String userid);

	// public boolean deleteLabel(LabelUpdate info, String token);

	public boolean addLabel(Long labelId, Long noteId, String token);

	List<Label> getLabel(String token);

	List<Note> getAllNotes(String token, Long labelId);

	public boolean removeLabel(Long labelId, Long noteId, String token);

	public boolean createLabelAndMap(LabelDto label, String token, Long noteId);
}