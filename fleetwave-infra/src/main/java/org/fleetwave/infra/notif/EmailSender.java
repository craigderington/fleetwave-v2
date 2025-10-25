package org.fleetwave.infra.notif;
public interface EmailSender { void send(EmailMessage msg) throws Exception; }
