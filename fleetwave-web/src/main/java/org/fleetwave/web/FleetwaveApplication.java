package org.fleetwave.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication @EnableScheduling
public class FleetwaveApplication {
  public static void main(String[] args){ SpringApplication.run(FleetwaveApplication.class, args); }
}
