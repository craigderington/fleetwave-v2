-- ==== INITIAL SCHEMA (V1) ====
-- Keep types simple and portable; enforce states with CHECK constraints.

-- Persons
CREATE TABLE persons (
    id         UUID PRIMARY KEY,
    tenant_id  VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255),
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX ux_persons_tenant_email
    ON persons(tenant_id, email)
    WHERE email IS NOT NULL;

-- Workgroups
CREATE TABLE workgroups (
    id         UUID PRIMARY KEY,
    tenant_id  VARCHAR(100) NOT NULL,
    name       VARCHAR(120) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,

    CONSTRAINT workgroups_name_per_tenant UNIQUE (tenant_id, name)
);

-- Memberships (person <-> workgroup)
CREATE TABLE memberships (
    id            UUID PRIMARY KEY,
    tenant_id     VARCHAR(100) NOT NULL,
    workgroup_id  UUID NOT NULL REFERENCES workgroups(id) ON DELETE CASCADE,
    person_id     UUID NOT NULL REFERENCES persons(id)    ON DELETE CASCADE,
    role          VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,

    CONSTRAINT memberships_role_check
        CHECK (role IN ('MEMBER','MANAGER')),
    CONSTRAINT memberships_unique
        UNIQUE (tenant_id, workgroup_id, person_id)
);

-- Radios
CREATE TABLE radios (
    id          UUID PRIMARY KEY,
    tenant_id   VARCHAR(100) NOT NULL,
    serial_num  VARCHAR(120) NOT NULL,
    model       VARCHAR(120),
    status      VARCHAR(120) NOT NULL default 'AVAILABLE',
    notes       TEXT,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,

    CONSTRAINT radios_serial_per_tenant UNIQUE (tenant_id, serial_num)
    CONSTRAINT radios_status_check check (status in ('AVAILABLE', 'ASSIGNED', 'IN_REPAIR', 'RETIRED'))
);

-- Assignments
CREATE TABLE assignments (
    id                  UUID PRIMARY KEY,
    tenant_id           VARCHAR(100) NOT NULL,
    radio_id            UUID NOT NULL REFERENCES radios(id) ON DELETE CASCADE,

    -- Exactly one of these is non-null (person OR workgroup)
    assignee_person_id  UUID REFERENCES persons(id)    ON DELETE SET NULL,
    assignee_workgroup_id UUID REFERENCES workgroups(id) ON DELETE SET NULL,

    status       VARCHAR(20)  NOT NULL,
    start_at     TIMESTAMPTZ  NOT NULL,
    expected_end TIMESTAMPTZ,
    end_at       TIMESTAMPTZ,

    created_at   TIMESTAMPTZ  NOT NULL,
    updated_at   TIMESTAMPTZ  NOT NULL,

    -- Status must match your Java enum
    CONSTRAINT assignments_status_check
        CHECK (status IN ('REQUESTED','APPROVED','ASSIGNED','RETURNED','CANCELLED')),

    -- Ensure exactly-one assignee rule at the DB level (optional but helpful)
    CONSTRAINT assignments_one_assignee_chk
        CHECK (
          (assignee_person_id IS NOT NULL AND assignee_workgroup_id IS NULL)
          OR
          (assignee_person_id IS NULL AND assignee_workgroup_id IS NOT NULL)
        )
);

-- Helpful indexes
CREATE INDEX ix_assignments_tenant_radio ON assignments(tenant_id, radio_id);
CREATE INDEX ix_assignments_status       ON assignments(status);
CREATE INDEX ix_memberships_tenant       ON memberships(tenant_id);
CREATE INDEX ix_radios_tenant            ON radios(tenant_id);
CREATE INDEX ix_persons_tenant_name      ON persons(tenant_id, last_name, first_name);

