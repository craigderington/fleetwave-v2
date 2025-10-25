package org.fleetwave.infra.notif;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="notif.email", name="provider", havingValue="ses")
public class SesEmailSender implements EmailSender {
  @Override public void send(EmailMessage msg) throws Exception {
    // Placeholder: Implement with AWS SDK v2 SESv2 client
    // For scaffold, fallback to logging
    System.out.println("[SES] send to=" + msg.to() + " subject=" + msg.subject());
  }
}
