create table if not exists request (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  workgroup_id uuid not null references workgroup(id),
  requester_id uuid not null,
  radio_model_pref text,
  reason text,
  needed_from timestamptz,
  needed_until timestamptz,
  status text not null,
  created_at timestamptz not null default now()
);

create table if not exists approval (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  request_id uuid not null references request(id),
  approver_id uuid not null,
  decision text not null,
  comment text,
  decided_at timestamptz not null default now()
);

create table if not exists alert_rule (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  name text not null,
  type text not null,
  severity text not null,
  cooldown_sec int not null default 900,
  config_json jsonb not null default '{}'::jsonb,
  enabled boolean not null default true,
  unique (tenant_id, name)
);

create table if not exists alert (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  rule_id uuid not null references alert_rule(id),
  subject_type text not null,
  subject_id uuid not null,
  status text not null,
  first_seen timestamptz not null default now(),
  last_seen timestamptz not null default now(),
  count int not null default 1
);

create table if not exists notification (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  alert_id uuid not null references alert(id),
  channel text not null,
  destination text not null,
  status text not null,
  attempts int not null default 0,
  last_attempt_at timestamptz
);

create table if not exists work_order (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  radio_id uuid not null references radio(id),
  plan_name text,
  status text not null,
  opened_at timestamptz not null default now(),
  closed_at timestamptz
);
