package com.group10.sparkysbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "onetimepad", catalog = "sparkysbankdb")
public class OneTimePad implements java.io.Serializable{

	public int id;
	public int login_attempts;
	public String username;
	public String otp;
	public long creationtime;
	public String enteredkey;

	@Column(name = "enteredkey", nullable = false,columnDefinition = "varchar(60) default 'nokey'")
	public String getEnteredkey() {
		return enteredkey;
	}

	public void setEnteredkey(String enteredkey) {
		this.enteredkey = enteredkey;
	}

	public OneTimePad(){
	}

	public OneTimePad(int id) {
		this.id = id;
	}
	
	public OneTimePad(int id, int login_attempts,String username,String otp,long creationtime,String enteredkey)
	{
		this.id=id;
		this.username=username;
		this.login_attempts=login_attempts;
		this.otp=otp;
		this.creationtime=creationtime;
		this.enteredkey=enteredkey;
	}

	@Column(name = "creationtime")
	public long getCreationtime() {
		return creationtime;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}

	public void setCreationtime(long creationtime) {
		this.creationtime = creationtime;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	public int getId() {
		return this.id;
	}
	
	
	@Column(name = "login_attempts")
	public int getLogin_attempts() {
		return login_attempts;
	}

	public void setLogin_attempts(int login_attempts) {
		this.login_attempts = login_attempts;
	}

	@Column(name = "username", unique = true, length = 45)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "otp", length = 60)
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
	
}
