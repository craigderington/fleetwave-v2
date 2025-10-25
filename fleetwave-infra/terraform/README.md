# FleetWave Terraform (AWS)

Creates:
- VPC (if not provided), public/private subnets
- ECR repository
- ACM certificate for `fleetwave.org` + `api/admin/portal/*.` SANs
- ALB + target group + Route53 A-records (api/admin/portal + wildcard)
- RDS Postgres (Multi-AZ optional)
- ECS Fargate Cluster + Service + Task (web)
- Secrets Manager secret for DB connection

## Usage
```bash
cd terraform
terraform init
terraform apply -var 'domain_name=fleetwave.org' -var 'hosted_zone_id=ZXXXXXXXXXXXXX'
```
After apply, push your Docker image to the created ECR and update ECS service (the CI workflow does this automatically).

## App config (task)
The task injects `DB_SECRET_JSON` from Secrets Manager. Parse it in Spring Boot on startup to construct:
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```
You can wire an `EnvironmentPostProcessor` or a small initializer bean to parse the JSON secret.
