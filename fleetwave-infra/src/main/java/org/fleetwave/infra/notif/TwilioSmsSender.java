package org.fleetwave.infra.notif;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="notif.sms", name="provider", havingValue="twilio", matchIfMissing = true)
public class TwilioSmsSender implements SmsSender {
  @Override public void send(String to, String body) throws Exception {
    // Placeholder: integrate Twilio SDK; scaffold logs
    System.out.println("[TWILIO] sms to=" + to + " body=" + body);
  }
}
