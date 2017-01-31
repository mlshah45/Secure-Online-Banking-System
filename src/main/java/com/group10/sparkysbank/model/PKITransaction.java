package com.group10.sparkysbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "pkitransaction", catalog = "sparkysbankdb", uniqueConstraints = @UniqueConstraint(columnNames = "transactionid"))
public class PKITransaction {

	private Integer transactionid;
	private String merchant;
	private String customer;
	private String transactionstatus;
	private Double amount;
	private String merchantToken;
	private String customerToken;

	@Id
	@Column(name = "transactionid")
	@GeneratedValue
	public Integer getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(Integer transactionid) {
		this.transactionid = transactionid;
	}
	@Column(name="merchant")
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	@Column(name="customer")
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	@Column(name="status")
	public String getTransactionstatus() {
		return transactionstatus;
	}
	public void setTransactionstatus(String transactionstatus) {
		this.transactionstatus = transactionstatus;
	}
	@Column(name="amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	@Column(name="merchanttoken", length=2000)
	public String getMerchantToken() {
		return merchantToken;
	}
	public void setMerchantToken(String merchantToken) {
		this.merchantToken = merchantToken;
	}
	@Column(name="customertoken",length=2000)
	public String getCustomerToken() {
		return customerToken;
	}
	public void setCustomerToken(String customerToken) {
		this.customerToken = customerToken;
	}

}
