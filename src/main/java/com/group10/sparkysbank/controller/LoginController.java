
package com.group10.sparkysbank.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group10.sparkysbank.service.OneTimePadService;
import com.group10.sparkysbank.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private OneTimePadService otpService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(ModelMap model)
	{
		System.out.println("in login controller");
		return "userLogin";
	}
	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String home(Model model)
	{
		
		return "home";
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(ModelMap model)
	{

		return "userLogin";
	}
	
	@RequestMapping(value="/loginFailed", method=RequestMethod.GET)
	public String loginFailed(ModelMap model, Principal user)
	{

		model.addAttribute("error", "true");
		return "userLogin";
	}
	
	@RequestMapping(value="/403", method=RequestMethod.GET)
	public String ifNotAuthorized(ModelMap model)
	{
		return "403";
	}
	
	@RequestMapping(value="/forgotpassword",method=RequestMethod.GET)
	public String forgotPasswordClicked(Model model, HttpSession session)
	{
		System.out.println("in forgot password");
		return "forgotPassword";
	}
	
	@RequestMapping(value="/forgotpassword",method=RequestMethod.POST)
	public ModelAndView forgotPasswordNextClicked(@RequestParam("enteredusername") String enteredusername, HttpSession session)
	{
		System.out.println("in forgot password Post"+" "+enteredusername);
		ModelAndView model = null;
		
			if(userService.isUserPresent(enteredusername))
			{
				otpService.generateOTP(enteredusername);
			session.setAttribute("type", "forgotpassword");
			session.setAttribute("username", enteredusername);
		    model = new ModelAndView("otpVerify");
			}
			else
			{
				model = new ModelAndView("forgotPassword");
				model.addObject("errorMessage", "Username is not valid!");
			}
	
		return model;
	}

}