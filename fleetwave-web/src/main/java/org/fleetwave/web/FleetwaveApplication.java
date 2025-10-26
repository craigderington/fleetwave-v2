package org.fleetwave.web;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication(scanBasePackages={ "org.fleetwave.web","org.fleetwave.app","org.fleetwave.infra","org.fleetwave.domain" })
@EntityScan(basePackages="org.fleetwave.domain")
@EnableJpaRepositories(basePackages="org.fleetwave.domain.repo")
@EnableScheduling
public class FleetwaveApplication { public static void main(String[] args){ SpringApplication.run(FleetwaveApplication.class,args);} }
