package com.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import java.util.UUID;

@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {

	private static final long serialVersionUID = 8734140534986494039L;

	public Authorities() {
		this.ensureId();
	}

	@PrePersist
	private void ensureId(){
		this.setAuthorityId(UUID.randomUUID().toString());
	}
	
	@Id
	private String authorityId;
	private String emailId;
	private String authorities;

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRoleName() {
		return authorities;
	}

}
