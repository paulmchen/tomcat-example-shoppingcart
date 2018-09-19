package com.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 2681531852204068105L;

	public User() {
		this.ensureId();
	}

	@PrePersist
	private void ensureId(){
		this.setUserId(UUID.randomUUID().toString());
	}

	@Id
	private String userId;
	private String emailId;
	private String password;
	private boolean enabled;

	@OneToOne(mappedBy = "users")
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUsername() {
		return emailId;
	}


}
