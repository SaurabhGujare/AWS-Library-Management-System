package com.neu.cloudassign1.dao;


import com.neu.cloudassign1.model.Book;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookDAO {
    private EntityManager entityManager;

    @Autowired
    public BookDAO(EntityManager entityM) {
        entityManager = entityM;
    }

    @Transactional
    public void saveBook(Book b) {


        Session currentSession = entityManager.unwrap(Session.class);

        currentSession.saveOrUpdate(b);
    }

    @Transactional
    public boolean isBook(String title) {

        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM Book WHERE title=:unm ");
        theQuery.setParameter("unm", title);

        List<Book> list = theQuery.getResultList();

        if(list.isEmpty())
            return false;

        return true ;

    }

    @Transactional
    public List<Book> showBooks() {

        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM Book");

        List<Book> list = theQuery.getResultList();

        return list;

    }

    @Transactional
    public Book getBookById(String id) {

/*		Session currentSession = entityManager.unwrap(Session.class);

		Book book = currentSession.get(Book.class, id);

		return book;*/

        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM Book");

        List<Book> list = theQuery.getResultList();

        Book bb = new Book();

        for(Book b: list){
            if(b.getId().toString().equals(id)) {
                bb.setId(b.getId());
                bb.setTitle(b.getTitle());
                bb.setAuthor(b.getAuthor());
            }
        }

        return bb;


    }

    @Transactional
    public void deleteBookByTitle(String title) {

        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("delete From Book WHERE title=:unm ");

        theQuery.setParameter("unm", title);

        theQuery.executeUpdate();
    }

}
