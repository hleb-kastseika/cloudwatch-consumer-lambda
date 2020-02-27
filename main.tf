terraform {
  required_version = ">= 0.12.20"

  required_providers {
    aws = ">= 2.48.0"
  }
}

provider "aws" {
  profile = "default"
  region = "us-east-1"
}

variable elasticsearch_url {}
variable cloudwatch_log_group_name {}
variable cloudwatch_log_stream_name {}

data "aws_iam_policy_document" "lambda_cloudwatch_logs_document" {
  statement {
    actions = [
      "sts:AssumeRole"
    ]
    principals {
      type = "Service"
      identifiers = [
        "lambda.amazonaws.com"
      ]
    }
  }
}

data "aws_iam_policy_document" "lambda_cloudwatch_logs_policy_document" {
  statement {
    actions = [
      "logs:*"
    ]
    resources = [
      "*"
    ]
  }
}

resource "aws_iam_role" "lambda_cloudwatch_logs" {
  name = "lambda_cloudwatch_logs"
  assume_role_policy = data.aws_iam_policy_document.lambda_cloudwatch_logs_document.json
}

resource "aws_iam_role_policy" "lambda_cloudwatch_logs_policy" {
  name = "lambda_cloudwatch_logs_policy"
  role = aws_iam_role.lambda_cloudwatch_logs.id
  policy = data.aws_iam_policy_document.lambda_cloudwatch_logs_policy_document.json
}

resource "aws_lambda_function" "cloudwatch_log_consumer_lambda" {
  filename = "target/cloudwatch-log-consumer-0.0.1-SNAPSHOT.jar"
  function_name = "cloudwatch-log-consumer-lambda"
  description = "Lambda function to transmit CloudWatch logs to ElasticSearch"
  depends_on = [
    aws_iam_role.lambda_cloudwatch_logs
  ]
  role = aws_iam_role.lambda_cloudwatch_logs.arn
  runtime = "java11"
  timeout = 300
  handler = "gk.logconsumer.LogHandler::handleRequest"
  memory_size = 256
  source_code_hash = filesha256("target/cloudwatch-log-consumer-0.0.1-SNAPSHOT.jar")
  environment {
    variables = {
      ES_INDEX_URL = var.elasticsearch_url
    }
  }
}

resource "aws_cloudwatch_log_group" "lambda_cloudwatch_log_group" {
  name = var.cloudwatch_log_group_name
  retention_in_days = 14
}

resource "aws_cloudwatch_log_stream" "lambda_cloudwatch_log_stream" {
  name = var.cloudwatch_log_stream_name
  log_group_name = aws_cloudwatch_log_group.lambda_cloudwatch_log_group.name
}

resource "aws_lambda_permission" "cloudwatch_access_permission" {
  statement_id = "AllowExecutionFromCloudWatch"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.cloudwatch_log_consumer_lambda.function_name
  principal = "logs.amazonaws.com"
}

resource "aws_cloudwatch_log_subscription_filter" "cloudwatch_log_subscription" {
  depends_on = [
    aws_lambda_permission.cloudwatch_access_permission
  ]
  name = "cloudwatch_log_subscription"
  log_group_name = aws_cloudwatch_log_group.lambda_cloudwatch_log_group.name
  filter_pattern = ""
  destination_arn = aws_lambda_function.cloudwatch_log_consumer_lambda.arn
}