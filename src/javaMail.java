import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class javaMail {       //TLS

	public static void sendMail(int jid) {

		final String username = "subhamgoggle@gmail.com";
		final String password = "23175467";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			
			

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("subhamgoggle@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("subham.sarkar017@gmail.com"));
			message.setSubject("Testing Subject");
			BodyPart messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setText("Your files having JOB ID "+jid+". Thank you for using our services.");
			 // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "C:/Users/Subham/NEWWORKSPACE/newbioinfo/WebContent/OUTPUT/JOB"+jid+"/JOB"+jid+".zip";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName("JOB"+jid+".zip");
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);
		//	message.setText("Your files having JOB ID "+jid+" will be mailed to you later. Thank you for using our services.");

			Transport.send(message);
			
			System.out.println("Mail Sent for JOB ID "+jid);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}