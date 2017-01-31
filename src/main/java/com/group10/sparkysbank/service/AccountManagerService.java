package com.group10.sparkysbank.service;

import com.group10.sparkysbank.model.Useraccounts;

public interface AccountManagerService {

	public Useraccounts getUserAccountForUserName(String userName) throws Exception;
	public void updateBalance(Integer accountNo, double amount, String type) throws Exception;
	public Useraccounts getUserNameForAccount(int accountno) throws Exception;
}
