-- ==== V4: Add Authentication Fields to Person ====
-- Adds username, password hash, and roles for Spring Security integration

-- Add username column (unique per tenant)
ALTER TABLE persons
    ADD COLUMN username VARCHAR(100);

-- Add password column (BCrypt hash)
ALTER TABLE persons
    ADD COLUMN password_hash VARCHAR(255);

-- Add roles column (comma-separated: ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
ALTER TABLE persons
    ADD COLUMN roles VARCHAR(255) DEFAULT 'ROLE_USER';

-- Add enabled flag for account status
ALTER TABLE persons
    ADD COLUMN enabled BOOLEAN DEFAULT true;

-- Create unique index for username per tenant
CREATE UNIQUE INDEX ux_persons_tenant_username
    ON persons(tenant_id, username)
    WHERE username IS NOT NULL;

-- Update existing records with demo credentials
-- Password is 'admin123' hashed with BCrypt (strength 10)
-- $2a$10$/cy8Y77kM3gteHvU9qGcJehFVVoUSGLEIX.cIMFHs08dm49vhRR1u
UPDATE persons
SET username = LOWER(REPLACE(first_name || '.' || last_name, ' ', '')),
    password_hash = '$2a$10$/cy8Y77kM3gteHvU9qGcJehFVVoUSGLEIX.cIMFHs08dm49vhRR1u',
    roles = 'ROLE_USER',
    enabled = true
WHERE username IS NULL;

-- Create admin user for 'ocps' tenant if not exists
INSERT INTO persons (id, tenant_id, first_name, last_name, email, username, password_hash, roles, enabled, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'ocps',
    'Admin',
    'User',
    'admin@ocps.gov',
    'admin',
    '$2a$10$/cy8Y77kM3gteHvU9qGcJehFVVoUSGLEIX.cIMFHs08dm49vhRR1u',
    'ROLE_USER,ROLE_MANAGER,ROLE_ADMIN',
    true,
    NOW(),
    NOW()
)
ON CONFLICT DO NOTHING;

-- Make username NOT NULL for future inserts (after populating existing records)
ALTER TABLE persons
    ALTER COLUMN username SET NOT NULL;
