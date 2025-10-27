-- Align assignments.status with org.fleetwave.domain.Assignment.Status
ALTER TABLE assignments DROP CONSTRAINT IF EXISTS assignments_status_check;

ALTER TABLE assignments
  ADD CONSTRAINT assignments_status_check
  CHECK (status IN ('REQUESTED','APPROVED','ASSIGNED','RETURNED','CANCELLED'));

