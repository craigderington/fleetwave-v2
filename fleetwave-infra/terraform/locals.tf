locals {
  name = var.project_name
  tags = { Project = var.project_name }

  portal_subdomain = "portal.${var.domain_name}"
  api_subdomain    = "api.${var.domain_name}"
  admin_subdomain  = "admin.${var.domain_name}"
  wildcard_tenant  = "*.${var.domain_name}"
}
