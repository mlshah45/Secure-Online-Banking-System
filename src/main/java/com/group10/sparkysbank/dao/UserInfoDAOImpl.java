package com.group10.sparkysbank.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

import com.group10.sparkysbank.model.Pwdrecovery;
import com.group10.sparkysbank.model.UserRoles;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Repository(value="userInfoDAO")
public class UserInfoDAOImpl implements UserInfoDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	public Userinfo findUserByUsername(String username) {
		
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(Userinfo.class);
		criteria.add(Restrictions.eq("username", username));
		Userinfo user=(Userinfo)criteria.uniqueResult();
		return user;
	}

	public int registerNewUserAccount(Userinfo userInfo, Useraccounts account,UserRoles roles) {
		Session session=sessionFactory.getCurrentSession();
	//	session.beginTransaction();
		session.save(userInfo);
		session.save(account);
		session.save(roles);
		Criteria criteria=session.createCriteria(Useraccounts.class);
		criteria.add(Restrictions.eq("username",account.getUsername()));
		Useraccounts acc=(Useraccounts)criteria.uniqueResult();
		return acc.getAccountno();
	}

	public Set<String> getRolesByUserId(String username) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from UserRoles where username=:uname");
		query.setParameter("uname", username);
		ArrayList<UserRoles> list= (ArrayList<UserRoles>) query.list();
		Set<String> role=new LinkedHashSet<String>();
		for(UserRoles roles:list)
		{
			role.add(roles.getRole());
		}
		return role;
	}
	
	public void updateUserInfo(Userinfo userInfo)
	{
		 Session session = sessionFactory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.update(userInfo); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}

	public void deleteUserInfo(Userinfo userInfo)
	{
		 Session session = sessionFactory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
			 session.delete(userInfo); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}
	
	public ArrayList<String> getAllCustomers() {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from UserRoles where role=:rolecustomer");
		query.setParameter("rolecustomer", "ROLE_CUSTOMER");
		ArrayList<UserRoles> customers= (ArrayList<UserRoles>) query.list();
		ArrayList<String> users=new ArrayList<String>();
		for(UserRoles customer:customers)
			users.add(customer.getUsername());
		
		return users;
	}

	public boolean isFirstLogin(String name) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(Userinfo.class);
		criteria.add(Restrictions.eq("username", name));
		Userinfo user=(Userinfo)criteria.uniqueResult();
		if(user==null)
			return false;
		return !user.isEnable();
	}

	public void enableUser(String userName) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(Userinfo.class);
		criteria.add(Restrictions.eq("username", userName));
		Userinfo user=(Userinfo)criteria.uniqueResult();
		user.setEnable(true);
		session.update(user);
	}
}
