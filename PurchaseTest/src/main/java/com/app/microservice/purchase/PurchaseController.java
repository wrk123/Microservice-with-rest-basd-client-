package com.app.microservice.purchase;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.app.microservice.user.User;
import com.app.microservice.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@RestController
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PurchaseController {

	@Autowired
	UserRepository userDAO;
	
	@Autowired
	PurchasesRepository purchaseDAO;
	
	@Autowired
	RestOperations restTemplate;
	
	LoginController userLog;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public @ResponseBody String homePage(){
		return "Welcome to purchase books.";
	}
	
	
	/* User related operations */
	
	//Method for creating users 
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> createUser(@RequestBody User userDetails) throws Exception{
		User user=null;
	
		//for checking update user 
		user=userDAO.findOne(userDetails.getId());
		if (user==null){
			//insert new user 
			try{
				user=new User(userDetails.getEmail(),userDetails.getName(),userDetails.getContact(),userDetails.getIsActive(), new Date(),new Date(),userDetails.getPassword(),0,userDetails.getUserType().toLowerCase());
				if(userDetails.getUserType().equalsIgnoreCase("admin"))
				{
					if(userDAO.findByUserType("admin")!=null)
						//throw new Exception("Cannot create two ADMIN users ");
						return new ResponseEntity<User>(HttpStatus.CONFLICT);
				}
				userDAO.save(user);
				}catch(Exception e){
					throw new Exception("Exception in saving user details...",e);
				}
		}else{  
				userDetails.setCreationTime(user.getCreationTime());
				userDetails.setLastModifiedTime(new Date());
				userDAO.save(userDetails);
			}
		
			return  new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
	//for fetching user details of a single user 
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<User> getOneUserDetails(@PathVariable Long id)throws Exception{
		User user=null;
			user=userDAO.findOne(id);
			if(user==null)
				return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			else
				{	if(user.getAuthToken()==0)
						throw new Exception("Cannot authenticate. Please login to view your details !!! ");
			}return new ResponseEntity<User>(user, HttpStatus.OK);
	}
		

    //for fetching all the users details 
	 @RequestMapping(value="/user/admin/all",method=RequestMethod.GET)
	 public @ResponseBody ResponseEntity<List<User>> getAllUsers(){
		 List<User> users=null;
		 try{
			 if(adminUserCheck()){
		 
			 users=(List<User>) userDAO.findAll();
			 return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		 }}catch(Exception e){
			 e.printStackTrace();
		 }
		 System.out.println(">>> You are not suthorized to view all user details.");
		 return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
	 }
	 
	 //for deleting the user details 
	@RequestMapping(value="/user/{id}",method=RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<User> deleteUser(@PathVariable Long id)throws Exception{
		User user=null;
		List<Purchases> purchase=null;
		user=userDAO.findOne(id);
		if(user!=null)
		{	
		 
			if(user.getAuthToken()==0){
				throw new Exception("Cannot authenticate. Please login to view your details !!! ");
			}
			try{	
					user.setLastModifiedTime(new Date());
					purchase=purchaseDAO.findByUserId(user.getId());
					if(user.getIsActive())	
						{	for(Purchases purch : purchase){			 						   //getting all the payments of that user and making it offline 
								purch.setLastModifiedTime(new Date());
								purch.setIsActive(false);
							}
							user.setIsActive(false);
							System.out.println(">>>>>>> User successfully made inActive !!!");
							userLog.userLogOut(user.getEmail());
							return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
						}
					else
						{	for(Purchases purch : purchase){
								purch.setLastModifiedTime(new Date());
								purch.setIsActive(true);
							}
							user.setIsActive(true);
						}
					
					userDAO.save(user);
					purchaseDAO.save(purchase);													//user status and his payment details also modified
					return new ResponseEntity<User>(user, HttpStatus.OK);
				}catch(Exception e){
					throw new Exception("Could not change status of the user. ",e);
				}
		}else
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND); 
	}
	
		
	//get user based on active or inactive
	@RequestMapping(value="/user/admin/isActive/{isActive}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<User>> getAllIsActiveUsers(@PathVariable boolean isActive)throws Exception{
		List<User> users=null;
		if(adminUserCheck()){
				users=userDAO.findByIsActive(isActive);
				return new  ResponseEntity<List<User>>(users,HttpStatus.OK);
			 }
		System.out.println(">>>> You are not authorized to view the details !!!");
		return new  ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
	}
	
	
	/* Purchase related operations  */
		
	//For purchasing books 
	@RequestMapping(value="/purchase",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Purchases> purchase(@RequestBody Purchases purchases)throws Exception{

		User user=null;
		Purchases purchase=null;
		Date creationDate=new Date();
		if(purchases.getOrderId()==null){

			//check whether book exists in the database 
			user=userDAO.findOne(purchases.getUserId());
			System.out.println("### Reached purchases "+user.toString());
			if(user.getAuthToken()==0){
				System.out.println(">>>>> You have not logged in to your account. Please login to purchase !!!");
				return new  ResponseEntity<Purchases>(HttpStatus.UNAUTHORIZED);
			}
			System.out.println("%%%% Checking of the users done ");
			JSONObject book=parseJSON(purchases);		
			String bookName=(String)book.get("bookName");
			Integer isbnId =(Integer) book.get("isbn"); 
			Long isbn= Long.parseLong(String.valueOf(isbnId));
			boolean isActive=(boolean)book.get("isActive");
			
			if(book==null || user==null){
				return new ResponseEntity<Purchases>(HttpStatus.NOT_FOUND);		
			}
			if(!user.getIsActive()){
				throw new Exception(" User with id : "+user.getId()+" not allowed to purchase books !!!");
			}
			if(!isActive){
				throw new Exception(" Book with name : "+bookName+" is not allowed to purchase !!!");
			}
			
			Optional<Purchases> purchaseOptional = purchaseDAO.findByIsbnAndUserId(isbn,user.getId());
			if(!purchaseOptional.isPresent()){
					purchase = new Purchases(null,creationDate,creationDate,purchases.getUserId(),purchases.getIsbn(),true,user,book);
					purchaseDAO.save(purchase);					
				}else{			
					return new ResponseEntity<Purchases>(HttpStatus.CONFLICT);		//book already purchases 				
				}	
		}else{				//first if checking 
			return new ResponseEntity<Purchases>(HttpStatus.CONFLICT);				//order id already created			
		}
		return new ResponseEntity<Purchases>(purchase, HttpStatus.OK);
	} 
		
	
	
	 //for fetching purchase details of a single user 
		@RequestMapping(value="/purchase/user/{userId}",method=RequestMethod.GET)
		public @ResponseBody ResponseEntity<List<Purchases>> getUserPurchaseDetails(@PathVariable Long userId)throws Exception{
			List<Purchases> purchase=null;
			User user=null;
			user=userDAO.findOne(userId);
			if(user==null){
				throw new Exception(" User with id : "+userId+" does not exists !!!"); 
			}
			if(user.getAuthToken()==0){
				System.out.println(">>>>> You have not logged in. Please login to view your purchase details !!!");
				return new  ResponseEntity<List<Purchases>>(HttpStatus.UNAUTHORIZED);
			}
			purchase=purchaseDAO.findByUserId(userId);
			if(purchase!=null){
				 for(Purchases  purch : purchase) {
					 purch.setBook(parseJSON(purch));
				  }
				 return new ResponseEntity<List<Purchases>>(purchase, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<List<Purchases>>(HttpStatus.NOT_FOUND);
			}
		}
		
		 //for fetching all the purchase details 	
		 @RequestMapping(value="/purchase/admin/all",method=RequestMethod.GET)
		 public @ResponseBody ResponseEntity<List<Purchases>> getAllPurchases(){
			 List<Purchases> purchase=(List<Purchases>) purchaseDAO.findAll();
			 User user=null;
			 try{
				 if(adminUserCheck()){ 
			 		 for(Purchases  purch : purchase) {
			 			 purch.setBook(parseJSON(purch));
			 			 user=userDAO.findOne(purch.getUserId());
			 			 purch.setUser(user);
			 		 }
				 return new ResponseEntity<List<Purchases>>(purchase, HttpStatus.OK);
			 }
			}catch(Exception e){e.printStackTrace();}
			
			 return new ResponseEntity<List<Purchases>>(HttpStatus.UNAUTHORIZED);
		 }
		 
		//for deleting the purchase details 
		@RequestMapping(value="/purchase/admin/orderId/{orderId}",method=RequestMethod.DELETE)
		public @ResponseBody ResponseEntity<Purchases> deletePurchaseDetails(@PathVariable Long orderId)throws Exception{
			Purchases purchase=null;
			if(adminUserCheck()){
				purchase=purchaseDAO.findOne(orderId);
				if(purchase!=null)
				{	purchase.setLastModifiedTime(new Date());
					if(purchase.getIsActive())
						purchase.setIsActive(false);
					else
						purchase.setIsActive(true);
						
					purchase.setBook(parseJSON(purchase));
					purchaseDAO.save(purchase);
					
					return new ResponseEntity<Purchases>(purchase, HttpStatus.OK);				
				}else
					return new ResponseEntity<Purchases>(HttpStatus.NOT_FOUND);  
			}else
				return new ResponseEntity<Purchases>(HttpStatus.UNAUTHORIZED);
		}
			
			//get books based on active or inactive
			@RequestMapping(value="/purchase/admin/isActive/{isActive}",method=RequestMethod.GET)
			public @ResponseBody ResponseEntity<List<Purchases>> getAllIsActivePurchases(@PathVariable boolean isActive)throws Exception{
				List<Purchases> purchase=purchaseDAO.findByIsActive(isActive);
				if(adminUserCheck()){
					 for(Purchases purch:purchase)	
							purch.setBook(parseJSON(purch));
						return new  ResponseEntity<List<Purchases>>(purchase,HttpStatus.OK);
				}				 
				System.out.println("You are not authorized to view details....");
				return new  ResponseEntity<List<Purchases>>(HttpStatus.UNAUTHORIZED);					
			}
			
			//to get the details of the book from the books  service running
			JSONObject parseJSON(Purchases purchase){
				StringBuilder url=new StringBuilder().append("http://localhost:8090/book/").append(purchase.getIsbn());
				JSONObject result=restTemplate.getForObject(url.toString(), JSONObject.class);			
				return result;
			}
			
			boolean adminUserCheck()throws Exception{
				User user=userDAO.findByUserType("admin");
				if(user==null){
					throw new Exception(" Admin user does not exists !!!");
				}
				if(user.getAuthToken()!=0)				
					return true;
				else return false;
			}
			
			
}
