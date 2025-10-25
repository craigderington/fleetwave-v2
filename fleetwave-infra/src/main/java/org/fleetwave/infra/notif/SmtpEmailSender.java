package org.fleetwave.infra.notif;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@ConditionalOnProperty(prefix="notif.email", name="provider", havingValue="smtp")
public class SmtpEmailSender implements EmailSender {
  @Override public void send(EmailMessage msg) throws Exception {
    Properties props = new Properties();
    // Expect SMTP_* env vars or JavaMail properties configured externally
    props.put("mail.smtp.host", System.getenv().getOrDefault("SMTP_HOST","localhost"));
    props.put("mail.smtp.port", System.getenv().getOrDefault("SMTP_PORT","25"));
    Session session = Session.getInstance(props);
    MimeMessage m = new MimeMessage(session);
    m.setFrom(new InternetAddress(msg.from()));
    m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(msg.to()));
    m.setSubject(msg.subject());
    m.setText(msg.text() != null ? msg.text() : msg.html(), "utf-8", msg.html()!=null?"html":"plain");
    Transport.send(m);
  }
}
