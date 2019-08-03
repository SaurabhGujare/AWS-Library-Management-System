package com.neu.cloudassign1.service;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.neu.cloudassign1.repository.UserRepository;
import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;

@Service
public class UserServiceImplementation implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;
    private EntityManager entityManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AmazonSNSAsync amazonSNSClient;

    @Autowired
    private UserServiceImplementation(UserRepository userRepository, EntityManager entityManager, BCryptPasswordEncoder bCryptPasswordEncoder, AmazonSNSAsync amazonSNSClient){
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.amazonSNSClient = amazonSNSClient;
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

    @Override
    public User findUserByEmail(String email) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM User WHERE email=:email ");

        theQuery.setParameter("email",email);


        User u = (User) theQuery.getSingleResult();

        return u;
    }

//    @Override
//    public void sendMessage(String email) throws ExecutionException, InterruptedException {
//
//        logger.info("Sending Message - {} ", email);
//
//        String topicArn = getTopicArn("reset_password");
//        PublishRequest publishRequest = new PublishRequest(topicArn, email);
//        Future<PublishResult> publishResultFuture = amazonSNSClient.publishAsync(publishRequest);
//        String messageId = publishResultFuture.get().getMessageId();
//
//        logger.info("Send Message {} with message Id {} ", email, messageId);
//
//    }

//    public String getTopicArn(String topicName) {
//
//        String topicArn = null;
//
//        try {
//            Topic topic = amazonSNSClient.listTopicsAsync().get().getTopics().stream()
//                    .filter(t -> t.getTopicArn().contains(topicName)).findAny().orElse(null);
//
//            if (null != topic) {
//                topicArn = topic.getTopicArn();
//            } else {
//                logger.info("No Topic found by the name : ", topicName);
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        logger.info("Arn corresponding to topic name {} is {} ", topicName, topicArn);
//
//        return topicArn;
//
//    }
}
