variable "project_name" { type = string  default = "fleetwave" }
variable "aws_region"   { type = string  default = "us-east-1" }
variable "domain_name"  { type = string  description = "Root domain (e.g., fleetwave.org)" }
variable "hosted_zone_id" { type = string description = "Route53 Hosted Zone ID for domain" }
variable "vpc_id"       { type = string  default = null }
variable "public_subnet_ids"  { type = list(string) default = null }
variable "private_subnet_ids" { type = list(string) default = null }

variable "db_instance_class" { type = string default = "db.t4g.medium" }
variable "db_allocated_storage" { type = number default = 20 }
variable "db_multi_az" { type = bool default = true }
variable "db_engine_version" { type = string default = "16.3" }

variable "ecr_repo_name" { type = string default = "fleetwave-web" }
variable "container_cpu" { type = number default = 512 }
variable "container_memory" { type = number default = 1024 }
variable "desired_count" { type = number default = 2 }
