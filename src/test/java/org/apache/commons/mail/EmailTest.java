package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	//some test emails used for the purpose of the tests
	private static final String[] TEST_EMAILS = {"ab@de.com","mdjds@jhddjs.com.de","lak.e@d.org"};
	
	//the email variable that uses the concrete email class we created
	private EmailConcrete email;
	
	@Before//setup method, creates an instance for email
	public void setUpEmailTest() throws Exception{
		email = new EmailConcrete();
	}
	
	@After//tear down method, nothing needed to be torn down
	public void tearDownEmailTest() throws Exception{
		email = null;//not really needed but i added it
	}
	
	/*
	 * Test for the addBcc(String... emails) function
	 */
	
	//Testing adding a bcc address with name
	@Test
	public void testAddBccEmails() throws Exception{
		email.addBcc(TEST_EMAILS);//adding the list of emails 
		assertEquals(3,email.getBccAddresses().size());//testing to see if all 3 test addresses were added
		List<InternetAddress> bccEmails = email.getBccAddresses();//store the bcc addresses in a variable to be able to access them 1 at a time
		for(int i =0;i<TEST_EMAILS.length;i++) {//loop through the test ones to check if they match the ones in bcc
			assertEquals(TEST_EMAILS[i],bccEmails.get(i).toString());//checking that it added the correct addresses
		}
	}
	
	/*
	 * Test for the addCc(String email) function
	 */
	
	//Testing adding a cc address 
	@Test
	public void testAddCc() throws Exception{
		email.addCc(TEST_EMAILS[0]);//adding the first email from the list
		assertEquals(1,email.getCcAddresses().size());//testing to see if the address was added
		List<InternetAddress> CcEmails = email.getCcAddresses();//store the Cc addresses in a variable to be able to access them 
		assertEquals(TEST_EMAILS[0],CcEmails.get(0).toString());//checking that it added the correct address
	}
	
	/*
	 * Tests for the addHeader(String name, String value) function
	 */
	
	//Testing adding a header 
	@Test
	public void testAddHeader() throws Exception{
		email.addHeader("Moe", "1");//adding the header
		assertTrue(email.headers.containsKey("Moe"));//testing to see if the header name was added
		assertEquals("1",email.headers.get("Moe"));//checking that it added the correct value associated to that name
	}
	
	//Testing adding a header with null argument
	@Test(expected = IllegalArgumentException.class)
	public void testAddHeaderIllegalException() throws Exception{
		email.addHeader(null, "1");//should throw exception because name cannot be null
	}
	
	/*
	 * Test for the addReplyTo(String email, String name) function
	 */
	
	//Testing adding a replyto address with name
	@Test
	public void testAddReplyTo() throws Exception{
		email.addReplyTo(TEST_EMAILS[0],"Moe");//adding to the replyto list the first email from test emails and the name Moe
		assertEquals(1,email.getReplyToAddresses().size());//testing to see if the replyTo address was added
	}
	
	/*
	 * Tests for the buildMimeMessage() function
	 */
	
	//testingInvoking it twice
	@Test(expected = IllegalStateException.class)//expects this exception
	public void testBuildMimeMessageCallingItTwice() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email 
		//all the above sets were used to be able to successfully build the mimeMessage
		email.buildMimeMessage();
		email.buildMimeMessage();//throws an exception because we cannot invoke buildMimeMessage more than once
	}
	
	//testing it without the proper setup
	@Test(expected = EmailException.class)//expects this exception
	public void testBuildMimeMessageNoHost() throws Exception{
		email.buildMimeMessage();//calling it without setting up from, to or host
	}
	
	//testing it without the proper setup
	@Test(expected = EmailException.class)//expects this exception
	public void testBuildMimeMessageNoFrom() throws Exception{
		email.setHostName("host");//set the host name 
		email.buildMimeMessage();//calling it without setting up from or to 
	}
	
	//testing it without the proper setup
	@Test(expected = EmailException.class)//expects this exception
	public void testBuildMimeMessageNoTo() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.buildMimeMessage();//calling it without setting up to 
	}
	
	//testing building a mimemessage with a subject that is set
	@Test
	public void testBuildMimeMessageWithSubject() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email 
		email.setSubject("Email subject");//set  subject
		email.buildMimeMessage();
		assertEquals("Email subject", email.message.getSubject());//making sure that the subject got set in the message also
	}
	
	//Testing the function with a cc instead of a to
	@Test
	public void testBuildMimeMessageWithCcInsteadOfTo() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addCc(TEST_EMAILS[2]);//set a Cc
		email.buildMimeMessage();
		assertEquals(TEST_EMAILS[2], email.message.getAllRecipients()[0].toString());//making sure it has the correct address
	}
	
	//Testing the function with a bcc
	@Test
	public void testBuildMimeMessageWithBcc() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email 
		email.addBcc(TEST_EMAILS[2]);//set a Bcc
		email.buildMimeMessage();
		assertEquals(TEST_EMAILS[2], email.message.getAllRecipients()[1].toString());//making sure it has the correct bcc address
	}
	
	//Testing the function with a replyto
	@Test
	public void testBuildMimeMessageWithReplyTo() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email 
		email.addReplyTo(TEST_EMAILS[2]);//set a ReplyTo
		email.buildMimeMessage();
		assertEquals(TEST_EMAILS[2], email.message.getReplyTo()[0].toString());//making sure it has the correct address
	}
	
	//Testing the function with a header 
	@Test
	public void testBuildMimeMessageWithHeader() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email 
		email.addHeader("Moe", "1");//adding the header
		email.buildMimeMessage();
		assertTrue(email.headers.containsKey("Moe"));//testing to see if the header name was added
		assertEquals("1",email.headers.get("Moe"));//checking that it added the correct value associated to that name
	}

	//Testing the function with content  
	@Test
	public void testBuildMimeMessageWithContent() throws Exception{
		email.setHostName("host");//set the host name 
		email.setFrom(TEST_EMAILS[0]);//set the from 
		email.addTo(TEST_EMAILS[1]);//set the receiver email
		
		//setting content
		MimeMultipart holder = new MimeMultipart();//default value
		email.setContent(holder);//setting default content
		
		email.buildMimeMessage();
		assertEquals(holder,email.message.getContent());//make sure we have the same content in the message
	}

	/*
	 * Tests for the getHostName() function
	 */
	
	//testing getting the host name after setting it
	@Test
	public void testGetHostNameUsingSet() throws Exception{
		email.setHostName("localhost");//setting the host name first, so we can use get normally
		assertEquals("localhost",email.getHostName());//testing to see if the gethostname works
	}
	
	//testing getting the host name without setting it
	@Test
	public void testGetHostNameNull() throws Exception{
		//we do not set a host name.
		assertNull(email.getHostName());//testing to see if the gethostname return null when we did not set it
	}
	
	//testing getting the host name after setting it through a session 
	@Test
	public void testGetHostNameUsingSession() throws Exception{
		//setting up session 
		Properties prop = new Properties();
	    prop.setProperty(email.MAIL_HOST,"host");//setting host name
	    Session sess = Session.getInstance(prop);
	    email.setMailSession(sess);//set mail session
		assertEquals("host",email.getHostName());//testing to see if the gethostname return null when we did not set it
	}
	
	/*
	 * Tests for the getMailSession() function
	 */
	
	//testing getting a mail session after setting it up
	@Test
	public void testGetMailSession() throws Exception{
		//setting up session
		Properties prop = new Properties();
	    Session sess = Session.getInstance(prop);
	    email.setMailSession(sess);//set mail session
		assertEquals(sess,email.getMailSession());//testing to see if the session set was retrieved using get
	}
	
	/*
	 * Tests for the getSentDate() function
	 */
	
	//testing getting the sent date after setting it 
	@Test
	public void testGetSentDate() throws Exception{
		Date thisDate = new Date();//using default date value
	    email.setSentDate(thisDate);//setting it
		assertEquals(thisDate,email.getSentDate());//testing to see if the date set was retrieved correctly using get
	}
	
	/*
	 * Tests for the getSocketConnectionTimeout() function
	 */
	
	//testing getting the socket connection timeout after setting it to 60000 ms
	@Test
	public void testGetSocketConnectionTimeout() throws Exception{
		email.setSocketConnectionTimeout(60000);//setting it
		assertEquals(60000,email.getSocketConnectionTimeout());//testing to see if the timeout set was retrieved correctly using get
	}
	
	/*
	 * Tests for the setFrom(String email) function
	 */
	
	//testing setting the from address
	@Test
	public void testSetFrom() throws Exception{
		email.setFrom(TEST_EMAILS[0]);//setting from
		assertEquals(TEST_EMAILS[0],email.getFromAddress().toString());//checking that it added the correct address
	}
	
}
