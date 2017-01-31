package com.group10.sparkysbank.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.OneTimePad;
import com.group10.sparkysbank.model.Pwdrecovery;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.model.UserRoles;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Repository("OneTimePadDAO")
public class OneTimePadDAOImpl implements OneTimePadDAO {

	@Autowired 
	private SessionFactory sessionFactory;
	
	public OneTimePad findUserByUsername(String username) {

		Query query = sessionFactory.getCurrentSession().createQuery("FROM OneTimePad U WHERE U.username='"+username+"'");
		List<OneTimePad> userList = query.list();
		
		if(userList.isEmpty())
			return null;
		
		return userList.get(0);
	}

	public void createOTP(OneTimePad user)
			throws Exception {

		sessionFactory.getCurrentSession().save(user);		
	}
	
	
	public void updateLoginAttempts(OneTimePad user) {
	
		sessionFactory.getCurrentSession().update(user);
	}
	
	
	public void deleteUserByUsername(OneTimePad User)
	{
		sessionFactory.getCurrentSession().delete(User);
	}
	
}
