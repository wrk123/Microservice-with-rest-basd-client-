package com.app.microservice.user;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "userDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="user_id")
	  private long id;
	  
	  @NotNull
	  @Column(name="email")
	  private String email;
	  
	  @NotNull
	  private String name;

	  @NotNull
	  private String contact;
	  
	  @NotNull
	  private boolean isActive;
	  
	  @Temporal(TemporalType.TIMESTAMP)
	  private Date creationTime;
		
	  @Temporal(TemporalType.TIMESTAMP)
	  private Date lastModifiedTime;
	
	  @NotNull
	  @Column(name="user_password")
	  private String password;
	  
	  @Column(name="auth_token")
	  private Integer authToken;
	  
	  @Column(name="user_type")
	  private String userType;
	 	
	  public User() {
		// TODO Auto-generated constructor stub
	  }
	  
	 

	public User( String email, String name, String contact, boolean isActive, Date creationTime,
			Date lastModifiedTime, String password, String userType) {
		super();
		this.email = email;
		this.name = name;
		this.contact = contact;
		this.isActive = isActive;
		this.creationTime = creationTime;
		this.lastModifiedTime = lastModifiedTime;
		this.password = password;
		this.userType=userType;
	}



	public long getId() {
			return id;
		}
		
		public void setId(long id) {
			this.id = id;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getContact() {
			return contact;
		}
		
		public void setContact(String contact) {
			this.contact = contact;
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
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public Integer getAuthToken() {
			return authToken;
		}
		
		public void setAuthToken(Integer authToken) {
			this.authToken = authToken;
		}
			
		public static long getSerialversionuid() {
			return serialVersionUID;
		}


		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", email=" + email + ", name=" + name + ", contact=" + contact + ", isActive="
					+ isActive + ", creationTime=" + creationTime + ", lastModifiedTime=" + lastModifiedTime
					+ ", password=" + password + ", authToken=" + authToken + ", userType=" + userType + "]";
		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((email == null) ? 0 : email.hashCode());
			result = prime * result + ((password == null) ? 0 : password.hashCode());
			return result;
		}



		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			User other = (User) obj;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			if (password == null) {
				if (other.password != null)
					return false;
			} else if (!password.equals(other.password))
				return false;
			return true;
		}
		
		
	  
		  
}
