package com.neu.cloudassign1.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class BookDAO {

    private EntityManager entityManager;

    @Autowired
    public BookDAO(EntityManager entityManager) {
        entityManager = entityManager;

    }
}
