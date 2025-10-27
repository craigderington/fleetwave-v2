package org.fleetwave.web.dto;

import java.util.UUID;

public final class MembershipDtos {
  private MembershipDtos() {}

  public static final class AddMembershipRequest {
    public UUID personId;
    public UUID workgroupId;
    public String role; // MEMBER / MANAGER
  }
}
