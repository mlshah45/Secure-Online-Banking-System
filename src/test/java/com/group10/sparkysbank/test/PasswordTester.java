package com.group10.sparkysbank.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTester extends TestCase {

	@Test
	public void testPasswordGenerator()
	{
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		String password=encoder.encode("Secret1");
		System.out.println(password);
	}
	
}

