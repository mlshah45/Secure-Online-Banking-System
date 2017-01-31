package com.group10.sparkysbank.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.group10.sparkysbank.model.Userinfo;



public class UserPIIValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return Userinfo.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
		Userinfo user=(Userinfo)arg0;
		String username=user.getUsername();
		String identificationId=user.getIdentificationid();

		if(!username.matches("^[a-z0-9_-]{3,16}$"))
		{
			arg1.rejectValue("username", "Userinfo.username");
		}
			
		if(!identificationId.matches("^[0-9]{9}$"))
		{
			arg1.rejectValue("identificationid", "Userinfo.identificationId");
		}
		System.out.println("in validator");
	}

	
}
