package com.neu.cloudassign1.controller;

import com.neu.cloudassign1.exception.UserException;
import com.neu.cloudassign1.model.User;
import com.neu.cloudassign1.service.UserService;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;



@RestController
public class LoginController {

	private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatsDClient statsDClient;

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

        statsDClient.incrementCounter("endpoint./.http.get");

        Map<String,String> timeMap= new HashMap<String,String>();
        timeMap.put("date",java.time.LocalDate.now().toString());
        timeMap.put("time",java.time.LocalTime.now().toString());
        logger.info("User login: Success");
        return new ResponseEntity(timeMap,HttpStatus.OK);

    }
	
	@RequestMapping(value= "/user/register" , method = RequestMethod.POST, produces="application/json")
    public ResponseEntity<String> registerNewUser(@RequestBody User user) throws UserException {

        statsDClient.incrementCounter("endpoint.user.register.http.post");
        logger.info("Create User: Start");

        Map<String,String> messageMap= new HashMap<String,String>();
        if(userService.isUser(user.getEmail())){
        	System.out.println("\n\n\n**** 1 *****\n\n\n");
            messageMap.put("errorMessage","User Already Exists");
            logger.error("Create User: Failure: User Already Exists");
            return new ResponseEntity(messageMap, HttpStatus.FORBIDDEN);
        }
        try {
            if(user.getPassword().length()<8){
                messageMap.put("errrorMessage","Password must me atleast 8 character long");
                logger.error("Create User: Failure: Password must me atleast 8 character long");
                return new ResponseEntity(messageMap, HttpStatus.FORBIDDEN);
            }
            userService.saveUser(user);
            messageMap.put("successMessage","User Successfully Registered");
            logger.info("Create User: Success");
            logger.info("Create User: Stop");
            return new ResponseEntity(messageMap, HttpStatus.OK);
        }catch(ConstraintViolationException ce) {
            messageMap.put("errorMessage","Invalid Email Address");
            logger.error("Create User: Failure: Invalid Email Address");
        	return new ResponseEntity(messageMap,HttpStatus.FORBIDDEN);
        }
        
    }

    @RequestMapping(value="/reset", method = RequestMethod.GET)
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email){

        statsDClient.incrementCounter("endpoint.reset.http.get");
        logger.info("generateResetToken - Start ");

        Map<String,String> messageMap= new HashMap<String,String>();

        try
        {
            User user = userService.findUserByEmail(email);
            if(user != null)
            {
                userService.sendMessage(email);
            }
            messageMap.put("Success","Password reset email sent");
            logger.info("generateResetToken - End ");
            return new ResponseEntity(messageMap, HttpStatus.CREATED);

        }
        catch (Exception e)
        {
            logger.error("Exception in generating reset token : " + e.getMessage());
            messageMap.put("Reset email failed",e.getMessage());
            return new ResponseEntity(messageMap,HttpStatus.BAD_REQUEST);
        }

    }
	
}
