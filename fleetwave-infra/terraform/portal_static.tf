resource "aws_s3_bucket" "portal" {
  bucket = "${local.name}-portal-${var.aws_region}"
  force_destroy = true
  tags = local.tags
}

resource "aws_s3_bucket_website_configuration" "portal" {
  bucket = aws_s3_bucket.portal.id
  index_document { suffix = "index.html" }
  error_document { key = "index.html" }
}

resource "aws_cloudfront_origin_access_identity" "oai" {}

resource "aws_s3_bucket_policy" "portal_policy" {
  bucket = aws_s3_bucket.portal.id
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Sid = "AllowCloudFront",
      Effect = "Allow",
      Principal = { AWS = aws_cloudfront_origin_access_identity.oai.iam_arn },
      Action = "s3:GetObject",
      Resource = "${aws_s3_bucket.portal.arn}/*"
    }]
  })
}

resource "aws_cloudfront_distribution" "portal" {
  enabled = true
  origins = [{
    domain_name = aws_s3_bucket.portal.bucket_regional_domain_name
    origin_id   = "s3-portal"
    s3_origin_config { origin_access_identity = aws_cloudfront_origin_access_identity.oai.cloudfront_access_identity_path }
  }]
  default_cache_behavior {
    allowed_methods  = ["GET","HEAD","OPTIONS"]
    cached_methods   = ["GET","HEAD","OPTIONS"]
    target_origin_id = "s3-portal"
    viewer_protocol_policy = "redirect-to-https"
  }
  default_root_object = "index.html"
  price_class = "PriceClass_100"
  viewer_certificate {
    acm_certificate_arn = aws_acm_certificate_validation.cert.certificate_arn
    ssl_support_method  = "sni-only"
    minimum_protocol_version = "TLSv1.2_2021"
  }
  restrictions { geo_restriction { restriction_type = "none" } }
  tags = local.tags
}

resource "aws_route53_record" "portal_cf" {
  zone_id = var.hosted_zone_id
  name    = "portal.${var.domain_name}"
  type    = "A"
  alias {
    name                   = aws_cloudfront_distribution.portal.domain_name
    zone_id                = aws_cloudfront_distribution.portal.hosted_zone_id
    evaluate_target_health = false
  }
}
