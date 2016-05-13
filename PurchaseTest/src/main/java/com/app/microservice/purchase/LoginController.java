package com.app.microservice.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
	private ResponseEntity<User> loginUser(@RequestParam String email,@RequestParam String password){
		User user=null;
		user=userDAO.findByEmail(email);
		System.out.println("######### Inside login, diaplying the details of user logged in :"+user);
		if(user==null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		if(!user.getPassword().equalsIgnoreCase(password)){
			System.out.println(" >>>>>>>>>>> Invalid password !!!");
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}
		if(user.getAuthToken()!=0){
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		user.setAuthToken(user.hashCode());
		userDAO.save(user);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	String userLogOut(@RequestParam String email){
		User user=null;
		
			user=userDAO.findByEmail(email);
			if(user.getAuthToken()==0){
				return "You have already logged out.";
			}
			user.setAuthToken(0);
			userDAO.save(user);
		
		return "You are successfully logged out."; 
	}
}
