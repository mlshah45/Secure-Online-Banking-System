package com.group10.sparkysbank.dao;

import java.util.ArrayList;

import com.group10.sparkysbank.model.UserPII;

public interface UserPIIDAO {

	public void saveUserPIIRequest(UserPII userPII);

	public ArrayList<UserPII> getAllPIIChangeRequests();

	public UserPII getUserPIIToken(String username);

	public void deleteUserPIIRequest(UserPII userPII);
}
