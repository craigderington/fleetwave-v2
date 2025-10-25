create table if not exists app_user (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  email text not null,
  name text not null,
  password_hash text not null,
  phone text,
  enabled boolean not null default true,
  unique (tenant_id, email)
);
create table if not exists role (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  name text not null,
  unique (tenant_id, name)
);
create table if not exists user_role (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  user_id uuid not null references app_user(id),
  role_id uuid not null references role(id),
  unique (tenant_id, user_id, role_id)
);
create table if not exists idempotency_key (
  id uuid primary key,
  tenant_id uuid not null references tenant(id),
  key text not null,
  created_at timestamptz not null default now(),
  unique (tenant_id, key)
);

-- Assignment assignee refactor (add FKs; keep legacy columns for now if present)
alter table assignment add column if not exists user_id uuid null;
alter table assignment add column if not exists workgroup_id uuid null references workgroup(id);
-- Optional check constraint (Postgres allows CREATE OR REPLACE only in 12+; we try-add with name)
do $$ begin
  alter table assignment add constraint chk_assignee_oneof check (
    (user_id is not null and workgroup_id is null) or
    (user_id is null and workgroup_id is not null)
  );
exception when duplicate_object then null; end $$;
