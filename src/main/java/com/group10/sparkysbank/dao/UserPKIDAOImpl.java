package com.group10.sparkysbank.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.UserPKI;
@Repository("userPKIDAO")
public class UserPKIDAOImpl implements UserPKIDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	public void storePublicKeyAndToken(String username, byte[] publicKey,
			String token) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		UserPKI pki=new UserPKI(username,publicKey,token);
		session.save(pki);
		
	}

	public UserPKI getPKIToken(String username) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserPKI.class);
		criteria.add(Restrictions.eq("username",username));
		UserPKI pkiToken=(UserPKI) criteria.uniqueResult();
		return pkiToken;
	}

	
}
