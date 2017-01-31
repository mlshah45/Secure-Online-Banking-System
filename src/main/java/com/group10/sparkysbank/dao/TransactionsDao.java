package com.group10.sparkysbank.dao;

import java.util.List;
import java.util.Set;

import com.group10.sparkysbank.model.Transactions;

public interface TransactionsDao {

	public void createTransferTransaction(Transactions transactions) throws Exception;

	public void createCreditTransaction(Transactions transactions)throws Exception;

	public void createDebitTransaction(Transactions transactions)throws Exception;
	
	public Transactions findTransactionByID(int idtransactions);
	public List<Transactions> findTransactionListByAccountno(int accountno);
	public List<Transactions> findPendingTransactionList();
	public Transactions findViewableOrNot(int accno);
	public Transactions findEditableOrNot(int accno);
	public Transactions findTransViewableOrNot(int accno);
	public List<Transactions> getTransToBeApproved(String role);
	public Transactions updateTrans(Transactions trans);
	public List<Transactions> getExtUserReqList(String role);
	public void createViewExtProfileTrans(Transactions transactions)throws Exception;
	public int countOfReqForAcc(String type, int accno);
	public int countOfReq(String type, int accno);
	public int countOfReqTrans(String type, int accno);
	public List<Transactions> getViewProfileReqApproved(int accno);
}
