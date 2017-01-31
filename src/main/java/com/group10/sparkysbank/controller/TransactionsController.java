package com.group10.sparkysbank.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.group10.sparkysbank.service.AccountManagerService;
import com.group10.sparkysbank.service.TransactionsService;
import com.group10.sparkysbank.service.UserService;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Controller
public class TransactionsController {

	@Autowired
	TransactionsService transactionsService;

	@Autowired
	UserService userService;

	@Autowired
	AccountManagerService accountManagerService;

	// Sravya's Code
	@RequestMapping(value = "/viewTransactions")
	public String viewTransactionAmt(ModelMap model) {
		model.addAttribute("transAmt", new Transactions());
		return "transactionsPage";
	}

	@RequestMapping(value = "/viewTransactions", method = RequestMethod.POST)
	public String viewTransactionAmt(
			@ModelAttribute("transAmt") Transactions transactions,
			BindingResult result, SessionStatus status, Model model) {

		if (result.hasErrors()) {
			System.out.println("error");
			return "transactionsPage";
		}
		model.addAttribute("trans", (this.transactionsService
				.viewTransaction(transactions.getIdtransactions()))
				.getAmtInvolved());
		// System.out.println(userInfo.getFirstname());
		return "transactionsPage";
	}

	/*
	 * @RequestMapping(value="/viewTransaction", method=RequestMethod.POST)
	 * public ModelAndView viewTransaction(@ModelAttribute("transactions")
	 * Transactions transactions, BindingResult result, ModelMap map){
	 * ModelAndView model = new ModelAndView("transactionsPage");
	 * model.addObject("trans",
	 * (this.transactionsService.viewTransaction(transactions
	 * .getIdtransactions())).getAmtInvolved()); return model; }
	 */

	/*
	 * @RequestMapping(value="/viewTransaction") public String
	 * viewTransaction(Model model){ int id=1; model.addAttribute("trans",
	 * (this.transactionsService.viewTransaction(id)).getAmtInvolved()); //
	 * model.addAttribute("amt_involved",
	 * this.transactionsService.listPersons()); return "transactionsPage"; }
	 */

	// ACTIVITY-List of transactions
	@RequestMapping(value = "/UserAccountManagementActivity", method = RequestMethod.GET)
	public String activityUserInfo(Model model) {
		model.addAttribute("accessInfo", new Userinfo());
		return "usrAccMgmtActivity";
	}

	@RequestMapping(value = "/UserAccountManagementActivity", method = RequestMethod.POST)
	public String activityUserInfo(
			@ModelAttribute("accessInfo") Userinfo userInfo,
			BindingResult result, SessionStatus status, Model model) {
		// add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror", null);
		// validate input format
		if (userInfo.getUsername() != null) {
			if (!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$")) {
				model.addAttribute("usernameerror",
						"Please enter a valid username");
				return "usrAccMgmtActivity";
			} else {
				// validate if reasonable request and username exists
				if (userService.getUserInfobyUserName(userInfo.getUsername()) == null) {
					model.addAttribute("usernameerror",
							"Specified username does not exist");
					return "usrAccMgmtActivity";
				} else {
					Userinfo ui = userService.getUserInfobyUserName(userInfo
							.getUsername());
					// check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if (ur.equals("ROLE_CUSTOMER")
							|| ur.equals("ROLE_MERCHANT")) {
						// check if this viewing has been authorized
						if (userService.getViewTransactionAuthorization(ui
								.getUsername())) {
							Useraccounts ua;
							try {
								ua = accountManagerService
										.getUserAccountForUserName(userInfo
												.getUsername());
							} catch (Exception e) {
								return "usrAccMgmtActivity";
							}
							List<Transactions> trans = transactionsService
									.getTransactionsbyAccountNo(ua
											.getAccountno());
							model.addAttribute("accessInfo", ui);
							model.addAttribute("transList", trans);
							return "usrAccMgmtActivity";
						} else {
							model.addAttribute("usernameerror",
									"Currently not authorized to view");
							return "usrAccMgmtActivity";
						}
					} else {
						model.addAttribute("usernameerror",
								"Not a valid external user");
						return "usrAccMgmtActivity";
					}
				}
			}
		} else {
			model.addAttribute("usernameerror", "Please enter the username");
			return "usrAccMgmtActivity";
		}
		// ////////////////////////

