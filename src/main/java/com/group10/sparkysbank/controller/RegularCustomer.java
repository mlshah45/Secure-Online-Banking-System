package com.group10.sparkysbank.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.group10.sparkysbank.model.PKITransaction;
import com.group10.sparkysbank.service.PKIService;
import com.group10.sparkysbank.service.TransactionsService;

@Controller
public class RegularCustomer {
	
	@Autowired
	TransactionsService transactionService;
	
	@Autowired
	PKIService pkiService;
	
	@RequestMapping("/extCustomerHome")
	public String externalCustomer(Model model,HttpSession session, HttpServletRequest request)
	{
		return "hello";
	}
	@RequestMapping("/hello1")
	public String externalCustomerHome(Model model)
	{
		return "hello";
	}
	
	
	@RequestMapping("/paymentsFromMerchant")
	public String openPayments(Model model, Map<String, Object> map){
		ArrayList<PKITransaction> list=transactionService.getPKITransactionsForCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
		
		map.put("transactions", list);
		return "paymentsFromMerchant";
	}
	
	
	@RequestMapping(value="/verifyMerchant",method=RequestMethod.POST)
	public String authenticateMerchant(Model model,HttpServletRequest request) throws Exception, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		String id=request.getParameter("id");
		String merchant=request.getParameter("merchant");
		String customertoken=request.getParameter("customertoken");
		System.out.println("verifying merchant");
		if(customertoken.contains("javascript") ||customertoken.contains("delete")||customertoken.contains("create"))
		{
			System.out.println("no customer token");
			model.addAttribute("error", "error");
			return "paymentsFromMerchant";
		}
		PKITransaction transaction= pkiService.getTransactionFromTransactionId(Integer.parseInt(id));
		
		
		if(pkiService.authenticateMerchant(Integer.parseInt(id), merchant))
		{
			System.out.println("merchant authenticated");
			transaction.setCustomerToken(customertoken);
			transaction.setTransactionstatus("APPROVED_FROM_CUSTOMER");
		
			pkiService.updatePKITransaction(transaction);
		}
		else
		{
			model.addAttribute("invalid", "invalid");
		}
		return "paymentsFromMerchant";
	}


}
