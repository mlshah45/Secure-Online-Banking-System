package com.group10.sparkysbank.model;


// default package
// Generated Oct 26, 2014 3:05:06 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Useraccounts generated by hbm2java
 */
@Entity
@Table(name = "useraccounts", catalog = "sparkysbankdb")
public class Useraccounts implements java.io.Serializable {

	private Integer accountno;
	private String username;
	private String routingno;
	private String wiringno;
	private Date accountopendate;
	private double balance;

	public Useraccounts() {
	}

	public Useraccounts(Integer accountno) {
		this.accountno = accountno;
	}

	public Useraccounts(Integer accountno, String username, String routingno,
			String wiringno, Date accountopendate, double balance) {
		this.accountno = accountno;
		this.username = username;
		this.routingno = routingno;
		this.wiringno = wiringno;
		this.accountopendate = accountopendate;
		this.balance = balance;

	}

	@Id
	@Column(name = "accountno", unique = true, nullable = false, length = 50)
	@GeneratedValue
	public Integer getAccountno() {
		return this.accountno;
	}

	public void setAccountno(Integer accountno) {
		this.accountno = accountno;
	}

	@Column(name = "routingno", length = 45)
	public String getRoutingno() {
		return this.routingno;
	}

	public void setRoutingno(String routingno) {
		this.routingno = routingno;
	}

	@Column(name = "wiringno", length = 45)
	public String getWiringno() {
		return this.wiringno;
	}

	public void setWiringno(String wiringno) {
		this.wiringno = wiringno;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "accountopendate", length = 10)
	public Date getAccountopendate() {
		return this.accountopendate;
	}

	public void setAccountopendate(Date accountopendate) {
		this.accountopendate = accountopendate;
	}

	@Column(name = "balance")
	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}