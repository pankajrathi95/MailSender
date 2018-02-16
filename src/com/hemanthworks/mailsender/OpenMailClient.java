package com.hemanthworks.mailsender;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static com.hemanthworks.mailsender.SMTPConstants.SMTP_AUTH;
import static com.hemanthworks.mailsender.SMTPConstants.SMTP_HOST;
import static com.hemanthworks.mailsender.SMTPConstants.SMTP_PORT;
import static com.hemanthworks.mailsender.SMTPConstants.SMTP_TTLS;
import static com.hemanthworks.mailsender.MimeHeaderConstants.CHARSET_UTF_8;
import static com.hemanthworks.mailsender.MimeHeaderConstants.CONTENT_TRANSFER_ENCODING;
import static com.hemanthworks.mailsender.MimeHeaderConstants.CONTENT_TYPE;
import static com.hemanthworks.mailsender.MimeHeaderConstants.HEADER_FORMAT;
import static com.hemanthworks.mailsender.MimeHeaderConstants.HTML_CONTENT;
import static com.hemanthworks.mailsender.MimeHeaderConstants.UTF_8;

public class OpenMailClient
{
	private static String OT_SMTP_HOST = " ";

	private static String OT_SMTP_PORT = " ";

	private static String OT_SMTP_AUTH = "true";

	private static String OT_SMTP_TTLS_ENABLE = "true";

	private static String HEADER_FORMAT_FLOWED = "flowed";

	private static String EIGHT_BITS = "8bit";

	private static String NO_REPLY = " ";

	private final String fromEmailAddress;

	private final String password;

	private Session session;

	public OpenTextMailClient ( String fromEmailAddress, String password )
	{
		this.fromEmailAddress = fromEmailAddress;
		this.password = password;
		this.session = createSession();
	}

	public boolean sendMail ( String toEmailAddress, String htmlBody, String subject )
	{
		try
		{
			MimeMessage message = getMimeMessage(toEmailAddress, htmlBody, subject);
			Transport.send(message);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private MimeMessage getMimeMessage ( String toEmailAddress, String htmlBody, String subject )
	        throws MessagingException, UnsupportedEncodingException, AddressException
	{
		MimeMessage message = new MimeMessage(this.session);
		message.addHeader(CONTENT_TYPE, CHARSET_UTF_8);
		message.addHeader(HEADER_FORMAT, HEADER_FORMAT_FLOWED);
		message.addHeader(CONTENT_TRANSFER_ENCODING, EIGHT_BITS);
		message.setFrom(new InternetAddress(this.fromEmailAddress, NO_REPLY));
		message.setReplyTo(InternetAddress.parse(this.fromEmailAddress, false));
		message.setSubject(subject, UTF_8);
		message.setContent(htmlBody, HTML_CONTENT);
		message.setSentDate(new Date());
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
		return message;
	}

	private Session createSession ()
	{
		Properties properties = new Properties();
		properties.put(SMTP_HOST, OT_SMTP_HOST);
		properties.put(SMTP_AUTH, OT_SMTP_AUTH);
		properties.put(SMTP_PORT, OT_SMTP_PORT);
		properties.put(SMTP_TTLS, OT_SMTP_TTLS_ENABLE);
		Authenticator authenticator = getAuthenticator(this.fromEmailAddress, this.password);
		return Session.getInstance(properties, authenticator);

	}

	private Authenticator getAuthenticator ( final String fromMailAddress, final String password )
	{
		Authenticator authenticator = new Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication ()
			{
				return new PasswordAuthentication(fromMailAddress, password);
			}
		};

		return authenticator;
	}
}
