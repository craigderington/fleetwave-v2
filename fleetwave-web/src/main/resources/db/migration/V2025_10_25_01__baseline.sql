create table if not exists tenant (
  id uuid primary key,
  key text not null unique,
  name text not null
);
create table if not exists department (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  name text not null,
  unique (tenant_id, name)
);
create table if not exists workgroup (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  department_id uuid not null references department(id),
  name text not null,
  unique (tenant_id, department_id, name)
);
create table if not exists radio (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  serial text not null,
  callsign text,
  model text,
  status text not null,
  created_at timestamptz not null default now(),
  unique (tenant_id, serial)
);
create table if not exists assignment (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  radio_id uuid not null references radio(id),
  assignee_type text not null,
  assignee_id uuid not null,
  start_at timestamptz not null default now(),
  expected_end timestamptz,
  end_at timestamptz,
  status text not null,
  constraint chk_assignment_status check (status in ('ACTIVE','RETURNED','CANCELLED')),
  constraint chk_assignee_type check (assignee_type in ('USER','WORKGROUP')),
  constraint chk_date_order check (end_at is null or end_at >= start_at)
);
create unique index if not exists ux_active_assignment_per_radio
  on assignment(tenant_id, radio_id)
  where end_at is null and status = 'ACTIVE';
