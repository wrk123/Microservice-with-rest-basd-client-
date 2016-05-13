package com.app.microservice.user;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByEmail(String email);
	
	List<User> findByIsActive(boolean isActive);
	
	User findByUserType(String userType);
	
}
