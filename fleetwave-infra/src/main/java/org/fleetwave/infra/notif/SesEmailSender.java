package org.fleetwave.infra.notif;
import org.fleetwave.app.notif.EmailMessage;
import org.fleetwave.app.notif.EmailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(prefix="notif.email", name="provider", havingValue="ses", matchIfMissing = true)
public class SesEmailSender implements EmailSender {
  @Override public void send(EmailMessage msg){ /* integrate SES later */ }
}
