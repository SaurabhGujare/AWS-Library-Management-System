package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;

import java.util.concurrent.ExecutionException;

public interface UserService {

    public void saveUser(User user) throws UserException;

    public boolean isUser(String email);

    public String getPassword(String email);
    public User findUserByEmail(String email);
    public void sendMessage(String email) throws ExecutionException, InterruptedException;
}