		/*
		 * //case where the verification credentials are posted and activity
		 * records are sent if(userInfo.getUsername()!=null) { //Validating info
		 * Userinfo ui = userService.getUserInfo(userInfo); Useraccounts ua;
		 * if(ui != null) { try { ua =
		 * accountManagerService.getUserAccountForUserName
		 * (userInfo.getUsername()); } catch(Exception e) { return
		 * "usrAccMgmtActivity"; } List<Transactions> trans =
		 * transactionsService.getTransactionsbyAccountNo(ua.getAccountno());
		 * model.addAttribute("accessInfo", ui); model.addAttribute("transList",
		 * trans); return "usrAccMgmtActivity"; }
		 * 
		 * else { model.addAttribute("accessInfo", new Userinfo()); return
		 * "usrAccMgmtActivity"; } } else { model.addAttribute("accessInfo", new
		 * Userinfo()); return "usrAccMgmtActivity"; }
		 */
	}

	// A List of financial transactions to be authorized
	@RequestMapping(value = "/AuthorizeTransactions", method = RequestMethod.GET)
	public String authorizeTransList(Model model) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	@RequestMapping(value = "/transApprove", method = RequestMethod.POST)
	public String approveTrans(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(1);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	@RequestMapping(value = "/transReject", method = RequestMethod.POST)
	public String rejectTrans(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(0);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	// Authorization for external user requests
	// A List of user generated requests pending for authorization
	@RequestMapping(value = "/ExtUserRequests", method = RequestMethod.GET)
	public String extUserReqList(Model model) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService.getExtUserReqList(ur);
		Useraccounts ua = null;
		if (transL != null) {
			for (Transactions t : transL) {
				System.out.println(t.getTransactionTypes());
				if (t.getTransactionTypes().contains("TR_EDIT"))
					t.setTransactionTypes("EDIT APPROVAL REQUEST");
				else if (t.getTransactionTypes().equals("TR_VIEW"))
					t.setTransactionTypes("VIEW USER PROFILE REQUEST APPROVED");
				else if (t.getTransactionTypes().equals("TR_VIEWTR"))
					t.setTransactionTypes("REQUEST TO REVIEW TRANSACTIONS");
				try {
					ua = accountManagerService.getUserNameForAccount(t.getFromAccount());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t.setCreditDebit(ua.getUsername());
			}
		}
		model.addAttribute("transList", transL);
		return "extUserRequests";
	}

	//External User Profile view request 
	
	@RequestMapping(value = "/ExtUserProfileViewReq")
	public String ExtUserProfileViewReq(ModelMap model) {
		model.addAttribute("accessInfo", new Userinfo());
		model.addAttribute("usernameerror",null);
		model.addAttribute("Message", null);
		return "reqProfileViewAccess";
	}
	
	@RequestMapping(value = "/ExtUserProfileViewReq", method = RequestMethod.POST)
	public String ExtUserProfileViewReq(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo,
			BindingResult result, SessionStatus status, Model model) {
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		model.addAttribute("Message", null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "reqProfileViewAccess";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "reqProfileViewAccess";
				}
				else
				{
					//check if the user is an external user
					String ur = userService.getUserRoleType(userInfo.getUsername());
					if(ur.equals("ROLE_CUSTOMER")||ur.equals("ROLE_MERCHANT"))
					{
						String message= transactionsService.extUsrProfileViewReq(userInfo.getUsername());
						model.addAttribute("Message",message);
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid employee");
						return "reqProfileViewAccess";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "reqProfileViewAccess";
		}
		return "reqProfileViewAccess";
	}
	
	@RequestMapping(value = "/transactionReviewRequest", method = RequestMethod.GET)
	public String reqTransReview(Model model) {
		//add objects to model
		model.addAttribute("accessInfo", new Userinfo());
		model.addAttribute("Message", null);
		//validate input format
		return "transReviewReq";
	} 
	
	@RequestMapping(value = "/transactionReviewRequest", method = RequestMethod.POST)
	public String reqTransReview(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo,
			BindingResult result, SessionStatus status, Model model) {
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("Message", null);
		//validate input format
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		if(ur.equals("ROLE_CUSTOMER")||ur.equals("ROLE_MERCHANT"))
		{
			String message= transactionsService.extUsrTransReviewReq(username);
			model.addAttribute("Message",message);
		}
		else
		{
			model.addAttribute("Message", "Not a valid employee");
			return "transReviewReq";
		}
		return "transReviewReq";
	} 
	
	// A List of view profile requests to be authorized
	@RequestMapping(value = "/AuthorizeProfileView", method = RequestMethod.GET)
	public String authorizeViewProfileList(Model model) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL =null;
		try
		{
		transL = transactionsService
				.getViewProfileReqApproved(ur);
		model.addAttribute("transList", transL);		
		}
		catch(Exception e)
		{
			model.addAttribute("transList", transL);
			return "approveProfileViews";
		}
		model.addAttribute("trans", new Transactions());
		return "approveProfileViews";
	}
	
	@RequestMapping(value = "/transApproveViewProfile", method = RequestMethod.POST)
	public String approveTransView(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(1);
		transaction.setStatus(3);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL =null;
		try
		{
		transL = transactionsService
				.getViewProfileReqApproved(ur);
		model.addAttribute("transList", transL);		
		}
		catch(Exception e)
		{
			model.addAttribute("transList", transL);
			return "approveProfileViews";
		}
		model.addAttribute("trans", new Transactions());
		return "approveProfileViews";
	}

	@RequestMapping(value = "/transRejectViewProfile", method = RequestMethod.POST)
	public String rejectTransView(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(0);
		transaction.setStatus(2);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL =null;
		try
		{
		transL = transactionsService
				.getViewProfileReqApproved(ur);
		model.addAttribute("transList", transL);		
		}
		catch(Exception e)
		{
			model.addAttribute("transList", transL);
			return "approveProfileViews";
		}
		model.addAttribute("trans", new Transactions());
		return "approveProfileViews";
	}
}
/*=======
package com.group10.sparkysbank.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.group10.sparkysbank.service.AccountManagerService;
import com.group10.sparkysbank.service.TransactionsService;
import com.group10.sparkysbank.service.UserService;
import com.group10.sparkysbank.model.Transactions;
import com.group10.sparkysbank.model.Useraccounts;
import com.group10.sparkysbank.model.Userinfo;

@Controller
public class TransactionsController {

	@Autowired
	TransactionsService transactionsService;

	@Autowired
	UserService userService;

	@Autowired
	AccountManagerService accountManagerService;

	// Sravya's Code
	@RequestMapping(value = "/viewTransactions")
	public String viewTransactionAmt(ModelMap model) {
		model.addAttribute("transAmt", new Transactions());
		return "transactionsPage";
	}

	@RequestMapping(value = "/viewTransactions", method = RequestMethod.POST)
	public String viewTransactionAmt(
			@ModelAttribute("transAmt") Transactions transactions,
			BindingResult result, SessionStatus status, Model model) {

		if (result.hasErrors()) {
			System.out.println("error");
			return "transactionsPage";
		}
		model.addAttribute("trans", (this.transactionsService
				.viewTransaction(transactions.getIdtransactions()))
				.getAmtInvolved());
		// System.out.println(userInfo.getFirstname());
		return "transactionsPage";
	}

	
	 * @RequestMapping(value="/viewTransaction", method=RequestMethod.POST)
	 * public ModelAndView viewTransaction(@ModelAttribute("transactions")
	 * Transactions transactions, BindingResult result, ModelMap map){
	 * ModelAndView model = new ModelAndView("transactionsPage");
	 * model.addObject("trans",
	 * (this.transactionsService.viewTransaction(transactions
	 * .getIdtransactions())).getAmtInvolved()); return model; }
	 

	
	 * @RequestMapping(value="/viewTransaction") public String
	 * viewTransaction(Model model){ int id=1; model.addAttribute("trans",
	 * (this.transactionsService.viewTransaction(id)).getAmtInvolved()); //
	 * model.addAttribute("amt_involved",
	 * this.transactionsService.listPersons()); return "transactionsPage"; }
	 

	// ACTIVITY-List of transactions
	@RequestMapping(value = "/UserAccountManagementActivity", method = RequestMethod.GET)
	public String activityUserInfo(Model model) {
		model.addAttribute("accessInfo", new Userinfo());
		return "usrAccMgmtActivity";
	}

	@RequestMapping(value = "/UserAccountManagementActivity", method = RequestMethod.POST)
	public String activityUserInfo(
			@ModelAttribute("accessInfo") Userinfo userInfo,
			BindingResult result, SessionStatus status, Model model) {
		// add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror", null);
		// validate input format
		if (userInfo.getUsername() != null) {
			if (!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$")) {
				model.addAttribute("usernameerror",
						"Please enter a valid username");
				return "usrAccMgmtActivity";
			} else {
				// validate if reasonable request and username exists
				if (userService.getUserInfobyUserName(userInfo.getUsername()) == null) {
					model.addAttribute("usernameerror",
							"Specified username does not exist");
					return "usrAccMgmtActivity";
				} else {
					Userinfo ui = userService.getUserInfobyUserName(userInfo
							.getUsername());
					// check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if (ur.equals("ROLE_CUSTOMER")
							|| ur.equals("ROLE_MERCHANT")) {
						// check if this viewing has been authorized
						if (userService.getViewTransactionAuthorization(ui
								.getUsername())) {
							Useraccounts ua;
							try {
								ua = accountManagerService
										.getUserAccountForUserName(userInfo
												.getUsername());
							} catch (Exception e) {
								return "usrAccMgmtActivity";
							}
							List<Transactions> trans = transactionsService
									.getTransactionsbyAccountNo(ua
											.getAccountno());
							model.addAttribute("accessInfo", ui);
							model.addAttribute("transList", trans);
							return "usrAccMgmtActivity";
						} else {
							model.addAttribute("usernameerror",
									"Currently not authorized to view");
							return "usrAccMgmtActivity";
						}
					} else {
						model.addAttribute("usernameerror",
								"Not a valid external user");
						return "usrAccMgmtActivity";
					}
				}
			}
		} else {
			model.addAttribute("usernameerror", "Please enter the username");
			return "usrAccMgmtActivity";
		}
		// ////////////////////////

		
		 * //case where the verification credentials are posted and activity
		 * records are sent if(userInfo.getUsername()!=null) { //Validating info
		 * Userinfo ui = userService.getUserInfo(userInfo); Useraccounts ua;
		 * if(ui != null) { try { ua =
		 * accountManagerService.getUserAccountForUserName
		 * (userInfo.getUsername()); } catch(Exception e) { return
		 * "usrAccMgmtActivity"; } List<Transactions> trans =
		 * transactionsService.getTransactionsbyAccountNo(ua.getAccountno());
		 * model.addAttribute("accessInfo", ui); model.addAttribute("transList",
		 * trans); return "usrAccMgmtActivity"; }
		 * 
		 * else { model.addAttribute("accessInfo", new Userinfo()); return
		 * "usrAccMgmtActivity"; } } else { model.addAttribute("accessInfo", new
		 * Userinfo()); return "usrAccMgmtActivity"; }
		 
	}

	// A List of financial transactions to be authorized
	@RequestMapping(value = "/AuthorizeTransactions", method = RequestMethod.GET)
	public String authorizeTransList(Model model) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	@RequestMapping(value = "/transApprove", method = RequestMethod.POST)
	public String approveTrans(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(1);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	@RequestMapping(value = "/transReject", method = RequestMethod.POST)
	public String rejectTrans(@ModelAttribute("trans") Transactions tr,
			BindingResult result, SessionStatus status, Model model) {
		Transactions transaction = transactionsService.viewTransaction(tr
				.getIdtransactions());
		transaction.setApprovalNeeded(0);
		transaction.setApproved(0);
		transactionsService.updateTrans(transaction);
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService
				.getTransToBeApproved(ur);
		model.addAttribute("transList", transL);
		model.addAttribute("trans", new Transactions());
		return "authTransList";
	}

	// Authorization for external user requests
	// A List of user generated requests pending for authorization
	@RequestMapping(value = "/ExtUserRequests", method = RequestMethod.GET)
	public String extUserReqList(Model model) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String ur = userService.getUserRoleType(username);
		List<Transactions> transL = transactionsService.getExtUserReqList(ur);
		if (transL != null) {
			for (Transactions t : transL) {
				System.out.println(t.getTransactionTypes());
				if (t.getTransactionTypes().contains("TR_EDIT"))
					t.setTransactionTypes("EDIT APPROVAL REQUEST");
				else if (t.getTransactionTypes().contains("TR_VIEW"))
					t.setTransactionTypes("VIEW USER PROFILE REQUEST APPROVED");
				else if (t.getTransactionTypes().contains("TR_VIEWTR"))
					t.setTransactionTypes("REQUEST TO REVIEW TRANSACTIONS");
			}
		}
		model.addAttribute("transList", transL);
		return "extUserRequests";
	}

	//External User Profile view request 
	
	@RequestMapping(value = "/ExtUserProfileViewReq")
	public String ExtUserProfileViewReq(ModelMap model) {
		model.addAttribute("accessInfo", new Userinfo());
		model.addAttribute("usernameerror",null);
		model.addAttribute("Message", null);
		return "reqProfileViewAccess";
	}
	
	@RequestMapping(value = "/ExtUserProfileViewReq", method = RequestMethod.POST)
	public String ExtUserProfileViewReq(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo,
			BindingResult result, SessionStatus status, Model model) {
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		model.addAttribute("Message", null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "reqProfileViewAccess";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "reqProfileViewAccess";
				}
				else
				{
					//check if the user is an external user
					String ur = userService.getUserRoleType(userInfo.getUsername());
					if(ur.equals("ROLE_CUSTOMER")||ur.equals("ROLE_MERCHANT"))
					{
						String message= transactionsService.extUsrProfileViewReq(userInfo.getUsername());
						model.addAttribute("Message",message);
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid employee");
						return "reqProfileViewAccess";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "reqProfileViewAccess";
		}
		return "reqProfileViewAccess";
	}
	
}
>>>>>>> branch 'master' of https://github.com/mounikavm/SSProject_SparkyBankingSystem.git
*/