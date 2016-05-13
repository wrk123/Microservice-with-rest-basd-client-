package com.app.microservice.purchase;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasesRepository extends CrudRepository<Purchases, Long>{
	
	
	List<Purchases> findByUserId(Long userId);
	
	List<Purchases> findByIsbn(Long isbn);
	
	public Optional<Purchases> findByIsbnAndUserId(Long isbn,Long userId);
	
	List<Purchases> findByIsActive(boolean isActive);

}
