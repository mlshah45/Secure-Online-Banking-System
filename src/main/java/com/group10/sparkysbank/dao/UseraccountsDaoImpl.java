package com.group10.sparkysbank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Repository("useraccountsDao")
public class UseraccountsDaoImpl implements UseraccountsDao {

	@Autowired 
	private SessionFactory sessionFactory;
	
	public Useraccounts getAccountByUsername(String userName) throws Exception {
		// TODO Auto-generated method stub
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Useraccounts U WHERE U.username='"+userName+"'");
		List<Useraccounts> useraccountsList = query.list();
		
		if(useraccountsList.get(0) == null)
			throw new Exception("No accounts for this user!");
		
		return useraccountsList.get(0);
		
		
	}

	public Useraccounts getUserAccount(Integer accountNo) throws Exception {
		// TODO Auto-generated method stub
		Useraccounts useraccounts = (Useraccounts)sessionFactory.getCurrentSession().get(Useraccounts.class, accountNo);
		
		if(useraccounts == null)
			throw new Exception("Invalid account!");
		
		return useraccounts;
	}

	public void updateBalance(Useraccounts useraccounts) throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().update(useraccounts);
	}

}
/*<<<<<<< HEAD
package com.group10.sparkysbank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Repository("useraccountsDao")
public class UseraccountsDaoImpl implements UseraccountsDao {

	@Autowired 
	private SessionFactory sessionFactory;
	
	public Useraccounts getAccountByUsername(String userName) throws Exception {
		// TODO Auto-generated method stub
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Useraccounts U WHERE U.username='"+userName+"'");
		List<Useraccounts> useraccountsList = query.list();
		
		if(useraccountsList.get(0) == null)
			throw new Exception("No accounts for this user!");
		System.out.println(useraccountsList.get(0).getAccountno());
		return useraccountsList.get(0);
		
		
	}

	public Useraccounts getUserAccount(Integer accountNo) throws Exception {
		// TODO Auto-generated method stub
		Useraccounts useraccounts = (Useraccounts)sessionFactory.getCurrentSession().get(Useraccounts.class, accountNo);
		
		if(useraccounts == null)
			throw new Exception("Invalid account!");
		
		return useraccounts;
	}

	public void updateBalance(Useraccounts useraccounts) throws Exception {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().update(useraccounts);
	}

}
=======*/
