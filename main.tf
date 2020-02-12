provider "aws" {
  profile = "default"
  region = "us-east-1"
}

data "aws_iam_policy_document" "lambda_cloudwatch_logs" {
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

data "aws_iam_policy_document" "lambda_cloudwatch_logs_policy" {
  statement {
    actions = [
      "logs:*"
    ]

    resources = [
      "*"
    ]
  }

  statement {
    actions = [
      "logs:CreateLogGroup"
    ]

    resources = [
      "*"
    ]
  }
}

resource "aws_iam_role" "lambda_cloudwatch_logs" {
  name = "lambda_cloudwatch_logs"
  assume_role_policy = data.aws_iam_policy_document.lambda_cloudwatch_logs.json
}

resource "aws_iam_role_policy" "lambda_cloudwatch_logs_polcy" {
  name = "lambda_cloudwatch_logs_polcy"
  role = aws_iam_role.lambda_cloudwatch_logs.id
  policy = data.aws_iam_policy_document.lambda_cloudwatch_logs_policy.json
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
      ES_INDEX_URL = var.elaticseatch_url
    }
  }
}