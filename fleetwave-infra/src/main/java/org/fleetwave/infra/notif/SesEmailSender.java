package org.fleetwave.infra.notif;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Component
@ConditionalOnProperty(prefix="notif.email", name="provider", havingValue="ses")
public class SesEmailSender implements EmailSender {
  private final SesV2Client ses = SesV2Client.builder().region(Region.AWS_GLOBAL).build();
  @Override public void send(EmailMessage msg) {
    SendEmailRequest req = SendEmailRequest.builder()
      .fromEmailAddress(msg.from())
      .destination(d -> d.toAddresses(msg.to()))
      .content(c -> c.simple(s -> s.subject(b -> b.data(msg.subject()))
        .body(b -> b.text(t -> t.data(msg.text() != null ? msg.text() : ""))
                     .html(h -> h.data(msg.html() != null ? msg.html() : "")))))
      .build();
    ses.sendEmail(req);
  }
}
