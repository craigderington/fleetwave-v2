package org.fleetwave.infra.notif;
public interface SmsSender { void send(String to, String body) throws Exception; }
