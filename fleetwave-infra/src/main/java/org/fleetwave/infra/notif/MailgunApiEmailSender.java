package org.fleetwave.infra.notif;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="notif.email", name="provider", havingValue="mailgun")
public class MailgunApiEmailSender implements EmailSender {
  @Override public void send(EmailMessage msg) throws Exception {
    // Placeholder: call Mailgun HTTP API with domain and key from env/config
    System.out.println("[MAILGUN] send to=" + msg.to() + " subject=" + msg.subject());
  }
}
