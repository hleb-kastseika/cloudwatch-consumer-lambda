provider "aws" {
  profile = "default"
  region = "us-east-1"
}

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
      ES_INDEX_URL = "https://search-cloudwatch-log-storage-lsy4kiurrnaubum73ofdijgu5a.us-east-1.es.amazonaws.com/cloudwatch-logs/"
    }
  }
}

resource "aws_lambda_permission" "cloudwatch_access_permission" {
  statement_id = "AllowExecutionFromCloudWatch"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.cloudwatch_log_consumer_lambda.function_name
  principal = "logs.amazonaws.com"
}

resource "aws_cloudwatch_log_subscription_filter" "cloudwatch_log_subscription" {
  depends_on = [
    "aws_lambda_permission.cloudwatch_access_permission"
  ]
  name = "cloudwatch_log_subscription"
  log_group_name = "lambda-handling-logs"
  filter_pattern = ""
  destination_arn = aws_lambda_function.cloudwatch_log_consumer_lambda.arn
  distribution = "Random"
}