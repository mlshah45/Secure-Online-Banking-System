package com.group10.sparkysbank.service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.group10.sparkysbank.dao.OneTimePadDAO;
import com.group10.sparkysbank.dao.UserInfoDAO;
import com.group10.sparkysbank.model.OneTimePad;

@Service("otpService")
public class OneTimePadServiceImpl implements OneTimePadService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private OneTimePadDAO OneTimePadDAO;

	@Autowired
	private UserInfoDAO userinfoDao;

	public String encrypt(String username, String key) {
		if (username.length() != key.length())
			error("Lengths must be equal");
		int[] im = charArrayToInt(username.toCharArray());
		int[] ik = charArrayToInt(key.toCharArray());
		int[] data = new int[username.length()];

		for (int i = 0; i < username.length(); i++) {
			data[i] = im[i] ^ ik[i];
		}

		return new String(intArrayToChar(data));
	}

	public String decrypt(String message, String key) {
		if (message.length() != key.length())
			error("Lengths must be equal");
		int[] im = charArrayToInt(message.toCharArray());
		int[] ik = charArrayToInt(key.toCharArray());
		int[] data = new int[message.length()];

		for (int i = 0; i < message.length(); i++) {
			data[i] = im[i] ^ ik[i];
		}

		return new String(intArrayToChar(data));
	}

	private String genKey(int length) {
		SecureRandom rand = new SecureRandom();
		char[] key = new char[length];
		for (int i = 0; i < length; i++) {
			key[i] = (char) rand.nextInt(132);
			if ((int) key[i] < 97)
				key[i] = (char) (key[i] + 72);
			if ((int) key[i] > 122)
				key[i] = (char) (key[i] - 72);
		}

		return new String(key);
	}

	@Transactional
	public void generateOTP(String username) {

		OneTimePad user = OneTimePadDAO.findUserByUsername(username);
		
		if (user != null)
		{
			long currentTime = System.currentTimeMillis();
			long keyGeneratedTime = user.getCreationtime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - keyGeneratedTime);
			
			if (minutes < 5)
				return;
				
			//if minutes is greater than 5, then deletes that otp record and creates a new one
			OneTimePadDAO.deleteUserByUsername(user);
		}

		String email = userinfoDao.findUserByUsername(username).getEmail();
		String key = genKey(username.length());
		long currentTime = System.currentTimeMillis();
		String enMessage = encrypt(username, key);
		emailService.sendEmail(new String(key), email);
		String deMessage = decrypt(enMessage, key);

		user = new OneTimePad();
		user.setUsername(username);
		user.setCreationtime(currentTime);
		user.setOtp(enMessage);
		user.setEnteredkey("nokey");
		try
		{
	     	//saving otp record
			OneTimePadDAO.createOTP(user);
		} 
		catch (Exception e)
		{
			System.out.println("some error in creating otp");
			e.printStackTrace();
		}

	}

	@Transactional
	public boolean otpVerify(String key,String username) throws Exception {

		if(key.length()!=username.length())   throw new Exception("Invalid Key!");
		
		OneTimePad user = OneTimePadDAO.findUserByUsername(username);
		
		if(user==null)  throw new Exception("GO back to login page and try again!");
		
		//calculates time for otp validation
		long currentTime = System.currentTimeMillis();
		long keyGeneratedTime = user.getCreationtime();
		long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - keyGeneratedTime);

		if (minutes < 5) {
			String savedotp = user.getOtp();
			String computedotp = encrypt(username, key);
			if (savedotp.compareTo(computedotp) != 0)
				throw new Exception("Invalid Key!");
		} else if(minutes>=5){
			OneTimePadDAO.deleteUserByUsername(user);

			generateOTP(username);

			throw new Exception("Key is expired! a new key is sent to your email!");
		}

		OneTimePadDAO.deleteUserByUsername(user);
		return true;
	
	}

	private int chartoInt(char c) {
		return (int) c;
	}

	private char intToChar(int i) {
		return (char) i;
	}

	private int[] charArrayToInt(char[] cc) {
		int[] ii = new int[cc.length];
		for (int i = 0; i < cc.length; i++) {
			ii[i] = chartoInt(cc[i]);
		}
		return ii;
	}

	private char[] intArrayToChar(int[] ii) {
		char[] cc = new char[ii.length];
		for (int i = 0; i < ii.length; i++) {
			cc[i] = intToChar(ii[i]);
		}
		return cc;
	}

	private void error(String msg) {
		System.out.println(msg);
		// System.exit(-1);
	}

}
