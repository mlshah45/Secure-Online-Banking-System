package com.group10.sparkysbank.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group10.sparkysbank.dao.PKITransactionDAO;
import com.group10.sparkysbank.dao.UserPKIDAO;
import com.group10.sparkysbank.model.PKITransaction;
import com.group10.sparkysbank.model.UserPKI;

@Service("pkiService")
public class PKIService {

	@Autowired
	UserPKIDAO userPKIDAO;

	@Autowired
	PKITransactionDAO pkiTransactionDAO;

	public void generateKeyPairAndToken(String username,String token) throws Exception{


		Date startDate = new Date();              // time from which certificate is valid
		Date expiryDate =new Date();       // time after which certificate is not valid
		BigInteger serialNumber =  BigInteger.valueOf(System.currentTimeMillis());     // serial number for certificate
		KeyPairGenerator keygen=KeyPairGenerator.getInstance("RSA");
		SecureRandom sr =SecureRandom.getInstance("SHA1PRNG", "SUN");
		keygen.initialize(2048, sr);
		KeyPair pair = keygen.generateKeyPair();

		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		X500Principal dnName = new X500Principal("CN=Test CA Certificate");
		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(dnName);
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(dnName);                       // note: same as issuer
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("SHA1withRSA");;
		X509Certificate cert = certGen.generate(pair.getPrivate());

		cert.verify(pair.getPublic());
		System.out.println(displayHex(pair.getPublic().getEncoded()));
		System.out.println(displayHex(pair.getPrivate().getEncoded()));
		FileOutputStream cos = new FileOutputStream(username+".cer");
		cos.write(cert.getEncoded());

		KeyStore ks = KeyStore.getInstance("pkcs12");   
		char[] password = "123456".toCharArray();
		ks.load(null,null);
		System.out.println();
		ks.setKeyEntry("test", pair.getPrivate(), password, new Certificate[]{cert,cert});
		FileOutputStream fos = new FileOutputStream(username+".pfx");
		ks.store(fos, password);
		fos.close();

		userPKIDAO.storePublicKeyAndToken(username, pair.getPublic().getEncoded(), token);

	}

	@Transactional
	public boolean authenticateMerchant(Integer transactionId, String merchant) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

		try{
			PKITransaction transaction= pkiTransactionDAO.getTransactionFromTransactionId(transactionId);
			String mToken=transaction.getMerchantToken();
			UserPKI pkiToken= userPKIDAO.getPKIToken(merchant);
			byte[] publicKeyEncoded=pkiToken.getPublickey();
			PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyEncoded));

			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] cipherTextBytes = org.bouncycastle.util.encoders.Base64.decode(mToken);
			byte[] decryptedText = rsa.doFinal(cipherTextBytes);

			String merchantToken=new String(decryptedText);
			if(merchantToken.equals(pkiToken.getToken()))
			{
				System.out.println("authenticated");
				return true;
			}
			else{
				System.out.println("not authenticated");
				return false;
			}

		}
		catch(Exception e){
			System.out.println("not authenticated");
			return false;
		}



	}

	@Transactional
	public void savePKITransaction(PKITransaction transaction){
		pkiTransactionDAO.savePayment(transaction);
	}

	@Transactional
	public void updatePKITransaction(PKITransaction transaction){
		pkiTransactionDAO.updatePayment(transaction);
	}
	@Transactional
	public PKITransaction getTransactionFromTransactionId(Integer transactionId){
		return pkiTransactionDAO.getTransactionFromTransactionId(transactionId);
	}
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, Exception {
		PKIService pki=new PKIService();
		//pki.generateKeyPairAndToken("harry","asdsa");
		pki.authenticateMerchant(1, "pkim");


	}
	public static String displayHex(byte[] bytesKey)
	{
		String str = new String();
		for(byte b : bytesKey)
			str+=String.format("%02X ",b);
		return str;
	}
	@Transactional
	public boolean authenticateCustomer(int transactionId, String customer) {
		// TODO Auto-generated method stub
		try{
			PKITransaction transaction= pkiTransactionDAO.getTransactionFromTransactionId(transactionId);
			String cToken=transaction.getCustomerToken();
			UserPKI pkiToken= userPKIDAO.getPKIToken(customer);
			byte[] publicKeyEncoded=pkiToken.getPublickey();
			PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyEncoded));

			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] cipherTextBytes = org.bouncycastle.util.encoders.Base64.decode(cToken);
			byte[] decryptedText = rsa.doFinal(cipherTextBytes);

			String merchantToken=new String(decryptedText);
			if(merchantToken.equals(pkiToken.getToken()))
			{
				System.out.println("authenticated");
				return true;
			}
			else{
				System.out.println("not authenticated");
				return false;
			}

		}
		catch(Exception e){
			System.out.println("not authenticated");
			return false;
		}
	}
	@Transactional
	public boolean authenticatePIIRequest(String username, String token) {
		// TODO Auto-generated method stub
		try{
			UserPKI userPKI= userPKIDAO.getPKIToken(username);
			byte[] publicKeyEncoded=userPKI.getPublickey();
			PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyEncoded));

			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] cipherTextBytes = org.bouncycastle.util.encoders.Base64.decode(token);
			byte[] decryptedText = rsa.doFinal(cipherTextBytes);

			String Token=new String(decryptedText);
			if(Token.equals(userPKI.getToken()))
			{
				System.out.println("authenticated");
				return true;
			}
			else{
				System.out.println("not authenticated");
				return false;
			}

		}
		catch(Exception e){
			System.out.println("not authenticated");
			return false;
		}


	}
}
