package org.fleetwave.app.notif;
public record EmailMessage(String from, String to, String subject, String html, String text, String replyTo) {}
