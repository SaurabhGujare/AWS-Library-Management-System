package com.neu.cloudassign1.dao;

import com.neu.cloudassign1.model.CoverImage;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ImageDAO {
	
	
    private EntityManager entityManager;
    private BookDAO bookDAO;

    @Autowired
    public ImageDAO(EntityManager entityM, BookDAO bookDAO) {
        entityManager = entityM;
        this.bookDAO = bookDAO;
    }

    @Transactional
    public void saveCoverImage(CoverImage i) {

        Session currentSession = entityManager.unwrap(Session.class);

        currentSession.saveOrUpdate(i);

    }

    @Transactional
    public List<CoverImage> getAllCoverImage() {

        Session currentSession = entityManager.unwrap(Session.class);
        
        Query theQuery = currentSession.createQuery("FROM Book");

        List<CoverImage> list = theQuery.getResultList();

        return list;

    }

    @Transactional
    public CoverImage getCoverImageByBookId(String id) {

        Session currentSession = entityManager.unwrap(Session.class);
        
        Query theQuery = currentSession.createQuery("FROM CoverImage WHERE bookId=:id ");

        theQuery.setParameter("id", id);
                
        CoverImage image = (CoverImage) theQuery.getSingleResult();
        
        return image;
    }

}
