package com.group10.sparkysbank.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.group10.sparkysbank.model.UserRoles;
import com.group10.sparkysbank.model.Userinfo;

@Repository("userRolesDAO")
public class UserRolesDAOImpl implements UserRolesDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	public Set<String> getRolesByUserId(int userId) {
	
	/*	Session session=sessionFactory.getCurrentSession();
		//session.beginTransaction();
		
		Query query=session.createQuery("from UserRolesId where iduserinfo=:id");
		query.setParameter("id", userId);
		ArrayList<UserRolesId> userRoles= (ArrayList<UserRolesId>) query.list();
		Set<String> roles=new LinkedHashSet<String>();
		for(UserRolesId userRole:userRoles)
		{
			roles.add(userRole.getRoleType());
		}
		
		return roles;
	*/
		return null;}
	//get user role type for a given username
	public String findUserRoleType(String username) {
		
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserRoles.class);
		criteria.add(Restrictions.eq("username", username));
		UserRoles user=(UserRoles)criteria.uniqueResult();
		return user.getRole();
	}

}
