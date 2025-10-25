package org.fleetwave.infra.notif;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="notif.sms", name="provider", havingValue="twilio", matchIfMissing = true)
public class TwilioSmsSender implements SmsSender {
  @Value("${notif.sms.accountSid:}") String sid;
  @Value("${notif.sms.authToken:}") String token;
  @Value("${notif.sms.from:}") String from;

  @Override public void send(String to, String body) {
    Twilio.init(sid, token);
    Message.creator(new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(from),
                    body).create();
  }
}
