package com.app.microservice.books;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Home page controller.
 * 
 * @author Paul Chapman
 */
@Controller
public class HomeController {
	
	 @RequestMapping("/")
	  @ResponseBody
	  public String index() {
	    return "Welcome to books purchase page.";
	  }
}
