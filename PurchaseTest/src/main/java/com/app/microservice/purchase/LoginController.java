package com.app.microservice.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.microservice.user.User;
import com.app.microservice.user.UserRepository;

@RestController
@RequestMapping(value="/auth")
public class LoginController {


	@Autowired
	UserRepository userDAO;
		
	@RequestMapping(value="/login",method=RequestMethod.POST)
	private ResponseEntity<User> loginUser(@RequestBody User userLogin){
		User user=null;
		user=userDAO.findByEmail(userLogin.getEmail());
		if(user==null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		if(!user.getPassword().equalsIgnoreCase(userLogin.getPassword())){
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}
		if(user.getAuthToken()!=0){
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		validateToken(user);
		userDAO.save(user);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	String userLogOut(@RequestBody User userLogout){
		User user=null;
		
			user=userDAO.findByEmail(userLogout.getEmail());
			if(user.getAuthToken()==0){
				return "You have already logged out.";
			}
			invalidateToken(user);
			userDAO.save(user);
		
		return "You are successfully logged out."; 
	}
	
	void invalidateToken(User user){
		user.setAuthToken(null);
	}
	
	void validateToken(User user){
		user.setAuthToken(user.hashCode());
	}
	
}

