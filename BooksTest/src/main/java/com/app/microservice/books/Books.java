package com.app.microservice.books;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="books")
public class Books implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="isbn")
	private long isbn;
	
	@NotNull
	private String bookName;
	
	@NotNull
	private String bookDescription;
	
	@NotNull
	private String price;
	
	@NotNull
	private boolean isActive;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;
	
	
	public Books() {	}
  
	public Books(long isbn){
		this.isbn=isbn;
	}
	
	public Books(String bookName, String bookDescription, String price, boolean isActive, Date creationTime,
			Date lastModifiedTime) {
		this.bookName = bookName;
		this.bookDescription = bookDescription;
		this.price = price;
		this.isActive = isActive;
		this.creationTime = creationTime;
		this.lastModifiedTime = lastModifiedTime;
	}
	
	

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookDescription() {
		return bookDescription;
	}

	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public String toString() {
		return "Books [isbn=" + isbn + ", bookName=" + bookName + ", bookDescription=" + bookDescription + ", price="
				+ price + ", isActive=" + isActive + ", creationTime=" + creationTime + ", lastModifiedTime="
				+ lastModifiedTime + "]";
	}
}
