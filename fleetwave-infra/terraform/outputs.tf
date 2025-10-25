output "alb_dns_name" { value = aws_lb.app.dns_name }
output "api_endpoint" { value = "http://${aws_route53_record.api.fqdn}" }
output "admin_endpoint" { value = "http://${aws_route53_record.admin.fqdn}" }
output "portal_endpoint" { value = "http://${aws_route53_record.portal.fqdn}" }
output "db_endpoint" { value = aws_db_instance.postgres.address }
output "ecr_repo_url" { value = aws_ecr_repository.repo.repository_url }
