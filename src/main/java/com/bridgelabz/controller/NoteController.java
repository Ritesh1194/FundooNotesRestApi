package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Note;
import com.bridgelabz.model.NoteDto;
import com.bridgelabz.model.NoteUpdate;
import com.bridgelabz.model.ReminderDto;
import com.bridgelabz.responses.Response;
import com.bridgelabz.services.INoteServices;
import com.bridgelabz.services.NoteServiceImpl;

@RestController
@RequestMapping("/note")
public class NoteController {
	@Autowired
	private NoteServiceImpl noteServices;

	@PostMapping("/create")
	public ResponseEntity<Response> registration(@RequestBody NoteDto information, @RequestHeader String token) {
		System.out.println(information.getDescription());
		noteServices.createNote(information, token);

		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("note created", 200, information));

	}

	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody NoteUpdate note, @RequestHeader("token") String token) {
		System.out.println("inside update controller" + note.getNoteId());
		noteServices.updateNote(note, token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("note updated", 200));
	}

	@PostMapping("/archieve/{id}")
	public ResponseEntity<Response> archieve(@PathVariable long id, @RequestHeader("token") String token) {
		System.out.println("inside delete controller" + id);
		noteServices.archievNote(id, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("note archieved", 200));

	}

	@PostMapping("/pin/{id}")
	public ResponseEntity<Response> pin(@PathVariable long id, @RequestHeader("token") String token) {
		System.out.println("inside pin" + id);
		noteServices.pinNote(id, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("note pinned", 200));

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable long id, @RequestHeader("token") String token) {
		System.out.println("inside delete controller" + id);
		noteServices.deleteNote(id, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("note deleted", 200));

	}

	@GetMapping("/fetchNote")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token) {

		List<Note> notes = noteServices.getAllNotes(token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("The notes are", 200, notes));
	}

	@GetMapping("/fetchTrashedNote")
	public ResponseEntity<Response> getTrashedNotes(@RequestHeader("token") String token) {
		List<Note> notes = noteServices.getTrashedNotes(token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("The trashed notes are", 200, notes));

	}

	@GetMapping("/note/fetcharchivenote")
	public ResponseEntity<Response> getArchiveNote(@RequestHeader("token") String token) {
		List<Note> notes = noteServices.getArchiveNote(token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("The archieved notes are", 200, notes));

	}

	@GetMapping("/fetchpinnednote")
	public ResponseEntity<Response> getPinnedNote(@RequestHeader("token") String token) {
		List<Note> notes = noteServices.getAllPinnedNotes(token);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("The pinned notes are", 200, notes));

	}

	@PostMapping("/addColor")
	public ResponseEntity<Response> addColour(@RequestParam("noteId") Long noteId, @RequestParam("color") String colour,
			@RequestHeader("token") String token) {
		noteServices.addColour(noteId, token, colour);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("color added", 200, colour));

	}

	@PostMapping("/addreminder")
	public ResponseEntity<Response> addReminder(@RequestParam("noteId") Long noteId,
			@RequestHeader("token") String token, @RequestBody ReminderDto reminder) {
		noteServices.addReminder(noteId, token, reminder);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Reminder added", 200, reminder));

	}

	@PostMapping("/removereminder")
	public ResponseEntity<Response> removeReminder(@RequestParam("noteId") Long noteId,
			@RequestHeader("token") String token) {
		noteServices.removeReminder(noteId, token, null);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Reminder removed", 200, noteId));
	}

//	@PostMapping("/notes/trashed/{noteId}")
//	public ResponseEntity<Response> trashed(@RequestHeader String token, @PathVariable Long noteId) {
//		if (noteServices.trashed(token, noteId)) {
//			return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "Note is Trashed"), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), "Note is Trashed"),
//				HttpStatus.BAD_REQUEST);
//	}
//
//	@PostMapping("/notes/restore/{noteId}")
//	public ResponseEntity<Response> restore(@RequestHeader String token, @PathVariable Long noteId) {
//		if (noteServices.restored(token, noteId)) {
//			return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "Note is Restored"), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), "Note is Trashed"),
//				HttpStatus.BAD_REQUEST);
//	}
}