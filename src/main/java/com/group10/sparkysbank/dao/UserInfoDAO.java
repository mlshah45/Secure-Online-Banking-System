package com.group10.sparkysbank.dao;

import java.util.ArrayList;
import java.util.Set;

import com.group10.sparkysbank.model.Pwdrecovery;
import com.group10.sparkysbank.model.UserRoles;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;


public interface UserInfoDAO {

	public Userinfo findUserByUsername(String username);
	public int registerNewUserAccount(Userinfo userInfo,Useraccounts account,UserRoles roles);
	public Set<String> getRolesByUserId(String username);
	public void updateUserInfo(Userinfo userInfo);
	public void deleteUserInfo(Userinfo userInfo);
	public ArrayList<String> getAllCustomers();
	public boolean isFirstLogin(String name);
	public void enableUser(String userName);

}
