package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;

public interface UserService {

    public void saveUser(User user) throws UserException;

    public boolean isUser(String email);

    public String getPassword(String email);
    public User findUserByEmail(String email);
    public String resetPassword(String email);
    public String getTopicArn(String topicName);
    public String sendMessage(String email) throws java.util.concurrent.ExecutionException, java.lang.InterruptedException;
}
