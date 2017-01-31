package com.group10.sparkysbank.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.dao.TransactionsDao;
import com.group10.sparkysbank.dao.UserInfoDAO;
import com.group10.sparkysbank.dao.UserPIIDAO;
import com.group10.sparkysbank.dao.UserRolesDAO;
import com.group10.sparkysbank.model.Pwdrecovery;
import com.group10.sparkysbank.model.Roles;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.model.UserPII;
import com.group10.sparkysbank.model.UserRoles;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;
import com.group10.sparkysbank.validator.FieldValidations;

@Service("userService")
public class UserService {

	@Autowired
	UserInfoDAO userInfoDAO;

	@Autowired
	UserRolesDAO userRolesDAO;

	@Autowired
	TransactionsDao transactionsDAO;

	@Autowired
	UserPIIDAO userPIIDAO;

	@Autowired
	AccountManagerService accountManagerService ;


	public int addNewExternalUuser(Userinfo userInfo,String role)
	{

		Useraccounts account=new Useraccounts();
		account.setBalance(500.0);
		account.setUsername(userInfo.getUsername());


		UserRoles roles=new UserRoles();
		roles.setRole(role);
		roles.setUsername(userInfo.getUsername());

		return userInfoDAO.registerNewUserAccount(userInfo,account,roles);

	}	
	@Transactional

	public boolean isUserPresent(String username)

	{

		if(userInfoDAO.findUserByUsername(username)!=null) return true;

		return false;

	}

	@Transactional

	public void changePassword(String newPassword, String userName) throws Exception

	{

		FieldValidations.validateSpecialChars(newPassword, 4,8, "Password");

		Userinfo userInfo = getUserInfobyUserName(userName);

		if(userInfo == null)

			throw new Exception("Invalid user name");

		userInfo.setPassword(new BCryptPasswordEncoder().encode(newPassword));

		updateUserInfo(userInfo);

	}

	//return user with given username and identification no
	@Transactional
	public Userinfo getUserInfo(Userinfo userInfo)
	{   String i = (userInfoDAO.findUserByUsername(userInfo.getUsername())).getIdentificationid();
	if(i.equalsIgnoreCase(userInfo.getIdentificationid()))
	{
		Userinfo ui = userInfoDAO.findUserByUsername(userInfo.getUsername()); 
		return ui;
	}
	return null;
	}

	//return user with given username and identification no
	@Transactional
	public Userinfo getUserInfobyUserName(String username)
	{   
		Userinfo ui = userInfoDAO.findUserByUsername(username);
		if(ui != null)
		{ 
			return ui;
		}
		return null;
	}

	//update address or emailID of user with given values
	@Transactional
	public void updateUserInfo(Userinfo userInfo)
	{   
		userInfoDAO.updateUserInfo(userInfo);
	}

	//update address or emailID of user with given values
	@Transactional
	public void deleteUserInfo(Userinfo userInfo)
	{   
		userInfoDAO.deleteUserInfo(userInfo);
	}

	//get user role type
	@Transactional
	public String getUserRoleType(String username)
	{   
		return userRolesDAO.findUserRoleType(username);
	}

	//get if view transaction is authorized
	@Transactional
	public Boolean getViewAuthorization(String username)
	{   
		int accno;
		try
		{
			accno = (accountManagerService.getUserAccountForUserName(username)).getAccountno();
		}
		catch(Exception e)
		{
			return null;
		}
		Transactions t = transactionsDAO.findViewableOrNot(accno);
		if(t==null)
			return false;
		//else if(t.getStatus()==2&&(t.getTransactionTypes().equals("TR_VIEW")&&t.getApprovalNeeded()==1))
		//return true;
		else 
			return true;
	}

	//get if edit transaction is authorized
	@Transactional
	public String getEditAuthorization(String username)
	{   
		int accno;
		try
		{
			accno = (accountManagerService.getUserAccountForUserName(username)).getAccountno();
		}
		catch(Exception e)
		{
			return null;
		}
		Transactions t = transactionsDAO.findEditableOrNot(accno);
		if(t!=null)
			return (t.getTransactionTypes()).substring(7, t.getTransactionTypes().length()-1);
		//else if(t.getStatus()==2&&(t.getTransactionTypes().equals("TR_VIEW")&&t.getApprovalNeeded()==1))
		//return true;
		else 
			return null;
	}

	//get if view transactions is authorized
	@Transactional
	public Boolean getViewTransactionAuthorization(String username)
	{
		int accno;
		try
		{
			accno = (accountManagerService.getUserAccountForUserName(username)).getAccountno();
		}
		catch(Exception e)
		{
			return null;
		}
		Transactions t = transactionsDAO.findTransViewableOrNot(accno);
		if(t!=null)
			return true;
		else 
			return false;
	}
	@Transactional
	public boolean isFirstLogin(String name) {
		// TODO Auto-generated method stub
		return userInfoDAO.isFirstLogin(name);

	}
	@Transactional
	public void enableUser(String userName) {
		// TODO Auto-generated method stub
		userInfoDAO.enableUser(userName);
	}
	@Transactional
	public void submitChangePIIRequest(UserPII userPII) {
		// TODO Auto-generated method stub
		userPIIDAO.saveUserPIIRequest(userPII);
	}
	@Transactional
	public ArrayList<UserPII> getAllPIIChangeRequests() {
		// TODO Auto-generated method stub
		 return userPIIDAO.getAllPIIChangeRequests();
	}
	@Transactional
	public UserPII getUserPIIToken(String username) {
		// TODO Auto-generated method stub
		return userPIIDAO.getUserPIIToken(username);
	}
	@Transactional
	public void deleteUserPIIRequest(UserPII userPII) {
		// TODO Auto-generated method stub
		userPIIDAO.deleteUserPIIRequest(userPII);
	}

}
