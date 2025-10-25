package org.fleetwave.app.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DomainEvents {
  private final ApplicationEventPublisher publisher;
  public void publish(Object event) { publisher.publishEvent(event); }
}
