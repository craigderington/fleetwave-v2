package org.fleetwave.infra.notif;
import org.fleetwave.app.notif.SmsSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(prefix="notif.sms", name="provider", havingValue="twilio", matchIfMissing = true)
public class TwilioSmsSender implements SmsSender {
  @Override public void send(String to, String body){ /* Twilio impl later */ }
}
