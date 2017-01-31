package com.group10.sparkysbank.dao;

import com.group10.sparkysbank.model.OneTimePad;

public interface OneTimePadDAO {

	public abstract OneTimePad findUserByUsername(String username);

	public abstract void createOTP(OneTimePad onetimepad) throws Exception;
	
	public void updateLoginAttempts(OneTimePad user);
	
	public void deleteUserByUsername(OneTimePad User);

}