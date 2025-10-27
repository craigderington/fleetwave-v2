-- Drop the old, incorrect check
ALTER TABLE assignments
  DROP CONSTRAINT IF EXISTS assignments_status_check;

-- Re-add with the correct allowed values (exactly what your Java enum uses)
ALTER TABLE assignments
  ADD CONSTRAINT assignments_status_check
  CHECK (status IN ('REQUESTED','APPROVED','ASSIGNED','RETURNED','CANCELLED'));

