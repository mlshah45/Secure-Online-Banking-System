package com.group10.sparkysbank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group10.sparkysbank.model.OneTimePad;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.service.FundManagementService;
import com.group10.sparkysbank.service.OneTimePadService;
import com.group10.sparkysbank.service.UserService;

@Controller
public class OtpController {

	@Autowired
	private OneTimePadService otpService;

	@Autowired
	private FundManagementService fundManagementService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/otpVerify", method = RequestMethod.GET)
	public ModelAndView otpPage() {
		OneTimePad user = new OneTimePad();

		ModelAndView model = new ModelAndView("otpVerify");
		model.addObject("otpVerify", user);
		System.out.println("in oneTimePad controller");

		return model;

	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(HttpServletRequest request,HttpSession session) {
		ModelAndView model = null;
		try
		{
			String password = (String)request.getParameter("newPassword");
			String confirmPassword = (String)request.getParameter("confirmPassword");
			System.out.println("pass="+password);
			System.out.println("pass="+confirmPassword);
			if(!password.equals(confirmPassword))
				throw new Exception("Passwords do not match!");
			String userName="";	

			model= new ModelAndView("userLogin");
			if(session.getAttribute("type")!=null && session.getAttribute("type").equals("forgotpassword"))
			{	 userName=(String)session.getAttribute("username");
			userService.changePassword(confirmPassword, userName);
			model=new ModelAndView("userLogin");
			System.out.println("in oneTimePad controller");
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);


			}
			else
			{ 

				userName=SecurityContextHolder.getContext().getAuthentication().getName();
				userService.changePassword(confirmPassword, userName);
				userService.enableUser(userName);
				model=new ModelAndView("changePII");
			}
			return model;



		}
		catch(Exception e)
		{
			model = new ModelAndView("changePassword");
			model.addObject("errorMessage", e.getMessage());
			return model;

		}

	}

	//Gets the key entered by the user and verifies it
	@RequestMapping(value = "/otpVerify", method = RequestMethod.POST)
	public ModelAndView otpVerify(@ModelAttribute("otpVerify") OneTimePad otpVerify,BindingResult result,@RequestParam("enteredkey") String enteredkey, ModelMap map,HttpSession session) {

		ModelAndView model;
		model = new ModelAndView("otpVerify");
		try 
		{
			//verification
			if (otpService.otpVerify(enteredkey,session.getAttribute("username").toString()))
			{
				model = new ModelAndView("hello");
				//otp call from debit functionality
				if (session.getAttribute("type").toString().compareTo("transaction") == 0)
				{
					try
					{
						Transactions transactions = (Transactions) session.getAttribute("transactions");
						fundManagementService.createDebitTransaction(transactions);
					} 
					catch (Exception e)
					{
						model = new ModelAndView("debit");
						e.printStackTrace();
						model.addObject("errorMessage", e.getMessage());
						return model;
					}
				} 
				//otp call from forgot password functionality
				else if (session.getAttribute("type").toString().compareTo("forgotpassword") == 0) 
				{
					model = new ModelAndView("changePassword");
				}

			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			model.addObject("errorMessage", e.getMessage());
			return model;
		}

		return model;

	}

}
