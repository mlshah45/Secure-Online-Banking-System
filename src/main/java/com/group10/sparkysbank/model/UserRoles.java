package com.group10.sparkysbank.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles", catalog = "sparkysbankdb")
public class UserRoles implements Serializable {
	
	
	private String username;
	
	private String role;
	
	public UserRoles() {
		// TODO Auto-generated constructor stub
	}
	
	public UserRoles(String username,String role)
	{
		this.username=username;
		this.role=role;
	}
	@Id
	@Column(name="username",length=9)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="role_type")
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
