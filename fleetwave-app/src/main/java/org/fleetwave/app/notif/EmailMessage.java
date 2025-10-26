package org.fleetwave.app.notif;
public final class EmailMessage {
  private final String from;
  private final String to;
  private final String subject;
  private final String html;
  private final String text;
  private final String replyTo;
  public EmailMessage(String from, String to, String subject, String html, String text, String replyTo){
    this.from = from; this.to = to; this.subject = subject; this.html = html; this.text = text; this.replyTo = replyTo;
  }
  public String getFrom(){ return from; }
  public String getTo(){ return to; }
  public String getSubject(){ return subject; }
  public String getHtml(){ return html; }
  public String getText(){ return text; }
  public String getReplyTo(){ return replyTo; }
}
