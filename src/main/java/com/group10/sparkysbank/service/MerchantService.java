package com.group10.sparkysbank.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.dao.PKITransactionDAO;
import com.group10.sparkysbank.dao.UserInfoDAO;
import com.group10.sparkysbank.model.PKITransaction;

@Service("merchantService")
public class MerchantService {

	@Autowired
	UserInfoDAO userInfoDAO;
	
	@Autowired
	PKITransactionDAO pkiTransactionDAO;
	
	@Transactional
	public ArrayList<String> getAllCustomers(){
		return userInfoDAO.getAllCustomers();
		//return null;
	}

	@Transactional
	public void submitPayment(PKITransaction transaction) {
		// TODO Auto-generated method stub
		pkiTransactionDAO.savePayment(transaction);
	}
	@Transactional
	public ArrayList<PKITransaction> getApprovedTransactions(String name) {
		// TODO Auto-generated method stub
		return pkiTransactionDAO.getApprovedTransactions(name);
	}
}
