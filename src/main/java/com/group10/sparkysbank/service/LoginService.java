package com.group10.sparkysbank.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.dao.UserInfoDAO;
import com.group10.sparkysbank.dao.UserRolesDAO;
import com.group10.sparkysbank.model.Roles;
import com.group10.sparkysbank.model.Userinfo;

@Service("loginService")
public class LoginService implements UserDetailsService{

	@Autowired
	UserInfoDAO userDao;
	@Autowired
	UserRolesDAO userRolesDAO;

	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Userinfo user=userDao.findUserByUsername(username);

		if(user!=null)
		{
			String password=user.getPassword();
			Collection<GrantedAuthority> roles=new ArrayList<GrantedAuthority>();
			Set<String> rolesColl=userDao.getRolesByUserId(username);
			for(String role:rolesColl)
			{
				roles.add(new GrantedAuthorityImpl(role));

			}
			User userInfo=new User(username, password, roles);
			
			return userInfo;
		}
		else
		{
			throw new UsernameNotFoundException("Invalid user!!");
		}
		
	}

}
