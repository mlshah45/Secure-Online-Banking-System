package com.group10.sparkysbank.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.UserPII;

@Repository("userPIIDAO")
public class UserPIIDAOImpl implements UserPIIDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	public void saveUserPIIRequest(UserPII userPII) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		session.save(userPII);
	}

	public ArrayList<UserPII> getAllPIIChangeRequests() {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserPII.class);
		ArrayList<UserPII> list= (ArrayList<UserPII>) criteria.list();
		return list;
		
	}

	public UserPII getUserPIIToken(String username) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserPII.class);
		criteria.add(Restrictions.eq("username",username));
		UserPII userPII=(UserPII) criteria.uniqueResult();
		return userPII;
	}

	public void deleteUserPIIRequest(UserPII userPII) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		session.delete(userPII);
	}

}
