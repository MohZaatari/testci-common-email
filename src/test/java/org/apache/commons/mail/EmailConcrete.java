package org.apache.commons.mail;

public class EmailConcrete extends Email {
//this class is created because email is abstract
	//it is used for testing purpose 
	@Override
	public Email setMsg(String msg) throws EmailException {//setmsg method that sets the body of email
		// TODO Auto-generated method stub
		return null;//this method was not implemented in email, so for now we make it return null
	}

}
