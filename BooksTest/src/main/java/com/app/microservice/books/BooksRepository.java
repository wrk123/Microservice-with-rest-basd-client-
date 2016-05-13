package com.app.microservice.books;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BooksRepository extends CrudRepository<Books, Long>{

	
	List<Books> findAll();
	
	List<Books> findByIsActive(boolean isActive);
	
	
}
