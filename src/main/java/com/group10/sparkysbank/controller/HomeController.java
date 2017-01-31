package com.group10.sparkysbank.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.group10.sparkysbank.model.Userinfo;
import com.group10.sparkysbank.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;

	@RequestMapping(value="/intHome")
	public String homePage(ModelMap model)
	{
		return "internalHome";
	}
	
	@RequestMapping(value="/register")
	public String registerAUser(ModelMap model)
	{
		model.addAttribute("extUser", new Userinfo());
		return "addExternalUserAccount";
	}
	
	@RequestMapping(value="/home")
	public String home(ModelMap model)
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		if(ur.equals("ROLE_ADMIN")||ur.equals("ROLE_EMPLOYEE"))
		   return "internalHome";
		else 
			return "userLogin";
	}
}
