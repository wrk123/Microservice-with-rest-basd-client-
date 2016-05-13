package com.app.microservice.purchase;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.json.simple.JSONObject;

import com.app.microservice.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="purchases")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Purchases implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;
	
	@Column(name="user_id")
	private Long userId;
	
	//@Column(name="isbn")
	private Long isbn;
	
	//@Column(name="isActive")
	private boolean isActive;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",insertable=false, updatable=false)
	private User user;
	
	@Transient
	@JoinColumn(name="isbn",insertable=false, updatable=false)
	private JSONObject book;
	
	public Purchases() { }

	public Purchases(Long orderId, Date creationTime, Date lastModifiedTime, Long userId, Long isbn, boolean isActive,
			User user, JSONObject book) {
		super();
		this.orderId = orderId;
		this.creationTime = creationTime;
		this.lastModifiedTime = lastModifiedTime;
		this.userId = userId;
		this.isbn = isbn;
		this.isActive = isActive;
		this.user = user;
		this.book = book;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Object getBook() {
		return book;
	}

	public void setBook(JSONObject book) {
		this.book = book;
	}

	@Override
	public String toString() {
		return "Purchases [orderId=" + orderId + ", creationTime=" + creationTime + ", lastModifiedTime="
				+ lastModifiedTime + ", userId=" + userId + ", isbn=" + isbn + ", isActive=" + isActive + ", user="
				+ user + ", book=" + book + "]";
	}
	
}
