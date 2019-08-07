#!/bin/bash

echo "Starting with Stack creation process......"
echo "Validating the Template......"

aws cloudformation validate-template --template-body file://csye6225-cf-lambda.json #>/dev/null 2>&1

if [[ $? -eq 0 ]]
then
    echo -n "Enter lamda stack name: "
    read APP_STACK_NAME
    echo -n "Enter s3 bucket name: "
    read S3Bucket
    echo -n "Enter Domain name: "
    read DomainName

    aws cloudformation create-stack --stack-name ${APP_STACK_NAME} --template-body file://csye6225-cf-lambda.json --parameters ParameterKey=S3Bucket,ParameterValue=${S3Bucket} ParameterKey=DomainName,ParameterValue=${DomainName} --capabilities CAPABILITY_NAMED_IAM
     # check if the
    if [[ $? -ne 0 ]]; then
        echo "Lambda  Stack creation not completed"
        exit 1
    fi
    echo "Lambda Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${APP_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Lambda Resource Stack created successfully"
    else
        echo "Stack not created"
    fi
else
    echo "Unable to validate template"
fi