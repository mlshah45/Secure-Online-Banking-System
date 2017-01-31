package com.group10.sparkysbank.dao;

import java.util.ArrayList;

import com.group10.sparkysbank.model.PKITransaction;

public interface PKITransactionDAO {
public void savePayment(PKITransaction transaction);
public void updatePayment(PKITransaction transaction);
public ArrayList<PKITransaction> getTransactionForCustomer(String username);

public PKITransaction getTransactionFromTransactionId(Integer transactionId);

public ArrayList<PKITransaction> getApprovedTransactions(String name);
}
