package com.group10.sparkysbank.dao;

import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

public interface UseraccountsDao {

	Useraccounts getAccountByUsername(String userName) throws Exception;

	Useraccounts getUserAccount(Integer accountNo) throws Exception;

	void updateBalance(Useraccounts useraccounts) throws Exception;

}
