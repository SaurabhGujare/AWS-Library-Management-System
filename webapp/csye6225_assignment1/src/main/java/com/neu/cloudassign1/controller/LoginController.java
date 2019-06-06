package com.neu.cloudassign1.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.neu.cloudassign1.dao.UserDAO;
import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;



@RestController
public class LoginController {
	
	
	private UserDAO userDao;
	
	@Autowired
	public LoginController(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	/**
     * InitBinder is a preprocessor used here to remove white spaces
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @RequestMapping(value= "/hello" , method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String sayHello() {
    	return "Hello World";
    }
    
	@RequestMapping(value= "/" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String loginUser() {

            return  java.time.LocalDate.now().toString()+" "+java.time.LocalTime.now().toString();

    }
	
	@RequestMapping(value= "/user/register" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewUser(@RequestBody User user) throws UserException {
        if(userDao.isUser(user.getEmail())){
        	System.out.println("\n\n\n**** 1 *****\n\n\n");
            return new ResponseEntity("User Already Exists", HttpStatus.FORBIDDEN);
        }
        try {
        	userDao.saveUser(user);
            return new ResponseEntity("User Successfully Registered", HttpStatus.OK);
        }catch(ConstraintViolationException ce) {
        	return new ResponseEntity("Invalid Email Address",HttpStatus.FORBIDDEN);
        }
        
    }
	
}
