package com.neu.cloudassign1.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;
import com.neu.cloudassign1.service.UserService;



@RestController
public class LoginController {

	private UserService userService;

	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	
	/**
     * InitBinder is a preprocessor used here to remove white spaces
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

	@RequestMapping(value= "/" , method = RequestMethod.GET)
    public ResponseEntity loginUser() {
        Map<String,String> timeMap= new HashMap<String,String>();
        timeMap.put("date",java.time.LocalDate.now().toString());
        timeMap.put("time",java.time.LocalTime.now().toString());
        return new ResponseEntity(timeMap,HttpStatus.OK);

    }
	
	@RequestMapping(value= "/user/register" , method = RequestMethod.POST, produces="application/json")
    public ResponseEntity<String> registerNewUser(@RequestBody User user) throws UserException {
        Map<String,String> messageMap= new HashMap<String,String>();
        if(userService.isUser(user.getEmail())){
        	System.out.println("\n\n\n**** 1 *****\n\n\n");
            messageMap.put("errorMessage","User Already Exists");
            return new ResponseEntity(messageMap, HttpStatus.FORBIDDEN);
        }
        try {
            if(user.getPassword().length()<8){
                messageMap.put("errrorMessage","Password must me atleast 8 character long");
                return new ResponseEntity(messageMap, HttpStatus.FORBIDDEN);
            }
            userService.saveUser(user);
            messageMap.put("successMessage","User Successfully Registered");
            return new ResponseEntity(messageMap, HttpStatus.OK);
        }catch(ConstraintViolationException ce) {
            messageMap.put("errorMessage","Invalid Email Address");
        	return new ResponseEntity(messageMap,HttpStatus.FORBIDDEN);
        }
        
    }
	
}
