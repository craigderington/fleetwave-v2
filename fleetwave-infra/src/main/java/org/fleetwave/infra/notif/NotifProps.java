package org.fleetwave.infra.notif;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "notif")
public class NotifProps {
  private Email email = new Email();
  private Sms sms = new Sms();

  public Email getEmail() { return email; }
  public Sms getSms() { return sms; }

  public static class Email {
    private String provider = "ses"; // ses|smtp|mailgun
    private String from;
    private Map<String, Map<String,String>> tenants; // key -> {provider:..., from:..., host:..., username:..., password:...}
    public String getProvider(){ return provider; }
    public void setProvider(String p){ this.provider = p; }
    public String getFrom(){ return from; }
    public void setFrom(String f){ this.from = f; }
    public Map<String, Map<String, String>> getTenants() { return tenants; }
    public void setTenants(Map<String, Map<String, String>> tenants) { this.tenants = tenants; }
  }

  public static class Sms {
    private String provider = "twilio";
    private String accountSid;
    private String authToken;
    private String from;
    public String getProvider(){ return provider; }
    public void setProvider(String p){ this.provider = p; }
    public String getAccountSid(){ return accountSid; }
    public void setAccountSid(String s){ this.accountSid = s; }
    public String getAuthToken(){ return authToken; }
    public void setAuthToken(String t){ this.authToken = t; }
    public String getFrom(){ return from; }
    public void setFrom(String f){ this.from = f; }
  }
}
