package org.fleetwave.web;
import org.fleetwave.domain.*; import org.fleetwave.domain.repo.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import java.time.OffsetDateTime; import java.util.UUID;
@Configuration public class SeedConfig {
  @Bean CommandLineRunner seed(RadioRepository radios, WorkgroupRepository wgr, AssignmentRepository arepo, AlertRuleRepository rr){
    return args -> { if (radios.count()==0){ Radio r=new Radio(); r.setId(UUID.randomUUID()); r.setSerial("APX-001"); r.setModel("APX-9000"); r.setCallsign("OCPS-1"); radios.save(r);
      Workgroup wg=new Workgroup(); wg.setId(UUID.randomUUID()); wg.setName("Dispatch"); wgr.save(wg);
      Assignment a=new Assignment(); a.setId(UUID.randomUUID()); a.setRadio(r); a.setAssigneeType(Assignment.AssigneeType.WORKGROUP); a.setAssigneeId(wg.getId()); a.setExpectedEnd(OffsetDateTime.now().minusMinutes(5)); arepo.save(a);
      AlertRule ar=new AlertRule(); ar.setId(UUID.randomUUID()); ar.setName("Overdue Assignment"); ar.setType(AlertRule.Type.OVERDUE_ASSIGNMENT); rr.save(ar); } };
  }
}
