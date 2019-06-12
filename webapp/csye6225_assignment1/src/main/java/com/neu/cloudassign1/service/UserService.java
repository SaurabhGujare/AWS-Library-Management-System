package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;

public interface UserService {

    public void saveUser(User user) throws UserException;

    public boolean isUser(String email);

    public String getPassword(String email);
}
