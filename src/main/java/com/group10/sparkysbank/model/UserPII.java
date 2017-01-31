package com.group10.sparkysbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UserPII", catalog = "sparkysbankdb", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserPII {

	private String username;
	private String PII;
	private String token;
	@Id
	@Column(name="username",length=20)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="PII")
	public String getPII() {
		return PII;
	}
	public void setPII(String pII) {
		PII = pII;
	}
	@Column(name="token",length=2000)
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
