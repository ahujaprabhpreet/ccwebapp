#!/bin/bash

echo "Starting with Stack creation process......"
echo "Validating the Template......"

aws cloudformation validate-template --template-body file://csye6225-cf-lambda.json #>/dev/null 2>&1

if [[ $? -eq 0 ]]
then

    echo -n "Enter lamda stack name: "
    read LAMBDA_STACK_NAME

    if [[ ${LAMBDA_STACK_NAME} == "" ]]; then
        echo "Please enter Lambda Stack name!"
        exit 1
    else
        echo "Lambda Stack name : ${LAMBDA_STACK_NAME}"
    fi

    echo -n "Enter S3 Code Deploy bucket name: "
    read CODE_DEPLOY_BUCKET

    if [[ ${CODE_DEPLOY_BUCKET} == "" ]]; then
        echo "Please enter Code Deploy Bucket name!"
        exit 1
    else
        echo "Code Deploy name : ${CODE_DEPLOY_BUCKET}"
    fi

    echo -n "Enter Domain name: "
    read DOMAIN_NAME

    if [[ ${DOMAIN_NAME} == "" ]]; then
        echo "Please enter Domain name!"
        exit 1
    else
        echo "Domain name : ${DOMAIN_NAME}"
    fi

    aws cloudformation create-stack --stack-name ${LAMBDA_STACK_NAME} --template-body file://csye6225-cf-lambda.json --parameters ParameterKey=S3Bucket,ParameterValue=${CODE_DEPLOY_BUCKET} ParameterKey=DomainName,ParameterValue=${DOMAIN_NAME} --capabilities CAPABILITY_NAMED_IAM
     # check if the
    if [[ $? -ne 0 ]]; then
        echo "Lambda  Stack creation not completed"
        exit 1
    fi
    echo "Lambda Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${LAMBDA_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Lambda Resource Stack created successfully"
    else
        echo "Lambda Stack not created"
    fi
else
    echo "Unable to validate template"
fi