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
Currently `main.tf` file describes next resources:
 - IAM role with policies for Lambda function which allow access to CloudWatch logs
 - Lambda function which handle CW logs and send them to ElasticSearch
 - CloudWatch log group and CloudWatch log stream on which mentioned lambda function is subscribed
 - Lambda permission for invocation of the function by logs
 - Subscription filter that specifies which CW log group should be tracked by the function
 
**Current Terraform template don't specify ElasticSearch cluster and you need to configure and to use it by you own.** 
 
Steps to create infrastructure:
1. Populate properties in `terraform.tfvars` file
2. Execute in Terminal:
 ```
terraform init
terraform apply
```

### How to create ElasticSearch index for log records:

Make next REST call using instead of %ES_INDEX_URL_VALUE% placeholder the value which you used in the previous step for ES URL.
 ```
PUT %ES_INDEX_URL_VALUE%/cloudwatch-logs

{ 
  "settings" : {
    "number_of_shards" : 1
  },
  "mappings": {
    "_doc": {
      "properties": {
        "logStream": {
          "type": "text"
        },
        "logGroup": {
          "type": "text"
        },
        "owner": {
          "type": "text"
        },
        "timestamp": {
          "type": "long"
        },
        "message": {
          "type": "text"
        },
        "id": {
          "type": "keyword"
        }
      }
    }
  }
}
```

### How to invoke Lambda function with AWS CLI:

Execute in Terminal next command using instead placeholders %CLOUDWATCH_LOGGROUP_NAME_VALUE% and %CLOUDWATCH_LOGSTREAM_NAME_VALUE% values which you used in the step with `terraform.tfvars` file.
For %SEQUENCE_TOKEN_VALUE% placeholder use value which will be provided by AWS CLI. 
 ```
aws logs put-log-events --log-group-name %CLOUDWATCH_LOGGROUP_NAME_VALUE% --log-stream-name %CLOUDWATCH_LOGSTREAM_NAME_VALUE% --log-events timestamp=1580833735000,message="Test Cloudwatch message" --sequence-token %SEQUENCE_TOKEN_VALUE%
```
