package com.group10.sparkysbank.dao;

import java.util.Set;

import com.group10.sparkysbank.model.Roles;
import com.group10.sparkysbank.model.Userinfo;

public interface UserRolesDAO {
	public Set<String> getRolesByUserId(int userId);
	public String findUserRoleType(String username);
}
