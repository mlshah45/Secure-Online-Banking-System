package com.group10.sparkysbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.dao.UserInfoDAO;
import com.group10.sparkysbank.dao.UseraccountsDao;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Service("accountManagerService")
public class AccountManagerServiceImpl implements AccountManagerService {

	@Autowired
	private UseraccountsDao useraccountsDao;
	
	@Transactional
	public Useraccounts getUserAccountForUserName(String userName)
			throws Exception {
		// TODO Auto-generated method stub
		
		Useraccounts useraccounts=useraccountsDao.getAccountByUsername(userName);
		
		return useraccounts;
	}
	
	@Transactional
	public void updateBalance(Integer accountNo, double amount, String type) throws Exception
	{
		Useraccounts useraccounts=useraccountsDao.getUserAccount(accountNo);
		if(type.equals("debit"))
		{
			if(useraccounts.getBalance() < amount)
			{
				throw new Exception("Insufficient funds!");
			}
			
			useraccounts.setBalance(useraccounts.getBalance() - amount);
		}
		else if(type.equals("credit"))
		{
			useraccounts.setBalance(useraccounts.getBalance()+amount);
		}
		
		useraccountsDao.updateBalance(useraccounts);
			
	}
	
	@Transactional
	public Useraccounts getUserNameForAccount(int accountno)
			throws Exception {
		// TODO Auto-generated method stub
		
		Useraccounts useraccounts=useraccountsDao.getUserAccount(accountno);
		
		return useraccounts;
	}

}
