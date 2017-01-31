package com.group10.sparkysbank.service;

import com.group10.sparkysbank.model.OneTimePad;

public interface OneTimePadService {

	public abstract String encrypt(String message, String key);

	public abstract String decrypt(String message, String key);

	public abstract void generateOTP(String username);
	
	public abstract boolean otpVerify(String key, String username) throws Exception;

}