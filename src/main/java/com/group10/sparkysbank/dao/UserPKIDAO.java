package com.group10.sparkysbank.dao;

import com.group10.sparkysbank.model.UserPKI;

public interface UserPKIDAO {

	public void storePublicKeyAndToken(String username,byte[] publicKey, String token);
	public UserPKI getPKIToken(String username);
}

