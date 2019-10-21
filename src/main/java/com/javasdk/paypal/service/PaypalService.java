package com.javasdk.paypal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.PotentialPayerInfo;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import com.paypal.api.payments.PotentialPayerInfo;
import com.javasdk.paypal.config.PaypalPaymentIntent;
import com.javasdk.paypal.config.PaypalPaymentMethod;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.BaseAddress;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.PotentialPayerInfo;
import com.paypal.api.payments.ShippingInfo;
import com.paypal.api.payments.Phone;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.InvoiceAddress;
import com.paypal.api.payments.Invoice;
@Service
public class PaypalService {

	@Autowired
	private APIContext apiContext;
	
	public Payment createPayment(
			
			String currency, 
			PaypalPaymentMethod method, 
			PaypalPaymentIntent intent, 
			String description, 
			String cancelUrl, 
			String successUrl) throws PayPalRESTException{
		
		
		// ###Details
		// Let's you specify details of a payment amount.
		Details details = new Details();
		details.setShipping("0.03");
		details.setSubtotal("30.00");
		details.setTax("0.07");
		details.setInsurance("0.01");
		details.setHandlingFee("1.00");
		details.setShippingDiscount("-1.00");
		
		Amount amount = new Amount();
		amount.setCurrency(currency);
		//total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
		//amount.setTotal(String.format("%.2f", total));
		amount.setTotal("30.11");
		amount.setDetails(details);
		
		


		// ### Items
		Item item = new Item();
		
		item.setName("hat").setSku("1").setDescription("Brown hat").setPrice("5").setCurrency("USD").setTax("0.01").setQuantity("5");
		item.setName("handbag").setSku("product34").setDescription("Black handbag.").setPrice("15.00").setCurrency("USD").setTax("0.02").setQuantity("1");
		ItemList itemList = new ItemList();
		List<Item> items = new ArrayList<Item>();
		items.add(item);
		items.add(1, item);
		//items.addAll(0, items);
		//items.addAll(1, item);
		itemList.setItems(items);
		//itemList.setShippingAddress(shippingAddress)
       

		
		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		
		transaction.setItemList(itemList);
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		//System.out.println("transactions "+transactions);
//		BaseAddress ba =new BaseAddress();
//		ba.setCity("San Jose");
//		ba.setCountryCode("US");
//		ba.setLine1("4th Floor");
//		ba.setLine2("Unit #34");
//		ba.setState("CA");
//		ba.setPostalCode("95131");
//		
		
//		//System.out.println("addreess "+ad);
		
		Phone p = new Phone();
		p.setCountryCode("1");
		p.setNationalNumber("120");
		p.setExtension("255-5011");
		
		Address ad =new Address();
		ad.setPhone("2025550110");
		ad.setCity("San Jose");
		ad.setCountryCode("US");
		ad.setLine1("4th Floor");
		ad.setLine2("Unit #34");
		ad.setState("CA");
		ad.setPostalCode("95131");
		
//		InvoiceAddress ia= new InvoiceAddress();
//		ia.setCity("San Jose");
//		ia.setCountryCode("US");
//		ia.setLine1("4th Floor");
//		ia.setLine2("Unit #34");
//		ia.setState("CA");
//		ia.setPostalCode("95131");
		
//		ShippingInfo si = new ShippingInfo();
//		si.setFirstName("Brian");
//		si.setLastName("Robinson");
//		si.setPhone(p);
		//si.setAddress(ia);
//		
//PotentialPayerInfo poi =new PotentialPayerInfo();
//		pi.setBillingAddress(ad);
		//pi.setEmail("john@doe.com.au");
		
		
//		ShippingAddress sa = new ShippingAddress();
//		sa.setRecipientName("Brian Robinson");
//		sa.setDefaultAddress(true);
//		sa.setPhone("2025550110");
//		sa.setCity("Jose");
//		sa.setCountryCode("US");
//		sa.setLine1("4th Floor");
//		sa.setLine2("Unit #34");
//		sa.setState("CA");
//		sa.setPostalCode("95131");
		
		
		
		PayerInfo pi = new PayerInfo();
		//pi.setShippingAddress(sa);
		//pi.setBillingAddress(si);
		pi.setFirstName("Brian");
		pi.setLastName("Robinson");
		//pi.setPhone("2025550110");
		pi.setBillingAddress(ad);
		
		
		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());
		payer.setPayerInfo(pi);
		//payer.setPayerInfo(pi);
		
		
		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		//payment.setBillingAgreementTokens(billingAgreementTokens)
		
		//payment.setPotentialPayerInfo(poi.setBillingAddress(ad));
		//payment.setPayer(payer.setPayerInfo(pi));
		
		
		
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);
		//payment.setPayer(payer.setPayerInfo(pi));

		System.out.println("payment details "+payment);
		
		return payment.create(apiContext);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
		Payment payment = new Payment();
		payment.setId(paymentId);
		
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}
}
