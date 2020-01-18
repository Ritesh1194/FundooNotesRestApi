package com.bridgelabz.services;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.NoteDto;
import com.bridgelabz.model.NoteUpdate;
import com.bridgelabz.model.ReminderDto;
import com.bridgelabz.model.User;
import com.bridgelabz.repository.INoteRepository;
import com.bridgelabz.repository.IUserRepository;
import com.bridgelabz.util.JwtGenerator;

@Service
public class NoteServiceImpl implements INoteServices {
	@Autowired
	private JwtGenerator tokenGenerator;
	@Autowired
	private IUserRepository repository;

	private User user = new User();
	@Autowired
	private INoteRepository noteRepository;

	private Note note = new Note();

	@Autowired
	ModelMapper modelMapper;

	@Transactional
	@Override
	public boolean createNote(NoteDto noteDto, String token) {
		long userId;
		try {
			userId = (long) tokenGenerator.parseJWT(token);
			System.out.println("Token Generatted " + userId);

			user = repository.getUserById(userId);
			System.out.println("User " + user);

			if (user != null) {
				// BeanUtils.copyProperties(noteDto, Note.class);
				note = modelMapper.map(noteDto, Note.class);
				note.setUserId(userId);
				note.setCreatedDateAndTime(LocalDateTime.now());
				note.setArchieved(false);
				note.setPinned(false);
				note.setTrashed(false);
				note.setColor("Blue");
				Note noteInformation = noteRepository.createNote(note);
				System.out.println(noteInformation);
			} else {
				throw new UserException("note is not present with the given id ");
			}
		} catch (Exception e) {

			throw new UserException("user is not present with the given id ");

		}
		return true;
	}

	@Transactional
	@Override
	public boolean updateNote(NoteUpdate information, String token) {
		try {
			Long userid = (long) tokenGenerator.parseJWT(token);
			System.out.println("inside note service" + userid);

			user = repository.getUserById(userid);

			Note note = noteRepository.findById(information.getNoteId());
			if (note != null) {
				System.out.println("note is   " + note);
				note.setNoteId(information.getNoteId());
				note.setDescription(information.getDescription());
				note.setTitle(information.getTitle());
				note.setPinned(information.isPinned());
				note.setArchieved(information.isArchieved());
				note.setArchieved(information.isTrashed());
				note.setUpDateAndTime(LocalDateTime.now());
				Note note1 = noteRepository.createNote(note);
				return true;
			}
		} catch (Exception e) {
			throw new UserException("user is not present");
		}
		return false;
	}

	@Transactional
	@Override
	public boolean deleteNote(long id, String token) {
		Note note = noteRepository.findById(id);
		System.out.println("Delete Node1");
		note.setTrashed(!note.isTrashed());
		noteRepository.deleteNote(id, note);
		System.out.println("Delete Node2");
		return true;
	}

	@Transactional
	@Override
	public boolean archievNote(long id, String token) {
		Note note = noteRepository.findById(id);
		note.setArchieved(!note.isArchieved());
		noteRepository.createNote(note);
		return true;
	}

	@Transactional
	@Override
	public boolean pinNote(long id, String token) {
		Note note = noteRepository.findById(id);
		note.setPinned(!note.isPinned());
		noteRepository.createNote(note);
		return true;
	}

	@Transactional
	@Override
	public List<Note> getAllNotes(String token) {
		List<Note> list11 = null;
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);
			if (user != null) {
				System.out.println("user logged in" + user.getUserId());
				System.out.println("user ");
				list11 = noteRepository.getNotes(userId);
			}
		} catch (Exception e) {
			throw new UserException("error occured");
		}
		return list11;
	}

	@Transactional
	@Override
	public List<Note> getTrashedNotes(String token) {
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);

			if (user != null) {
				System.out.println(user);
				List<Note> list = noteRepository.getTrashedNotes(userId);
				System.out.println("note fetched is" + " " + list.get(0));
				return list;

			} else {
				System.out.println(user + "hello");
				throw new UserException("note does not exist");
			}

		} catch (Exception e) {
			throw new UserException("error occured");
		}
	}

	@Transactional
	@Override
	public List<Note> getArchiveNote(String token) {
		List<Note> list;
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);

			if (user != null) {
				System.out.println(user);
				list = noteRepository.getArchiveNotes(userId);
				System.out.println("note fetched is" + " " + list.get(0).toString());

			} else {
				System.out.println(user + "hello");
				throw new UserException("note doesn't exist");
			}

		} catch (Exception e) {
			throw new UserException("error occured");
		}
		return list;
	}

	@Transactional
	@Override
	public boolean addColour(Long noteId, String token, String colour) {

		Long userid;

		try {
			userid = (long) tokenGenerator.parseJWT(token);
			System.out.println("user id" + " " + userid);
			Note note = noteRepository.findById(noteId);
			note.setColor(colour);
			noteRepository.createNote(note);

		} catch (Exception e) {
			throw new UserException("authentication failed");
		}
		return true;
	}

	@Transactional
	@Override
	public boolean addReminder(Long noteId, String token, ReminderDto reminder) {
		Long userid;

		try {
			userid = (long) tokenGenerator.parseJWT(token);
			System.out.println("user id" + " " + userid);
			Note note = noteRepository.findById(noteId);
			if (note != null) {
				System.out.println(note.getReminder());
				System.out.println(reminder);

				note.setReminder(reminder.getReminder());
				System.out.println(note.getColor());
				noteRepository.createNote(note);
			} else {
				throw new UserException("note doesn't exist");
			}

		} catch (Exception e) {
			throw new UserException("authentication failed");
		}
		return true;
	}

	@Transactional
	@Override
	public boolean removeReminder(Long noteId, String token, ReminderDto reminder) {
		Long userid;

		try {
			userid = (long) tokenGenerator.parseJWT(token);
			System.out.println("user id" + " " + userid);
			Note note = noteRepository.findById(noteId);
			if (note != null) {
				System.out.println(note.getReminder());
				System.out.println(reminder);

				note.setReminder(null);
				System.out.println(note.getColor());
				noteRepository.createNote(note);
			} else {
				throw new UserException("note doesn't exist");
			}

		} catch (Exception e) {
			throw new UserException("authentication failed");
		}
		return true;
	}

	@Transactional
	@Override
	public List<Note> getAllPinnedNotes(String token) {
		List<Note> allNotes;
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);
			if (user != null) {
				System.out.println("user logged in" + user.getUserId());
				System.out.println("user ");
				List<Note> list11 = noteRepository.getNotes(userId);
				System.out.println(list11);
			}
		} catch (Exception e) {
			throw new UserException("error occured");
		}
		return null;
	}

	@Transactional
	@Override
	public boolean restored(String token, Long noteId) {
		Long userId = (long) tokenGenerator.parseJWT(token);
		if (noteRepository.setRestored(userId, noteId)) {
			return true;
		}
		return false;
	}

	@Transactional
	@Override
	public boolean trashed(String token, Long noteId) {
		Long userId = (long) tokenGenerator.parseJWT(token);
		if (noteRepository.setTrashed(userId, noteId)) {
			return true;
		}
		return false;
	}
}