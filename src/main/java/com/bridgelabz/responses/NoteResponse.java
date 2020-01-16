package com.bridgelabz.responses;

import com.bridgelabz.model.NoteDto;

import lombok.Data;

@Data
public class NoteResponse {

	private NoteDto note;

	public NoteResponse(NoteDto note) {
		this.note = note;
	}
}