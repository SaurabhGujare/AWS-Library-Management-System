package com.neu.cloudassign1.dao;

import javax.persistence.Query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;
import java.util.UUID;



@Repository
public class UserDAO {
	
	
	private EntityManager entityManager;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserDAO(EntityManager entityM, BCryptPasswordEncoder bCryptPasswordEncoder) {
		entityManager = entityM;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Transactional
	public void saveUser(User user) throws UserException {
		try {
			Session currentSession = entityManager.unwrap(Session.class);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			currentSession.save(user);

		}catch(ConstraintViolationException ce) {
			throw ce;
		}catch(Exception e) {
			throw new UserException("Exception while getting the user "+e.getMessage());
		}
		
		
	}
	
	@Transactional
	public boolean isUser(String email) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query theQuery = currentSession.createQuery("FROM User WHERE email=:email ");
		theQuery.setParameter("email",email);
		List<User> list = theQuery.getResultList();
		if(list.isEmpty())
			return false;
		
		return true ;

	}
	
	@Transactional
	public String getPassword(String email) {
		
		Session currentSession = entityManager.unwrap(Session.class);
		
		Query theQuery = currentSession.createQuery("FROM User WHERE email=:email ");

		theQuery.setParameter("email",email);
		
		
		User u = (User) theQuery.getSingleResult();
		
		return u.getPassword();	
		
	}
	
	

}
