package com.group10.sparkysbank.service;

import com.group10.sparkysbank.model.Transactions;

public interface FundManagementService {
	public void createTransferTransaction(Transactions transactions) throws Exception;

	public void createCreditTransaction(Transactions transactions) throws Exception;

	public void createDebitTransaction(Transactions transactions)throws Exception;

}
