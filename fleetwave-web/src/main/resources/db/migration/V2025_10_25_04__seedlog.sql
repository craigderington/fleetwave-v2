create table if not exists seed_log (
  id uuid primary key,
  tenant_id uuid,
  key text not null unique,
  at timestamptz not null default now()
);
