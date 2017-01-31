package com.group10.sparkysbank.validator;

public class FieldValidations {

	public static void validateSpecialChars(String fieldValue, int minLength, int maxLength, String display) throws Exception
	{
		if(fieldValue == null||fieldValue.isEmpty())
			throw new Exception(display + ": must be entered");
		
		if(fieldValue.length() > maxLength)
			throw new Exception(display + ":length must not exceed "+maxLength);
		
		if(fieldValue.length() < minLength)
			throw new Exception(display + ":length must exceed "+minLength);
		
		if(!fieldValue.matches("^[A-Za-z0-9-_@#!].*$"))
			throw new Exception(display+":invalid characters");
	}
}
