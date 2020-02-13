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
terraform apply -var="ES_INDEX_URL=%ES_INDEX_URL_VALUE%"
```

### How to create ElasticSearch index for log records:
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
 ```
aws logs put-log-events --log-group-name %NAME_OF_CLOUDWATCH_LOGGROUP_VALUE% --log-stream-name %NAME_OF_CLOUDWATCH_LOGSTREAM_VALUE% --log-events timestamp=1580833735000,message="Test Cloudwatch message" --sequence-token %SEQUENCE_TOKEN_VALUE%
```
