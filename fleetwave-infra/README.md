# FleetWave Infra & CI

This package contains:
- **terraform/** — ECS Fargate + ALB + RDS + ECR + Route53 + ACM for `fleetwave.org`
- **.github/workflows/build-deploy.yml** — CI/CD: build JAR, build/push Docker to ECR, deploy to ECS

## How to use
1) Create an SSO/IAM user with ECR/ECS/RDS/Route53/ACM/Secrets permissions. Save keys in GitHub repo secrets:
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
2) `cd terraform && terraform init && terraform apply -var 'domain_name=fleetwave.org' -var 'hosted_zone_id=ZXXXXXXXX'`
3) Update `.github/ecs-task-def.json` with ARNs:
   - `executionRoleArn`: from Terraform output (task exec role)
   - `taskRoleArn`: from Terraform output (task role)
   - `REPLACE_ME_ECR_URL`: from Terraform output `ecr_repo_url`
   - `REPLACE_ME_DB_SECRET_ARN`: Secrets Manager ARN created by Terraform
4) Push `main` — CI builds and deploys.
5) Point tenant traffic at subdomains, e.g. `ocps.fleetwave.org` (wildcard already created).

> SES (email) and Twilio (SMS) are not provisioned here. Verify SES domain and set env/config in app for notifications.
