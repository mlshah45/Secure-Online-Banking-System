package com.group10.sparkysbank.service;

public interface EmailService {

	public abstract void sendEmail(String key, String emailId);
	public boolean sendEmailWithAttachment(String username,String emailId,String decodedPwd, String token);
	String  generatePassword();
}