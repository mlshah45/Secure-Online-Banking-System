package com.group10.sparkysbank.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.PKITransaction;

@Repository("pkiTransactionDAO")
public class PKITransactionDAOImpl implements PKITransactionDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void savePayment(PKITransaction transaction) {
		// TODO Auto-generated method stub

		Session session =sessionFactory.getCurrentSession();
		session.save(transaction);
	}

	public ArrayList<PKITransaction> getTransactionForCustomer(String username) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from PKITransaction where customer=:c and transactionstatus=:s");
		query.setParameter("c", username);
		query.setParameter("s", "PENDING_CUSTOMER_ACTION");
		ArrayList<PKITransaction> list= (ArrayList<PKITransaction>) query.list();
		return list;
	}

	public PKITransaction getTransactionFromTransactionId(Integer transactionId) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(PKITransaction.class);
		criteria.add(Restrictions.eq("transactionid", transactionId));
		PKITransaction transaction= (PKITransaction) criteria.uniqueResult();

		return transaction;
	}

	public ArrayList<PKITransaction> getApprovedTransactions(String name) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from PKITransaction where merchant=:m and transactionstatus=:s");
		query.setParameter("m", name);
		query.setParameter("s", "APPROVED_FROM_CUSTOMER");

		ArrayList<PKITransaction> list=(ArrayList<PKITransaction>) query.list();
		return list;
	}

	public void updatePayment(PKITransaction transaction) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		session.update(transaction);
	}

}
