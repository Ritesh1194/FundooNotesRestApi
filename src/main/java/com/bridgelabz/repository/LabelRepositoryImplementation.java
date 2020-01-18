package com.bridgelabz.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;

@Repository
public class LabelRepositoryImplementation implements ILabelRepository {

	@Autowired
	EntityManager entityManager;

	@Transactional
	@Override
	public Label saveLabel(Label labelInformation) {
		Session session = entityManager.unwrap(Session.class);
		session.save(labelInformation);
		return labelInformation;
	}

	@Transactional
	@Override
	public Note saveNote(Note noteInformation) {
		Session session = entityManager.unwrap(Session.class);
		session.save(noteInformation);
		return noteInformation;
	}

	@Transactional
	@Override
	public Label fetchLabel(Long userId, String name) {
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("from Label where user_id=:user_id and name=:name");
		q.setParameter("user_id", userId);
		q.setParameter("name", name);
		return (Label) q.uniqueResult();
	}

	@Transactional
	@Override
	public Label getLabel(Long id) {
		String hql = "FROM Label " + "WHERE label_id=:label_id";
		Session session = entityManager.unwrap(Session.class);
		return (Label) session.createQuery("from Label where label_id='" + id + "'").uniqueResult();
	}

	@Transactional
	@Override
	public Label updateLabel(Integer labelId, Label label, String token) {
		Session currentSession = entityManager.unwrap(Session.class);
		Label labelObj = currentSession.get(Label.class, labelId);
		if (labelObj != null) {
			currentSession.update(label);
			return label;
		}
		return labelObj;
	}

	@Transactional
	@Override
	public Label fetchLabelById(long id) {
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("from Label where label_id=:id");
		q.setParameter("id", id);
		return (Label) q.uniqueResult();
	}

//	@Override
//	public int deleteLabel(long i) {
//		String hql = "DELETE FROM Label " + "WHERE label_id =:label_id";
//		Session session = entityManager.unwrap(Session.class);
//		Query query = session.createQuery(hql);
//		query.setParameter("label_id", i);
//		int result = query.executeUpdate();
//		return result;
//	}
	@Transactional
	@Override
	public List<Label> getAllLabel(long id) {
		String hql = "FROM Label " + "WHERE userId=:userId";
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery("from Label where userId='" + id + "'").getResultList();
	}
}