package com.bridgelabz.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.model.PasswordUpdate;
import com.bridgelabz.model.User;

@Repository
public class UserRepositoryImplementation implements IUserRepository {

	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Override
	public User save(User userInformation) {

		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(userInformation);
		return userInformation;

	}

	@Transactional
	@Override
	public User getUser(String email) {

		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery(" FROM User where email=:email");
		q.setParameter("email", email);
		return (User) q.uniqueResult();

	}

	@Transactional
	@Override
	public User getUserById(Long id) {
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery(" FROM User where userId=:userId");
		q.setParameter("userId", id);
		return (User) q.uniqueResult();

	}

	@Transactional
	@Override
	public boolean upDate(PasswordUpdate information, Long id) {

		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("update User set password=:password" + " " + " " + "where userId=:userId");
		q.setParameter("password", information.getConfirmPassword());
		q.setParameter("userId", id);

		int status = q.executeUpdate();
		if (status > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public boolean verify(Long id) {

		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("update User set is_verified=:is_verified" + " " + " " + "where userId=:userId");
		q.setParameter("is_verified", true);
		q.setParameter("userId", id);

		int status = q.executeUpdate();
		if (status > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Transactional
	@Override
	public List<User> getUsers() {
		Session currentsession = entityManager.unwrap(Session.class);
		List<User> usersList = currentsession.createQuery("from User").getResultList();
		return usersList;
	}
}