package com.neu.cloudassign1.service;

import com.neu.cloudassign1.repository.UserRepository;
import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private EntityManager entityManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserServiceImplementation(UserRepository userRepository, EntityManager entityManager, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void saveUser(User user) throws UserException {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);

        }catch(ConstraintViolationException ce) {
            throw ce;
        }catch(Exception e) {
            throw new UserException("Exception while getting the user "+e.getMessage());
        }

    }

    @Override
    public boolean isUser(String email) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery = currentSession.createQuery("FROM User WHERE email=:email ");
        theQuery.setParameter("email",email);
        List<User> list = theQuery.getResultList();
        if(list.isEmpty())
            return false;
        return true ;

    }

    @Override
    public String getPassword(String email) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM User WHERE email=:email ");

        theQuery.setParameter("email",email);


        User u = (User) theQuery.getSingleResult();

        return u.getPassword();
    }
}
