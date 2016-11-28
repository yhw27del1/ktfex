package com.kmfex.exceptions;

public class PaymentRecordChangedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PaymentRecordChangedException(String message){
		super(message);
	}

}
