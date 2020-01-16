package com.bridgelabz.repository;

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
	public Note saveNote(Note note) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(note);
		return note;
	}

	@Transactional
	@Override
	public Note findById(long id) {
		Session session = entityManager.unwrap(Session.class);
		Query query = (Query) session.createQuery("from Note where id=:id");
		query.setParameter("id", id);
		return (Note) query.uniqueResult();
	}

	@Transactional
	@Override
	public boolean deleteNote(long id, long userid) {
		Session session = entityManager.unwrap(Session.class);
		String hql = "DELETE FROM Note " + "WHERE id = :id";
		Query query = session.createQuery(hql);
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
	public List<Note> getNotes(long userid) {

		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery(
				"from Note where id='" + userid + "'" + " and is_trashed=false and is_archieved=false ORDER BY id DESC")
				.list();
	}

	@Transactional
	@Override
	public List<Note> getTrashedNotes(long userid) {
		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);

		return session.createQuery("from Note where id='" + userid + "'" + " and is_trashed=true").list();
	}

	@Transactional
	@Override
	public List<Note> getArchiveNotes(long userid) {

		System.out.println("in repository");
		Session session = entityManager.unwrap(Session.class);

		return session
				.createQuery("from Note where id='" + userid + "'" + " and is_archieved=true" + " and is_trashed=false")
				.list();
	}

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
}
