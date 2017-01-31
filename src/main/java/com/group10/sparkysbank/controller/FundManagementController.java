
package com.group10.sparkysbank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.group10.sparkysbank.model.OneTimePad;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.service.FundManagementService;
import com.group10.sparkysbank.service.OneTimePadService;

@Controller
public class FundManagementController {
	
	@Autowired
	private FundManagementService fundManagementService ;
	
	@Autowired
	private OneTimePadService otpService;
	
	@RequestMapping(value="/transfer",method=RequestMethod.GET)
	public ModelAndView transfer()
	{
		Transactions transactions = new Transactions();
		ModelAndView model = new ModelAndView("Transfer");
		model.addObject("transactions", transactions);
		System.out.println("in transfer controller");	
		return model;
	}
	
	@RequestMapping(value="/transfer", method=RequestMethod.POST)
	public ModelAndView createTransfer(@ModelAttribute("transactions") Transactions transactions, BindingResult result, ModelMap map)
	{
		ModelAndView model;
		try {
			fundManagementService.createTransferTransaction(transactions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model = new ModelAndView("Transfer");
			e.printStackTrace();
			model.addObject("errorMessage", e.getMessage());
			return model;
		}
		
		model = new ModelAndView("hello");
		return model;
		
	}
	
	@RequestMapping(value="/credit",method=RequestMethod.GET)
	public ModelAndView credit()
	{
		Transactions transactions = new Transactions();
		ModelAndView model = new ModelAndView("credit");
		model.addObject("credit", transactions);
		System.out.println("in credit controller");
		
		return model;
	}
	
	@RequestMapping(value="/credit", method=RequestMethod.POST)
	public ModelAndView createCredit(@ModelAttribute("credit") Transactions transactions, BindingResult result, ModelMap map)
	{
		ModelAndView model;
		try {
			fundManagementService.createCreditTransaction(transactions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model = new ModelAndView("credit");
			e.printStackTrace();
			model.addObject("errorMessage", e.getMessage());
			return model;
		}
		model = new ModelAndView("hello");
		return model;
		
	}
	
	@RequestMapping(value="/debit",method=RequestMethod.GET)
	public ModelAndView debit()
	{
		Transactions transactions = new Transactions();
		
		
		ModelAndView model = new ModelAndView("debit");
		model.addObject("debit", transactions);
		System.out.println("in debit controller");
		
		return model;
	}
	
	@RequestMapping(value="/debit", method=RequestMethod.POST)
	public ModelAndView createDebit(@ModelAttribute("debit") Transactions transactions, BindingResult result, ModelMap map,HttpSession session)
	{
		ModelAndView model = null;
		//generates otp if amount >1000
		try {
			if(transactions.getAmtInvolved() == null || transactions.getAmtInvolved()==0||transactions.getAmtInvolved()>10000000)
				throw new Exception("Amount is invalid");
		if(transactions.getAmtInvolved()>1000)
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			otpService.generateOTP(auth.getName());

			session.setAttribute("transactions",transactions);
			session.setAttribute("type", "transaction");
			session.setAttribute("username",auth.getName());
		    model = new ModelAndView("otpVerify");
		    return model;
		}
		
		model = new ModelAndView("hello");
		
			fundManagementService.createDebitTransaction(transactions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model = new ModelAndView("debit");
			e.printStackTrace();
			model.addObject("errorMessage", e.getMessage());
			return model;
		}
		
		return model;
		
	}

	
}
