package com.neu.cloudassign1.dao;

import com.neu.cloudassign1.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
