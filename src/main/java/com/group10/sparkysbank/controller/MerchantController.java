package com.group10.sparkysbank.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.group10.sparkysbank.model.PKITransaction;
import com.group10.sparkysbank.service.MerchantService;
import com.group10.sparkysbank.service.PKIService;
import com.group10.sparkysbank.service.UserService;

@Controller
public class MerchantController {

	private static final Logger logger=Logger.getLogger(MerchantController.class);
	@Autowired
	MerchantService merchantService;

	@Autowired
	UserService userService;

	@Autowired
	PKIService pkiService;
	@RequestMapping("/submitPayment")
	public String submitPayment(Model model)
	{
		if(logger.isDebugEnabled()){
			logger.debug("Merchant submitted payment");
		}

		logger.info("Merchant submitted payment");
		ArrayList<String> customers= merchantService.getAllCustomers();
		model.addAttribute("users", customers);
		return "SubmitPayment";
	}

	@RequestMapping("/extHome")
	public String merchantHome(Model model)
	{
		return "hello";
	}

	@RequestMapping("/approvedTransactions")
	public String checkApprovedTransactions(Model model,Map<String,Object> map){
		ArrayList<PKITransaction> transactions= merchantService.getApprovedTransactions(SecurityContextHolder.getContext().getAuthentication().getName());
		map.put("transactions", transactions);
		return "approvedTx";
	}
	@RequestMapping(value="/paymentSubmitted",method=RequestMethod.POST)
	public String submittingToCustomer(Model model,HttpServletRequest request){

		String customer=request.getParameter("user");
		String amount=request.getParameter("amount");
		String token=request.getParameter("token");
		System.out.println(customer);
		System.out.println(amount);
		System.out.println(token);
		/*
		 * validation left
		 * */
		PKITransaction transaction=new PKITransaction();
		transaction.setAmount(Double.parseDouble(amount));
		transaction.setCustomer(customer);
		transaction.setMerchant(SecurityContextHolder.getContext().getAuthentication().getName());
		transaction.setMerchantToken(token);
		transaction.setTransactionstatus("PENDING_CUSTOMER_ACTION");
		merchantService.submitPayment(transaction);
		return "SubmitPayment";
	}

	@RequestMapping(value="/verifyCustomer")
	public String requestAuthenticationCustomer(Model model, HttpServletRequest request){

		String id=request.getParameter("id");
		String customer=request.getParameter("customer");

		PKITransaction transaction= pkiService.getTransactionFromTransactionId(Integer.parseInt(id));

		if(pkiService.authenticateCustomer(Integer.parseInt(id), customer))
		{
			System.out.println("merchant authenticated");
			transaction.setTransactionstatus("COMPLETED");

			pkiService.updatePKITransaction(transaction);
		}

		return "approvedTx";
	}
}

