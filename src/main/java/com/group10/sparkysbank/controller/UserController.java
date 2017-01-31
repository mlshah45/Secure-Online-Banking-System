package com.group10.sparkysbank.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.group10.sparkysbank.model.UserPII;
import com.group10.sparkysbank.model.Userinfo;
import com.group10.sparkysbank.service.AccountManagerService;
import com.group10.sparkysbank.service.EmailService;
import com.group10.sparkysbank.service.PKIService;
import com.group10.sparkysbank.service.TransactionsService;
import com.group10.sparkysbank.service.UserService;
import com.group10.sparkysbank.validator.UserValidator;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AccountManagerService accountManagerService;

	@Autowired
	UserValidator userValidator;

	//	@Autowired
	//	UserPIIValidator userPIIValidator;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	TransactionsService transactionsService;

	@Autowired
	private ReCaptcha recaptcha; 

	@Autowired
	PKIService pkiService;

	@Autowired
	EmailService emailService;

	@Transactional
	@RequestMapping(value="/addExtUser",method=RequestMethod.POST)
	public String submitForm(ModelMap model, @ModelAttribute ("extUser") @Validated Userinfo userInfo, BindingResult result, SessionStatus status, HttpServletRequest request, HttpServletResponse response,ServletRequest servletRequest) throws Exception
	{
		/*System.out.println("hello");
		String challangeField=request.getParameter("recaptcha_challenge_field").toString();
		String responseField=request.getParameter("recaptcha_response_field").toString();
		System.out.println("Secret="+challangeField);
		System.out.println("Secret="+responseField);

		String remoteAddress = servletRequest.getRemoteAddr();
		ReCaptchaResponse reCaptchaResponse = this.recaptcha.checkAnswer(remoteAddress, challangeField, responseField);
		if(!reCaptchaResponse.isValid()) {
			model.addAttribute("captchaerror", "captchaerror");
			result.addError(new ObjectError("", "captchaerror"));
			return "addExternalUserAccount";
		}*/


		String role=request.getParameter("role").toString();
		userValidator.validate(userInfo, result);



		if(result.hasErrors())
		{
			System.out.println("error");
			return "addExternalUserAccount";
		}
		System.out.println(userInfo.getFirstname());
		String decodedPwd = emailService.generatePassword();
		//System.out.println(userInfo.getFirstname());
		//String pass=userInfo.getPassword();
		//String pass=userInfo.getPassword();
		userInfo.setPassword(encoder.encode(decodedPwd));
		userInfo.setEnable(false);

		int accno=userService.addNewExternalUuser(userInfo,role);

		UUID uniqueToken =UUID.randomUUID();
		pkiService.generateKeyPairAndToken(userInfo.getUsername(),uniqueToken.toString());
		emailService.sendEmailWithAttachment(userInfo.getEmail(),userInfo.getUsername(),decodedPwd,uniqueToken.toString());
		model.addAttribute("accno", accno);
		return "addExternalUserAccount";

	}


	@RequestMapping(value="/piiChangeRequest")
	public String chagePIIForUser(Model model, HttpServletRequest request){

		String pii=request.getParameter("pii");
		String token=request.getParameter("token");

		if(!pii.matches("^[a-zA-Z0-9]$") || token==null|| token.equals(""))
		{
			model.addAttribute("error", "true");
		}
		UserPII userPII=new UserPII();
		userPII.setPII(pii);
		userPII.setToken(token);
		userPII.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		userService.submitChangePIIRequest(userPII);
		return "hello";
	}

	@RequestMapping(value="/ReviewPIIChange")
	public String reviewChangePIIRequest(Model model, Map<String,Object> map){

		ArrayList<UserPII> list=userService.getAllPIIChangeRequests();
		map.put("requests", list);
		return "reviewChangePIIRequest";
	}
	@RequestMapping(value="/changePII")
	public String openChangePII(){
		return "changePII";
	}

	@RequestMapping(value="/verifyPIIRequest")
	public String authenticatePIIRequest(Model model, HttpServletRequest request){

		String username=request.getParameter("username");

		if(username==null || username.equals("")){
			model.addAttribute("error", "true");
		}
		UserPII userPII=userService.getUserPIIToken(username);

		if(pkiService.authenticatePIIRequest(username,userPII.getToken())){
			System.out.println("authenticated");
			Userinfo user= userService.getUserInfobyUserName(username);
			user.setIdentificationid(userPII.getPII());
			userService.updateUserInfo(user);
			userService.deleteUserPIIRequest(userPII);
		}

		System.out.println(username);
		return "reviewChangePIIRequest";
	}
	@RequestMapping(value="/addExtUser1",method=RequestMethod.POST)
	public String submitForm1(ModelMap model, @ModelAttribute ("extUser") @Validated Userinfo userInfo, BindingResult result, SessionStatus status, HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("check");
		return "addExternalUserAccount";
	}


	//Author: Sravya
	//VIEW External Users
	@RequestMapping(value="/UserAccountManagement",method=RequestMethod.GET)
	public String viewUserAccessInfo(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "usrAccMgmt";
	}

	@RequestMapping(value="/UserAccountManagement",method=RequestMethod.POST)
	public String viewUserInfo(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		//validate input format
		//userPIIValidator.validate(userInfo, result);
		if(userInfo.getUsername()!=null)
		{
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "usrAccMgmt";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "usrAccMgmt";
				}
				else
				{
					Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
					//check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if(ur.equals("ROLE_CUSTOMER")||ur.equals("ROLE_MERCHANT"))
					{
						//check if this viewing has been authorized
						if(userService.getViewAuthorization(ui.getUsername()))
						{
							model.addAttribute("accessInfo", ui);
							return "usrAccMgmt";
						}
						else
						{
							model.addAttribute("usernameerror","Currently not authorized to view");
							return "usrAccMgmt";
						}
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid external user");
						return "usrAccMgmt";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "usrAccMgmt";
		}


	}

	//Edit External Users
	@RequestMapping(value="/EditExtProfile",method=RequestMethod.GET)
	public String editExtProfile(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		model.addAttribute("usernameerror",null);
		model.addAttribute("addresserror",null);
		model.addAttribute("editRequestMsg",null);
		return "editExtProfile";
	}

	@RequestMapping(value="/EditExtProfile",method=RequestMethod.POST)
	public String editExtProfile(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		model.addAttribute("addresserror",null);
		model.addAttribute("editRequestMsg",null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			//On clicking edit accept
			if(userInfo.getEmail()!=null)
			{
				Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername()); 
				if(userInfo.getAddress()!=null){
					if(!(userInfo.getAddress()).matches("^[a-zA-Z0-9_#]*$"))
					{
						model.addAttribute("addresserror","Please enter a valid address having characters numbers and #");
						return "editExtProfile";
					}
				}
				if(userInfo.getAddress() != ui.getAddress())
				{
					ui.setAddress(userInfo.getAddress());
				}
				userService.updateUserInfo(ui);
				model.addAttribute("accessInfo", new Userinfo());
				model.addAttribute("editRequestMsg", "Edit Request Approved!");
				return "editExtProfile";
			}
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "editExtProfile";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "editExtProfile";
				}
				else
				{
					Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
					//check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if(ur.equals("ROLE_CUSTOMER")||ur.equals("ROLE_MERCHANT"))
					{
						String address = userService.getEditAuthorization(ui.getUsername());        
						//check if this edit has been authorized
						if(address != null)
						{
							ui.setAddress(address);
							model.addAttribute("accessInfo", ui);
							return "editExtProfile";
						}
						else
						{
							model.addAttribute("usernameerror","Currently not authorized to edit");
							return "editExtProfile";
						}
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid customer");
						return "editExtProfile";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "editExtProfile";
		}


	}

	@RequestMapping(value="/RejectUpdate",method=RequestMethod.POST)
	public String rejectEditExtProfile(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		model.addAttribute("editRequestMsg", "Edit Request Rejected!");
		return "editExtProfile";
	}
	//EDIT
	@RequestMapping(value="/UserAccountManagementEdit",method=RequestMethod.GET)
	public String updateUserInfo(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "usrAccMgmtEdit";
	}

	@RequestMapping(value="/UserAccountManagementEdit",method=RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute ("accessInfo")Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//case where the verification credentials are posted
		if(userInfo.getEmail()==null && userInfo.getAddress()==null)
		{
			Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
			model.addAttribute("accessInfo", userService.getUserInfo(userInfo));
			return "usrAccMgmtEdit";
		}
		//case where the update is being made
		else
		{
			Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
			if(userInfo.getAddress() != ui.getAddress())
			{
				ui.setAddress(userInfo.getAddress());
			}
			if(userInfo.getEmail() != ui.getEmail())
			{
				ui.setEmail(userInfo.getEmail());
			}
			userService.updateUserInfo(ui);
			model.addAttribute("accessInfo", userService.getUserInfobyUserName(userInfo.getUsername()));
			return "usrAccMgmtEdit";
		}
	}

	//DELETE
	@RequestMapping(value="/UserAccountManagementDelete",method=RequestMethod.GET)
	public String deleteUserInfo(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "usrAccMgmtDelete";
	}

	@RequestMapping(value="/UserAccountManagementDelete",method=RequestMethod.POST)
	public String deleteUserInfo(@ModelAttribute ("accessInfo")Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//case where the verification credentials are posted
		if(userInfo.getEmail()==null)
		{
			Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
			model.addAttribute("accessInfo", userService.getUserInfo(userInfo));
			return "usrAccMgmtDelete";
		}
		//case where the delete is being made
		else
		{
			Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
			userService.deleteUserInfo(ui);
			return "usrAccMgmtDeleteMessage";
		}
	}

	//-------------Admin functionality 
	//View Employees
	@RequestMapping(value="/ViewEmpProfile",method=RequestMethod.GET)
	public String viewEmpProfile(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "viewEmpProfile";
	}

	@RequestMapping(value="/ViewEmpProfile",method=RequestMethod.POST)
	public String viewEmpProfile(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "viewEmpProfile";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "viewEmpProfile";
				}
				else
				{
					Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
					//check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if(ur.equals("ROLE_EMPLOYEE"))
					{
						model.addAttribute("accessInfo", ui);
						return "viewEmpProfile";
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid employee");
						return "viewEmpProfile";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "viewEmpProfile";
		}


	}

	//Edit Employees
	@RequestMapping(value="/EditEmpProfile",method=RequestMethod.GET)
	public String editEmpProfile(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "editEmpProfile";
	}

	@RequestMapping(value="/EditEmpProfile",method=RequestMethod.POST)
	public String editEmpProfile(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		model.addAttribute("addresserror",null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			if(userInfo.getEmail()!=null)
			{
				Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername()); 
				if(userInfo.getAddress()!=null){
					if(!(userInfo.getAddress()).matches("^[a-zA-Z0-9_#]*$"))
					{
						model.addAttribute("addresserror","Please enter a valid address having characters numbers and #");
						return "editEmpProfile";
					}
				}
				if(userInfo.getAddress() != ui.getAddress())
				{
					ui.setAddress(userInfo.getAddress());
				}
				userService.updateUserInfo(ui);
				model.addAttribute("accessInfo", userService.getUserInfobyUserName(userInfo.getUsername()));
				return "editEmpProfile";
			}
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "editEmpProfile";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "editEmpProfile";
				}
				else
				{
					Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
					//check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if(ur.equals("ROLE_EMPLOYEE"))
					{
						model.addAttribute("accessInfo", ui);
						return "editEmpProfile";
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid employee");
						return "editEmpProfile";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "viewEmpProfile";
		}


	}

	//Delete Employees
	@RequestMapping(value="/DeleteEmpProfile",method=RequestMethod.GET)
	public String deleteEmpProfile(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "deleteEmpProfile";
	}

	@RequestMapping(value="/DeleteEmpProfile",method=RequestMethod.POST)
	public String deleteEmpProfile(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		//add objects to model
		model.addAttribute("accessInfo", userInfo);
		model.addAttribute("usernameerror",null);
		model.addAttribute("deleteMessage",null);
		//validate input format
		if(userInfo.getUsername()!=null)
		{
			if(!(userInfo.getUsername()).matches("^[a-z0-9_-]{3,16}$"))
			{
				model.addAttribute("usernameerror","Please enter a valid username");
				return "deleteEmpProfile";
			}
			else
			{
				//validate if reasonable request and username exists
				if(userService.getUserInfobyUserName(userInfo.getUsername())==null)
				{
					model.addAttribute("usernameerror","Specified username does not exist");
					return "deleteEmpProfile";
				}
				else
				{
					Userinfo ui = userService.getUserInfobyUserName(userInfo.getUsername());
					if(!(userInfo.getEmail()==null))
						{
						   userService.deleteUserInfo(ui);
						   model.addAttribute("deleteMessage","Delete Successfull!");
						   model.addAttribute("accessInfo", new Userinfo());
						   return "deleteEmpProfile";
						}
					//check if the user is an external user
					String ur = userService.getUserRoleType(ui.getUsername());
					if(ur.equals("ROLE_EMPLOYEE"))
					{
						model.addAttribute("accessInfo", ui);
						return "deleteEmpProfile";
					}
					else
					{
						model.addAttribute("usernameerror", "Not a valid employee");
						return "deleteEmpProfile";
					}
				}
			}
		}
		else
		{
			model.addAttribute("usernameerror","Please enter the username");
			return "deleteEmpProfile";
		}


	}

	//-------------TransactionManagement
	//View Transaction management tabs
	@RequestMapping(value="/TransactionManagement",method=RequestMethod.GET)
	public String transMgmtTab(Model model)
	{
		model.addAttribute("accessInfo", new Userinfo());
		return "internalHomeTrans";
	}

	//Author: Sravya
	//EXTERNAL USER FUNCTIONALITY
	@RequestMapping(value="/viewMyProfile",method=RequestMethod.GET)
	public String viewMyself(Model model)
	{
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Userinfo user = userService.getUserInfobyUserName(username);
		model.addAttribute("accessInfo", user);
		return "viewExtInfo";
	}

	@RequestMapping(value="/requestEdit",method=RequestMethod.GET)
	public String editMyself(Model model)
	{
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Userinfo user = userService.getUserInfobyUserName(username);
		user.setIdentificationid("0");
		model.addAttribute("accessInfo", user);
		return "editMyInfo";
	}

	@RequestMapping(value="/requestEdit",method=RequestMethod.POST)
	public String editMyself(@ModelAttribute ("accessInfo") @Validated Userinfo userInfo, BindingResult result, SessionStatus status,Model model)
	{
		try
		{
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Userinfo user = userService.getUserInfobyUserName(username);
		String message= transactionsService.extUsrProfileEditReq(user.getUsername(),userInfo.getAddress());		
		model.addAttribute("msg", message);
		model.addAttribute("accessInfo", new Userinfo());
		return "editMyInfo";
		}
		catch(Exception e)
		{
			model.addAttribute("msg", "Unable to make the request, you have made a similar request which is pending approval!");
			model.addAttribute("accessInfo", new Userinfo());
			return "editMyInfo";
		}
	}

	@RequestMapping(value="/atFirstLogin")
	public String userAtFirstLogin(Model model){
		return "changePassword";
	}
}