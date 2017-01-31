package com.group10.sparkysbank.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.model.Transactions;

@Repository("transactionsDao")
public class TransactionsDaoImpl implements TransactionsDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void createTransferTransaction(Transactions transactions)
			throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(transactions);		

	}

	public void createCreditTransaction(Transactions transactions)
			throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(transactions);
	}

	public void createDebitTransaction(Transactions transactions)
			throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(transactions);

		
	}
	
	public Transactions findTransactionByID(int idtransactions) {
		Session session=sessionFactory.getCurrentSession();
		//session.beginTransaction();
		Criteria criteria=session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("idtransactions", idtransactions));
		Transactions trans=(Transactions)criteria.uniqueResult();
		return trans;
	}

	public List<Transactions> findTransactionListByAccountno(int accno) {
		Session session=sessionFactory.getCurrentSession();
		//session.beginTransaction();
		Query query=session.createQuery("from Transactions where fromAccount =:ano and status =:statusno");
		query.setParameter("ano", accno);
		query.setParameter("statusno", 1);
		List transList = query.list(); 		

		return transList;
	}

	public List<Transactions> findPendingTransactionList() {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where approvalNeeded =:approvalneed");
		query.setParameter("approvalneed", 1);
		List<Transactions> transList = query.list(); 	
		List<Transactions> tL = null; 
		for (Transactions t : transList) {
			if((t.getTransactionTypes()).equals("TR_VIEW")||(t.getTransactionTypes().equals("TR_VIEWTR")||(t.getTransactionTypes()).contains("TR_EDIT")))
					{
				       tL.add(t);
					}
		}
		return tL;
	}
	
	//Check if trans type is TR_VIEW and 
	@Transactional
	public Transactions findViewableOrNot(int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		//session.beginTransaction();
		Criteria criteria=session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("fromAccount", accno));
		criteria.add(Restrictions.eq("transactionTypes", "TR_VIEW"));
		criteria.add(Restrictions.eq("status", 2));
		criteria.add(Restrictions.eq("approvalNeeded", 1));
		Transactions trans=(Transactions)criteria.uniqueResult();
		if(trans!=null)
		{
			trans.setStatus(3);
			trans=updateTrans(trans);
		}
		return trans;
	}
	
	public Transactions updateTrans(Transactions trans)
	{
		 Session session = sessionFactory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.update(trans); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
		return trans;
	}
	
	@Transactional
	public Transactions findEditableOrNot(int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where fromAccount =:ano and status =:statusno");
		query.setParameter("ano", accno);
		query.setParameter("statusno", 2);
		List<Transactions> transList = query.list(); 
		Transactions trans = null;
		for (Transactions t : transList) {
			if(t.getTransactionTypes().contains("TR_EDIT"))
			   trans = t;
		}
		if(trans!=null)
		{
			trans.setStatus(3);
			trans=updateTrans(trans);
		}
		return trans;
	}
	
	@Transactional
	public Transactions findTransViewableOrNot(int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		//session.beginTransaction();
		Criteria criteria=session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("fromAccount", accno));
		criteria.add(Restrictions.eq("transactionTypes", "TR_VIEWTR"));
		criteria.add(Restrictions.eq("status", 2));
		criteria.add(Restrictions.eq("approvalNeeded", 1));
		Transactions trans=(Transactions)criteria.uniqueResult();
		if(trans!=null)
		{
			trans.setStatus(3);
			trans=updateTrans(trans);
		}
		return trans;
	}

	public List<Transactions> getTransToBeApproved(String role)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where transactionTypes=:type and approvalNeeded=:approvalneed");
		query.setParameter("approvalneed", 1);
		if(role.equals("ROLE_EMPLOYEE"))
		  query.setParameter("type", "TR_SEMI");
		else if(role.equals("ROLE_ADMIN"))
		  query.setParameter("type", "TR_CRITICAL");
		else return null;
		List transList = query.list(); 		
		return transList;
	}
	
	public List<Transactions> getExtUserReqList(String role)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where status=:stat");
		query.setParameter("stat", 2);
		//if(role.equals("ROLE_EMPLOYEE"))
		  //query.setParameter("type", "TR_SEMI");
		//else if(role.equals("ROLE_ADMIN"))
		  //query.setParameter("type", "TR_CRITICAL");
		//else return null;
		List transList = query.list(); 
		return transList;
	}

	public void createViewExtProfileTrans(Transactions transactions)
			throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(transactions);
	}

	public int countOfReqForAcc(String type, int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where transactionTypes=:type and status<:stat and fromAccount=:accno");
		query.setParameter("stat", 3);
		query.setParameter("type", type);
		query.setParameter("accno", accno);
		List<Transactions> transList = query.list(); 	
		int count = transList.size();
		return count;
	}
	
	public int countOfReq(String type, int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where status=:stat and fromAccount=:accno");
		query.setParameter("stat", 2);
		query.setParameter("accno", accno);
		List<Transactions> transList = query.list(); 
		List<Transactions> tL = null;
		int count =0;
		if(transList!=null)
		{
		for (Transactions t : transList) {
			if((t.getTransactionTypes()).contains(type))
			{
				tL.add(t);
			}
		}
		if(tL!=null)
		  count = tL.size();
		}
		return count;
	}
	
	public int countOfReqTrans(String type, int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where transactionTypes=:types and status=:stat and fromAccount=:accno");
		query.setParameter("stat", 2);
		query.setParameter("accno", accno);
		query.setParameter("types", type);
		List<Transactions> transList = query.list(); 
		int count = transList.size();
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+ count);
		return count;
	}
	
	public List<Transactions> getViewProfileReqApproved(int accno)
	{
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Transactions where transactionTypes=:types and status=:stat and fromAccount=:accno");
		query.setParameter("stat", 1);
		query.setParameter("accno", accno);
		query.setParameter("types", "TR_VIEW");
		List<Transactions> transList = query.list(); 
		return transList;

	}
}
