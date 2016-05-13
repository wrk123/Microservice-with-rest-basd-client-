package com.app.microservice.books;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BooksController {

	@Autowired
	BooksRepository booksDAO;
	
	//Method for creating books 
	@RequestMapping(value="/book",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Books> createBooks(@RequestBody Books bookDetails) throws Exception{
		Books book=null;
		
		//for checking duplicate entry of books
		book=booksDAO.findOne(bookDetails.getIsbn());
		if(book==null){
			//create a new book entry 
			try{
				book=new Books(bookDetails.getBookName(),bookDetails.getBookDescription(),bookDetails.getPrice(),bookDetails.getIsActive(),new Date(),new Date());
				booksDAO.save(book);
				}catch(Exception e){
					throw new Exception("Exception in saving book details...",e);
				}
		}else{
			  bookDetails.setCreationTime(book.getCreationTime());
			  bookDetails.setLastModifiedTime(new Date());
			  booksDAO.save(bookDetails);
		}
			return new ResponseEntity<Books>(book, HttpStatus.OK);
	}
		
	//for fetching book details of a single book 
		@RequestMapping(value="/book/{isbn}",method=RequestMethod.GET)
		public @ResponseBody ResponseEntity<Books> getOneBookDetails(@PathVariable Long isbn)throws Exception{
			Books book=null;
			
				book=booksDAO.findOne(isbn);
				if(book!=null)
					return new ResponseEntity<Books>(book, HttpStatus.OK);
				else {
					return new ResponseEntity<Books>(HttpStatus.NOT_FOUND);
				}
		}
		
		//for fetching all the book details
		 @RequestMapping(value="/book/all",method=RequestMethod.GET)
		 public @ResponseBody ResponseEntity<List<Books>> getAllBooks(){
			 List<Books> books=(List<Books>) booksDAO.findAll();
			 return new ResponseEntity<List<Books>>(books,HttpStatus.OK);
		 }
		
		//for deleting the book details 
		@RequestMapping(value="/book/{isbn}",method=RequestMethod.DELETE)
		public @ResponseBody ResponseEntity<Books> deleteBookDetails(@PathVariable Long isbn)throws Exception{
			Books book=null;
			book=booksDAO.findOne(isbn);
			if(book!=null)
			{	try{	
						book.setLastModifiedTime(new Date());
						if(book.getIsActive())
							book.setIsActive(false);
						else
							book.setIsActive(true);
						booksDAO.save(book);
						return new ResponseEntity<Books>(book, HttpStatus.OK);
					}catch(Exception e){
						throw new Exception("Could not change the status of book. ",e);
					}
			}else
				return new ResponseEntity<Books>(HttpStatus.NOT_FOUND); 
		}
		
		//get books based on active or inactive
		@RequestMapping(value="/book/isActive/{isActive}",method=RequestMethod.GET)
		public @ResponseBody ResponseEntity<List<Books>> getAllIsActiveBooks(@PathVariable boolean isActive)throws Exception{
			List<Books> book=null;
			book=booksDAO.findByIsActive(isActive);				
				return new  ResponseEntity<List<Books>>(book,HttpStatus.OK);
		}
}
