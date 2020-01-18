package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.LabelDto;
import com.bridgelabz.model.LabelUpdate;
import com.bridgelabz.model.Note;
import com.bridgelabz.responses.Response;
import com.bridgelabz.services.LabelServiceImplementation;

@RestController
@RequestMapping("/label")
public class LabelController {
	@Autowired
	private LabelServiceImplementation service;

	@PostMapping("/create")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto label, @RequestHeader("token") String token) {
		System.out.println(label.getName());
		service.createLabel(label, token);

		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("label created", 200, label));
	}

	@PostMapping("/createandmap")
	public ResponseEntity<Response> createLabelAndMap(@RequestBody LabelDto label, @RequestHeader("token") String token,
			@RequestParam("noteId") Long noteId) {
		System.out.println("label is" + label.getName());
		System.out.println("note id is" + noteId);
		service.createLabelAndMap(label, token, noteId);

		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("label created", 200, label));
	}

	@PostMapping("/addlabel")
	public ResponseEntity<Response> addLabel(@RequestParam("labelId") Long labelId,
			@RequestHeader("token") String token, @RequestParam("noteId") Long noteId) {
		System.out.println(labelId);
		service.addLabel(labelId, noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label added", 200));

	}

	@PostMapping("/removelabel")
	public ResponseEntity<Response> removeLabel(@RequestParam("labelId") Long labelId,
			@RequestHeader("token") String token, @RequestParam("noteId") Long noteId) {
		System.out.println(labelId);
		service.removeLabel(labelId, noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label removed", 200));

	}

	@PutMapping("/update")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelUpdate labelInfo,
			@RequestHeader("token") String token) {

		service.editLabel(labelInfo, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label updated", 200, labelInfo));
	}

//	@PostMapping("/delete")
//	public ResponseEntity<Response> delete(@RequestBody LabelUpdate labelInfo, @RequestHeader("token") String token) {
//		System.out.println("conroller");
//		service.deleteLabel(labelInfo, token);
//		return ResponseEntity.status(HttpStatus.OK).body(new Response("label deleted", 200, labelInfo));
//
//	}

	@GetMapping("/getAllLabel")
	public ResponseEntity<Response> get(@RequestHeader("token") String token) {
		List<Label> labels = service.getLabel(token);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response("All the labels that you are using", 200));

	}

	@GetMapping("/getLabelNotes")
	public ResponseEntity<Response> getNotes(@RequestHeader("token") String token, @RequestParam("id") Long labelId) {
		List<Note> list = service.getAllNotes(token, labelId);

		return ResponseEntity.status(HttpStatus.OK).body(new Response("The result is", 200));
	}
}