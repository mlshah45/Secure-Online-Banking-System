package com.group10.sparkysbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UserPKI", catalog = "sparkysbankdb", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserPKI {

	private String username;
	private byte[] publickey;
	private String token;
	
	public UserPKI() {
		// TODO Auto-generated constructor stub
	}
	
	public UserPKI(String username,byte[] publickey,String token)
	{
		this.username=username;
		this.publickey=publickey;
		this.token=token;
	}
	@Id
	@Column(name = "username", unique = true, length = 45)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "publickey", unique = true)
	public byte[] getPublickey() {
		return this.publickey;
	}
	public void setPublickey(byte[] publickey) {
		this.publickey = publickey;
	}
	
	@Column(name="token",length=60)
	public String getToken(){
		return this.token;
	}
	public void setToken(String token){
		this.token=token;
	}
	
}
