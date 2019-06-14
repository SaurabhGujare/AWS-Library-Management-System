package com.neu.cloudassign1.dao;

import com.neu.cloudassign1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
