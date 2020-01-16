package com.bridgelabz.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;

@Repository
public class LabelRepositoryImplementation implements ILabelRepository {

	@Autowired
	EntityManager entityManager;

	@Override
	public Label save(Label labelInformation) {
		Session session = entityManager.unwrap(Session.class);
		session.save(labelInformation);
		return labelInformation;
	}

	@Override
	public Note saveNote(Note noteInformation) {
		Session session = entityManager.unwrap(Session.class);
		session.save(noteInformation);
		return noteInformation;
	}

	@Override
	public Label fetchLabel(Long userid, String labelname) {
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("from Label where user_id=:id and name=:name");
		q.setParameter("id", userid);
		q.setParameter("name", labelname);
		return (Label) q.getResultList();
	}

	@Override
	public Label getLabel(Long id) {
		String hql = "FROM Label " + "WHERE label_id=:id";
		Session session = entityManager.unwrap(Session.class);
		return (Label) session.createQuery("from Label where label_id='" + id + "'").uniqueResult();
	}

	@Override
	public Label update(Integer labelId, Label label, String token) {
		Session currentSession = entityManager.unwrap(Session.class);
		Label labelObj = currentSession.get(Label.class, labelId);
		if (labelObj != null) {
			currentSession.update(label);
			return label;
		}
		return labelObj;
	}

	@Override
	public Label fetchLabelById(long id) {
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("from Label where label_id=:id");
		q.setParameter("id", id);
		return (Label) q.getResultList();
	}

	@Override
	public int deleteLabel(long i) {
		String hql = "DELETE FROM Label " + "WHERE label_id =:id";
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery(hql);
		query.setParameter("id", i);
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public List<Label> getAllLabel(long id) {
		String hql = "FROM Label " + "WHERE user_id=:id";
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery("from Label where user_Id='" + id + "'").getResultList();
	}
}