package com.bridgelabz.repository;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.model.Note;

@Repository
public class NoteRepositoryImplementation implements INoteRepository {
	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Override
	public Note createNote(Note note) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(note);
		return note;
	}

	@Transactional
	@Override
	public Note findById(long noteId) {
		Session session = entityManager.unwrap(Session.class);
		Query query = (Query) session.createQuery("from Note where noteId=:noteId");
		query.setParameter("noteId", noteId);
		return (Note) query.uniqueResult();
	}

	@Transactional
	@Override
	public boolean deleteNote(long id, Note note) {
		System.out.println("Delete Node3");
		Session session = entityManager.unwrap(Session.class);
		String hql = "DELETE FROM Note " + "WHERE noteId = :noteId";
		Query query = session.createQuery(hql);
		query.setParameter("noteId", id);
		int result = query.executeUpdate();
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public List<Note> getNotes(long userid) {
		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery("from Note where noteId='" + userid + "'"
				+ " and is_trashed=false and is_archieved=false ORDER BY id DESC").getResultList();
	}

	@Transactional
	@Override
	public List<Note> getTrashedNotes(long userid) {
		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery("from Note where user_id='" + userid + "'" + " and is_trashed=true").getResultList();
	}

	@Transactional
	@Override
	public List<Note> getArchiveNotes(long userid) {

		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);

		return session
				.createQuery(
						"from Note where userId='" + userid + "'" + " and is_archieved=true" + " and is_trashed=false")
				.getResultList();
	}

	@Transactional
	@Override
	public boolean updateColor(long id, long userid, String colour) {

		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("update Note  u set u.color = :color" + " where u.id = :id");

		query.setParameter("color", "red");
		query.setParameter("id", id);
		int result = query.executeUpdate();

		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public List<Note> getNotesOfSameLabel(Long userId, Long labelId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Note> query = currentSession.createQuery("from Note where user_id" + userId + "and labelId=" + labelId,
				Note.class);
		return query.getResultList();
	}

	@Transactional
	@Override
	public boolean setRestored(Long userId, Long noteId) {
		System.out.println();
		Session currentSession = entityManager.unwrap(Session.class);
		Note note = findById(noteId);
		if (note.getUserId().equals(userId)) {
			if (note.isTrashed()) {
				note.setTrashed(false);
				note.setCreatedDateAndTime(LocalDateTime.now());
				currentSession.saveOrUpdate(note);
				return true;
			}
			return false;
		}

		return false;
	}

	@Override
	public boolean setTrashed(Long userId, Long noteId) {
		Session currentSession = entityManager.unwrap(Session.class);
		System.out.println(noteId);
		Note note = findById(noteId);
		System.out.println("Note Trashed" + note.getUserId());
		if (note.getUserId().equals(userId)) {
			System.out.println("First If Condition");
			if (!note.isTrashed()) {
				note.setTrashed(true);
				note.setCreatedDateAndTime(LocalDateTime.now());
				currentSession.save(note);
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean setRemaineder(Long userId, Long noteId, LocalDateTime time) {
		Session currentSession = entityManager.unwrap(Session.class);
		Note note = findById(noteId);
		if (note.getUserId().equals(userId)) {
			note.setReminder(time);
			;
			currentSession.save(note);
			return true;
		}
		return false;
	}

}
