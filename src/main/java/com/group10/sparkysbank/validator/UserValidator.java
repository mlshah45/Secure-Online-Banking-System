package com.group10.sparkysbank.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.group10.sparkysbank.model.Userinfo;



public class UserValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return Userinfo.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
		Userinfo user=(Userinfo)arg0;
		String username=user.getUsername();
		String firstname=user.getFirstname();
		String middlename=user.getMiddlename();
		String lastname=user.getLastname();
		String password=user.getPassword();
		String identificationId=user.getIdentificationid();
		
		if(!firstname.matches("^[a-zA-Z]{1,12}$"))
		{
			arg1.rejectValue("firstname", "Userinfo.firstname");
		}
		
		if(!middlename.matches("^[a-zA-Z]{1,12}$"))
		{
			arg1.rejectValue("middlename", "Userinfo.middlename");
		}
		
		if(!lastname.matches("^[a-zA-Z]{1,12}$"))
		{
			arg1.rejectValue("lastname", "Userinfo.lastname");
		}
					
		if(!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$"))
		{
			arg1.rejectValue("password", "Userinfo.password");
			 
		}
			
		if(!username.matches("^[a-z0-9_-]{3,16}$"))
		{
			arg1.rejectValue("username", "Userinfo.username");
		}
			
		
		System.out.println("in validator");
	}

	
}
