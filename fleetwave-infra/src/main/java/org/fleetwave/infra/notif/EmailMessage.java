package org.fleetwave.infra.notif;
import java.util.Map;
public record EmailMessage(String from, String to, String subject, String html, String text, Map<String,String> headers) {}
