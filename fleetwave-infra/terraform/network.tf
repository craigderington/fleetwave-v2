# If VPC/Subnets not provided, create a simple VPC with 2 AZs (prod users may supply existing ones)
data "aws_availability_zones" "available" {}

resource "aws_vpc" "this" {
  count                = var.vpc_id == null ? 1 : 0
  cidr_block           = "10.80.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = merge(local.tags, { Name = "${local.name}-vpc" })
}

resource "aws_internet_gateway" "igw" {
  count  = var.vpc_id == null ? 1 : 0
  vpc_id = aws_vpc.this[0].id
  tags   = merge(local.tags, { Name = "${local.name}-igw" })
}

# 2 public + 2 private subnets
resource "aws_subnet" "public" {
  count                   = var.vpc_id == null ? 2 : 0
  vpc_id                  = aws_vpc.this[0].id
  cidr_block              = cidrsubnet(aws_vpc.this[0].cidr_block, 4, count.index)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true
  tags = merge(local.tags, { Name = "${local.name}-public-${count.index}" })
}

resource "aws_subnet" "private" {
  count             = var.vpc_id == null ? 2 : 0
  vpc_id            = aws_vpc.this[0].id
  cidr_block        = cidrsubnet(aws_vpc.this[0].cidr_block, 4, count.index + 8)
  availability_zone = data.aws_availability_zones.available.names[count.index]
  tags = merge(local.tags, { Name = "${local.name}-private-${count.index}" })
}

resource "aws_route_table" "public" {
  count  = var.vpc_id == null ? 1 : 0
  vpc_id = aws_vpc.this[0].id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw[0].id
  }
  tags = merge(local.tags, { Name = "${local.name}-public-rt" })
}

resource "aws_route_table_association" "public_assoc" {
  count          = var.vpc_id == null ? 2 : 0
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public[0].id
}

# outputs
data "aws_subnet" "provided_public" {
  count = var.public_subnet_ids == null ? 0 : length(var.public_subnet_ids)
  id    = var.public_subnet_ids[count.index]
}
data "aws_subnet" "provided_private" {
  count = var.private_subnet_ids == null ? 0 : length(var.private_subnet_ids)
  id    = var.private_subnet_ids[count.index]
}

locals {
  vpc_id             = var.vpc_id == null ? aws_vpc.this[0].id : var.vpc_id
  public_subnet_ids  = var.public_subnet_ids == null ? [for s in aws_subnet.public : s.id] : var.public_subnet_ids
  private_subnet_ids = var.private_subnet_ids == null ? [for s in aws_subnet.private : s.id] : var.private_subnet_ids
}
