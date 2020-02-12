![cloudwatch-consumer-lambda CI job](https://github.com/gleb-kosteiko/cloudwatch-consumer-lambda/workflows/cloudwatch-consumer-lambda%20CI%20job/badge.svg)

# cloudwatch-consumer-lambda

That AWS Lambda function listen CloudWatch log group(s) for new logs and transfer them to ElasticSearch.

### Requirements:
 - Java 11
 - Maven 3.6
 - AWS CLI
 - Terraform 0.12.20
  
### How to build:
 ```
mvn package
```

### How to create infrastructure for Lambda function in AWS:
 ```
terraform init
terraform apply
```

### How to invoke Lambda function with AWS CLI:
 ```
aws logs put-log-events --log-group-name %NAME_OF_CLOUDWATCH_LOGGROUP% --log-stream-name %NAME_OF_CLOUDWATCH_LOGSTREAM% --log-events timestamp=1580833735000,message="Test Cloudwatch message" --sequence-token %SEQUENCE_TOKEN%
```
